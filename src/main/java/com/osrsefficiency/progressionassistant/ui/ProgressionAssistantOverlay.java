package com.osrsefficiency.progressionassistant.ui;

import com.osrsefficiency.progressionassistant.ProgressionAssistantPlugin;
import com.osrsefficiency.progressionassistant.models.Task;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

public class ProgressionAssistantOverlay extends Overlay
{
    private final Client client;
    private final ProgressionAssistantPlugin plugin;

    @Inject
    private ProgressionAssistantOverlay(Client client, ProgressionAssistantPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        List<Task> tasks = plugin.getTasks();
        if (tasks == null || tasks.isEmpty())
        {
            return null;
        }

        Task currentTask = tasks.stream()
            .filter(t -> !t.isCompleted())
            .findFirst()
            .orElse(null);

        if (currentTask == null)
        {
            client.clearHintArrow();
            return null;
        }

        if (currentTask.getTargetLocation() != null)
        {
            client.setHintArrow(currentTask.getTargetLocation());
        }
        else
        {
            client.clearHintArrow();
        }

        if (currentTask.getTargetNpcId() != null)
        {
            for (NPC npc : client.getNpcs())
            {
                if (npc.getId() == currentTask.getTargetNpcId())
                {
                    OverlayUtil.renderActorOverlay(graphics, npc, npc.getName(), Color.CYAN);
                }
            }
        }

        if (currentTask.getTargetObjectId() != null)
        {
            GameObject object = findGameObject(currentTask.getTargetObjectId());
            if (object != null)
            {
                OverlayUtil.renderTileOverlay(graphics, object, "", Color.CYAN);
            }
        }

        return null;
    }

    private GameObject findGameObject(int id)
    {
        Scene scene = client.getScene();
        if (scene == null) return null;
        Tile[][][] tiles = scene.getTiles();
        for (int z = 0; z < Constants.MAX_Z; z++)
        {
            for (int x = 0; x < Constants.SCENE_SIZE; x++)
            {
                for (int y = 0; y < Constants.SCENE_SIZE; y++)
                {
                    Tile tile = tiles[z][x][y];
                    if (tile == null)
                    {
                        continue;
                    }
                    for (GameObject gameObject : tile.getGameObjects())
                    {
                        if (gameObject != null && gameObject.getId() == id)
                        {
                            return gameObject;
                        }
                    }
                }
            }
        }
        return null;
    }
}
