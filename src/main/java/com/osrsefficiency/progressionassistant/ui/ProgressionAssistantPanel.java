package com.osrsefficiency.progressionassistant.ui;

import com.osrsefficiency.progressionassistant.GoalManager;
import com.osrsefficiency.progressionassistant.models.Task;
import net.runelite.client.ui.PluginPanel;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.io.IOException;
import java.util.List;

public class ProgressionAssistantPanel extends PluginPanel
{
    private final GoalManager goalManager;

    public ProgressionAssistantPanel(GoalManager goalManager)
    {
        super();
        this.goalManager = goalManager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try
        {
            List<Task> tasks = goalManager.getCooksAssistantTasks();
            for (Task task : tasks)
            {
                add(new JLabel((task.isCompleted() ? "[X] " : "[ ] ") + task.getDescription()));
            }
        }
        catch (IOException e)
        {
            add(new JLabel("Error fetching quest requirements."));
        }
    }
}