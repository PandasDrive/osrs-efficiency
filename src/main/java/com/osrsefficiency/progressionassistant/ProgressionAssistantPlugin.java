package com.osrsefficiency.progressionassistant;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import com.osrsefficiency.progressionassistant.ui.ProgressionAssistantPanel;

import java.awt.image.BufferedImage;

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

	private ProgressionAssistantPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Progression Assistant started!");

		panel = new ProgressionAssistantPanel(goalManager);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Progression Assistant")
			.icon(icon)
			.priority(10)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Progression Assistant stopped!");
		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
		}
	}

	@Provides
	ProgressionAssistantConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProgressionAssistantConfig.class);
	}
}