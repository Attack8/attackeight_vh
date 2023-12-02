package dev.attackeight.attackeight_vh;

import iskallia.vault.config.entry.DescriptionData;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.AnimalJarItem;
import iskallia.vault.quest.base.Quest;
import iskallia.vault.world.data.QuestStatesData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnimalJarPickupQuest extends Quest {

    public static final String JAR_PICKUP = "jar_pickup";

    public AnimalJarPickupQuest(String id, String name, DescriptionData descriptionData, ResourceLocation icon, ResourceLocation targetId, float targetProgress, String unlockedBy, QuestReward reward) {
        super("jar_pickup", id, name, descriptionData, icon, targetId, targetProgress, unlockedBy, reward);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemInteractionForEntity(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isClientSide() && event.getTarget() instanceof Animal && !(event.getTarget() instanceof Player) && event.getPlayer() instanceof ServerPlayer p) {
            if (p.isCrouching() && p.getItemInHand(event.getHand()).getItem() == ModItems.ANIMAL_JAR && QuestStatesData.get().getState(p).getInProgress().contains(this.id)) {
                Entity targetEntity = event.getTarget();
                if (targetEntity instanceof Animal) {
                    Animal animal = (Animal)targetEntity;
                    if (!AnimalJarItem.canAddEntity(event.getItemStack(), animal) || animal.isBaby()) {
                        return;
                    }

                    if (targetEntity instanceof TamableAnimal) {
                        TamableAnimal tamableAnimal = (TamableAnimal)targetEntity;
                        if (tamableAnimal.getOwner() != null) {
                            return;
                        }
                    }

                    if (targetEntity instanceof Horse) {
                        Horse horse = (Horse)targetEntity;
                        if (horse.isTamed()) {
                            return;
                        }
                    }
                    this.progress(p, 1.0f);
                }
            }
        }

    }

    public MutableComponent getTypeDescription() {
        return new TextComponent("Pickup an animal in an animal jar");
    }
}
