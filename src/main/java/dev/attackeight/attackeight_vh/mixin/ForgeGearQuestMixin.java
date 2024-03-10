package dev.attackeight.attackeight_vh.mixin;

import iskallia.vault.VaultMod;
import iskallia.vault.config.entry.DescriptionData;
import iskallia.vault.event.event.ForgeGearEvent;
import iskallia.vault.quest.base.Quest;
import iskallia.vault.quest.type.ForgeGearQuest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(ForgeGearQuest.class)
public abstract class ForgeGearQuestMixin extends Quest {

    protected ForgeGearQuestMixin(String id, String name, DescriptionData descriptionData, ResourceLocation icon, ResourceLocation targetId, float targetProgress, String unlockedBy, QuestReward reward) {
        super(null, id, name, descriptionData, icon, targetId, targetProgress, unlockedBy, reward);
    }

    @Inject(
            method = "onForgeGear",
            at = @At(
                    value = "FIELD",
            target = "Liskallia/vault/quest/type/ForgeGearQuest;targetId:Lnet/minecraft/resources/ResourceLocation;",
            opcode = Opcodes.GETFIELD),
            remap = false)
    private void addJarPickupQuestInBuild(ForgeGearEvent event, CallbackInfo ci) {
        Player var3 = event.getPlayer();
        if (var3 instanceof ServerPlayer player) {
            if (Objects.equals(this.targetId, VaultMod.id( "tool"))) {
                if (event.getRecipeId().getPath().contains("tool")) {
                    this.progress(player, 1.0F);
                }
            }
        }
    }
}