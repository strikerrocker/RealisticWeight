package io.github.strikerrocker.realw.events;

import io.github.strikerrocker.realw.api.ItemWeight;
import io.github.strikerrocker.realw.api.player_weight.IWeight;
import io.github.strikerrocker.realw.api.player_weight.WeightProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

import static io.github.strikerrocker.realw.RealisticWeight.gamestages;

public class InventoryListener implements IContainerListener {

    private final EntityPlayerMP owner;

    InventoryListener(EntityPlayerMP owner) {
        this.owner = owner;
    }


    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        updateWeight(owner);
    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

    }

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory) {

    }

    private void updateWeight(EntityPlayer player) {
        if (player != null) {
            IWeight pWeight = player.getCapability(WeightProvider.WEIGHT_CAP, null);
            List<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);
            pWeight.setWeight(1);
            inventory.addAll(player.inventory.offHandInventory);
            inventory.addAll(player.inventory.armorInventory);
            if (gamestages) {
                int a = 1;
                for (ItemStack stack : inventory) {
                    int i = ItemWeight.getStackWeight(stack, player);
                    a = a + i;
                }
                int added = ((pWeight.getWeight() - pWeight.getApiWeight()) - a);
                pWeight.addWeight(added);
            } else {
                int a = 1;
                for (ItemStack stack : inventory) {
                    int i = ItemWeight.getStackWeight(stack);
                    a = a + i;
                }
                int added = ((pWeight.getWeight() - pWeight.getApiWeight()) - a);
                pWeight.addWeight(added);
            }
        }
    }
}