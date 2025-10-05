package com.osrsefficiency.progressionassistant;

import com.google.inject.Provides;
import com.osrsefficiency.progressionassistant.models.Task;
import com.osrsefficiency.progressionassistant.ui.ProgressionAssistantOverlay;
import com.osrsefficiency.progressionassistant.ui.ProgressionAssistantPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "OSRS Progression Assistant",
	tags = {"osrs", "progression", "assistant", "helper", "quest", "skill", "goal"},
	description = "Provides personalized, in-game progression guidance."
)
public class ProgressionAssistantPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ProgressionAssistantConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private GoalManager goalManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ProgressionAssistantOverlay overlay;

	@Inject
	private EventBus eventBus;

	private ProgressionAssistantPanel panel;
	private NavigationButton navButton;

	private List<Task> tasks;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Progression Assistant started!");

		panel = new ProgressionAssistantPanel();
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Progression Assistant")
			.icon(icon)
			.priority(10)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
		overlayManager.add(overlay);
		eventBus.register(this);

		if (client.getGameState() == GameState.LOGGED_IN)
		{
			loadTasks();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Progression Assistant stopped!");
		eventBus.unregister(this);
		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
		}
		overlayManager.remove(overlay);
	}

	private void loadTasks()
	{
		panel.showLoading();
		goalManager.getBarrowsArmourTasks(
			loadedTasks -> {
				this.tasks = loadedTasks;
				panel.updateTasks(loadedTasks);
			}
		);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			loadTasks();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
		// For now, we reload on any item change. This can be optimized later.
		loadTasks();
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	@Provides
	ProgressionAssistantConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProgressionAssistantConfig.class);
	}
}
