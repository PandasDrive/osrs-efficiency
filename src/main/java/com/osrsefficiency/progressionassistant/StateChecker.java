package com.osrsefficiency.progressionassistant;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;

import javax.inject.Inject;

@SuppressWarnings("deprecation")
public class StateChecker
{
    private final Client client;

    @Inject
    public StateChecker(Client client)
    {
        this.client = client;
    }

    public boolean hasItem(int itemId)
    {
        ItemContainer bank = client.getItemContainer(InventoryID.BANK);
        if (bank != null && bank.contains(itemId))
        {
            return true;
        }

        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory != null && inventory.contains(itemId))
        {
            return true;
        }

        return false;
    }

    public int getSkillLevel(Skill skill)
    {
        return client.getRealSkillLevel(skill);
    }
}
