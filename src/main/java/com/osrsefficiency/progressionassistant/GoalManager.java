package com.osrsefficiency.progressionassistant;

import com.osrsefficiency.progressionassistant.models.Task;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.coords.WorldPoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GoalManager
{
    private final WikiApiManager wikiApiManager;
    private final StateChecker stateChecker;
    private final ClientThread clientThread;

    @Inject
    public GoalManager(WikiApiManager wikiApiManager, StateChecker stateChecker, ClientThread clientThread)
    {
        this.wikiApiManager = wikiApiManager;
        this.stateChecker = stateChecker;
        this.clientThread = clientThread;
    }

    public void getCooksAssistantTasks(Consumer<List<Task>> tasksCallback, Consumer<Exception> errorCallback)
    {
        wikiApiManager.getCooksAssistantQuest(html -> {
            clientThread.invokeLater(() -> {
                try
                {
                    boolean questComplete = stateChecker.isCooksAssistantComplete();
                    Document doc = Jsoup.parse(html);

                    List<Task> tasks = new ArrayList<>();

                    // This is a very basic and brittle parsing logic. It will need to be improved.
                    Elements requirements = doc.select(".quest-requirements li");
                    for (Element req : requirements)
                    {
                        tasks.add(new Task(req.text(), questComplete));
                    }

                    // Add item requirements
                    Task flourTask = new Task("Pot of flour", questComplete || stateChecker.hasItem(1933));
                    flourTask.setTargetObjectId(1781);
                    flourTask.setTargetLocation(new WorldPoint(3166, 3306, 2));
                    tasks.add(flourTask);

                    tasks.add(new Task("Bucket of milk", questComplete || stateChecker.hasItem(1927)));
                    tasks.add(new Task("Egg", questComplete || stateChecker.hasItem(1944)));

                    Task cookTask = new Task("Talk to the Cook in Lumbridge Castle", questComplete);
                    cookTask.setTargetNpcId(278);
                    cookTask.setTargetLocation(new WorldPoint(3207, 3214, 0));
                    tasks.add(cookTask);

                    tasksCallback.accept(tasks);
                }
                catch (Exception e)
                {
                    errorCallback.accept(e);
                }
            });
        }, error -> clientThread.invokeLater(() -> errorCallback.accept(error)));
    }

    public void getBarrowsArmourTasks(Consumer<List<Task>> tasksCallback)
    {
        clientThread.invokeLater(() -> {
            List<Task> tasks = new ArrayList<>();

            boolean priestInPerilComplete = stateChecker.isPriestInPerilComplete();
            tasks.add(new Task("Complete Priest in Peril", priestInPerilComplete));

            boolean hasSpade = stateChecker.hasItem(952);
            Task spadeTask = new Task("Get a spade", hasSpade);
            // Future: Add guidance on where to get a spade
            tasks.add(spadeTask);

            Task travelTask = new Task("Travel to the Barrows mounds", false);
            travelTask.setTargetLocation(new WorldPoint(3565, 3316, 0));
            tasks.add(travelTask);

            // Specific Barrows mounds
            tasks.add(createBarrowsMoundTask("Dig at Dharok's mound", 20672, new WorldPoint(3574, 3297, 0)));
            tasks.add(createBarrowsMoundTask("Dig at Guthan's mound", 20673, new WorldPoint(3578, 3283, 0)));
            tasks.add(createBarrowsMoundTask("Dig at Karil's mound", 20674, new WorldPoint(3566, 3276, 0)));
            tasks.add(createBarrowsMoundTask("Dig at Torag's mound", 20675, new WorldPoint(3554, 3278, 0)));
            tasks.add(createBarrowsMoundTask("Dig at Verac's mound", 20676, new WorldPoint(3557, 3291, 0)));
            tasks.add(createBarrowsMoundTask("Dig at Ahrim's mound", 20677, new WorldPoint(3565, 3298, 0)));

            tasks.add(new Task("Loot the chest in the tunnels for a chance at Barrows armour", false));

            tasksCallback.accept(tasks);
        });
    }

    private Task createBarrowsMoundTask(String description, int objectId, WorldPoint location)
    {
        Task task = new Task(description, false);
        task.setTargetObjectId(objectId);
        task.setTargetLocation(location);
        return task;
    }
}