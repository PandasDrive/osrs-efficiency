package com.osrsefficiency.progressionassistant.ui;

import com.osrsefficiency.progressionassistant.models.Task;
import net.runelite.client.ui.PluginPanel;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.util.List;

public class ProgressionAssistantPanel extends PluginPanel
{
    private final JPanel contentPanel;

    public ProgressionAssistantPanel()
    {
        super(false);

        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.NORTH);
    }

    public void showLoading()
    {
        SwingUtilities.invokeLater(() -> {
            contentPanel.removeAll();
            contentPanel.add(new JLabel("Loading tasks..."));
            revalidate();
            repaint();
        });
    }

    public void showError()
    {
        SwingUtilities.invokeLater(() -> {
            contentPanel.removeAll();
            contentPanel.add(new JLabel("Error fetching quest requirements."));
            revalidate();
            repaint();
        });
    }

    public void updateTasks(List<Task> tasks)
    {
        SwingUtilities.invokeLater(() -> {
            contentPanel.removeAll();
            if (tasks == null || tasks.isEmpty())
            {
                showError();
                return;
            }

            for (Task task : tasks)
            {
                JLabel taskLabel = new JLabel((task.isCompleted() ? "[X] " : "[ ] ") + task.getDescription());
                contentPanel.add(taskLabel);
            }
            revalidate();
            repaint();
        });
    }
}