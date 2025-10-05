package com.osrsefficiency.progressionassistant;

import com.osrsefficiency.progressionassistant.models.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoalManager
{
    private final WikiApiManager wikiApiManager;
    private final StateChecker stateChecker;

    @Inject
    public GoalManager(WikiApiManager wikiApiManager, StateChecker stateChecker)
    {
        this.wikiApiManager = wikiApiManager;
        this.stateChecker = stateChecker;
    }

    public List<Task> getCooksAssistantTasks() throws IOException
    {
        String html = wikiApiManager.getCooksAssistantQuest();
        Document doc = Jsoup.parse(html);

        List<Task> tasks = new ArrayList<>();

        // This is a very basic and brittle parsing logic. It will need to be improved.
        Elements requirements = doc.select(".quest-requirements li");
        for (Element req : requirements)
        {
            tasks.add(new Task(req.text(), false));
        }

        // Add item requirements
        tasks.add(new Task("Pot of flour", stateChecker.hasItem(1933)));
        tasks.add(new Task("Bucket of milk", stateChecker.hasItem(1927)));
        tasks.add(new Task("Egg", stateChecker.hasItem(1944)));

        return tasks;
    }
}
