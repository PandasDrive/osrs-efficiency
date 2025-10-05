package com.osrsefficiency.progressionassistant.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task
{
    private String description;
    private boolean completed;
}
