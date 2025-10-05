package com.osrsefficiency.progressionassistant.models;

import net.runelite.api.coords.WorldPoint;

public class Task
{
    private String description;
    private boolean completed;
    private Integer targetNpcId;
    private Integer targetObjectId;
    private WorldPoint targetLocation;

    public Task(String description, boolean completed)
    {
        this.description = description;
        this.completed = completed;
    }

    // Getters
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public Integer getTargetNpcId() { return targetNpcId; }
    public Integer getTargetObjectId() { return targetObjectId; }
    public WorldPoint getTargetLocation() { return targetLocation; }

    // Setters
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setTargetNpcId(Integer targetNpcId) { this.targetNpcId = targetNpcId; }
    public void setTargetObjectId(Integer targetObjectId) { this.targetObjectId = targetObjectId; }
    public void setTargetLocation(WorldPoint targetLocation) { this.targetLocation = targetLocation; }
}
