package com.osrsefficiency.progressionassistant;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ProgressionAssistantPluginTest
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ProgressionAssistantPlugin.class);
		RuneLite.main(args);
	}
}