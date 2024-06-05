package dev.attackeight.attackeight_vh.mixin;

import com.mojang.logging.LogUtils;
import iskallia.vault.config.entry.DescriptionData;
import iskallia.vault.core.vault.QuestManager;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.Objective;
import iskallia.vault.core.world.storage.VirtualWorld;
import iskallia.vault.quest.base.Quest;
import iskallia.vault.quest.type.EnterVaultQuest;
import iskallia.vault.world.data.QuestStatesData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(EnterVaultQuest.class)
public abstract class EnterVaultQuestMixin extends Quest {

    @Unique
    private static Vault attackeight_vh$vault;

    protected EnterVaultQuestMixin(String type, String id, String name, DescriptionData descriptionData, ResourceLocation icon, ResourceLocation targetId, float targetProgress, String unlockedBy, QuestReward reward) {
        super(null, id, name, descriptionData, icon, targetId, targetProgress, unlockedBy, reward);
    }

    @Inject(
            method = "initServer",
            at = @At(
                    value = "HEAD",
                    remap = false
            ),
            remap = false
    )
    private void attackeight_vh$setVault(QuestManager manager, VirtualWorld world, Vault vault, CallbackInfo ci) {
        attackeight_vh$vault = vault;
    }

    @Inject(
            method = "lambda$initServer$0",
            at = @At(
                    value = "FIELD",
                    target = "Liskallia/vault/quest/type/EnterVaultQuest;id:Ljava/lang/String;",
                    opcode = Opcodes.GETFIELD
            ),
            remap = false,
            cancellable = true)
    private void attackeight_vh$respectTargetId(ServerPlayer serverPlayer, CallbackInfo ci) {
        AtomicBoolean raw = new AtomicBoolean(false);
        attackeight_vh$vault.get(Vault.OBJECTIVES).get(0).ifPresent(b -> {
            raw.set(b.get(Objective.CHILDREN).isEmpty());
        });
        if (QuestStatesData.get().getState(serverPlayer).getInProgress().contains(this.id)) {
            if (this.targetId.getPath().equals("any")) {
                this.progress(serverPlayer, 1.0F);
            }
            if (this.targetId.getPath().equals("raw") && raw.get()) {
                this.progress(serverPlayer, 1.0F);
            }
            if (this.targetId.getPath().equals("normal") && !raw.get()) {
                this.progress(serverPlayer, 1.0F);
            }
        }
        ci.cancel();
    }

}
