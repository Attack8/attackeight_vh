package dev.attackeight.attackeight_vh.mixin;

import dev.attackeight.attackeight_vh.AnimalJarPickupQuest;
import dev.attackeight.attackeight_vh.AttackeightVHMod;
import iskallia.vault.config.entry.DescriptionData;
import iskallia.vault.quest.base.Quest;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Quest.Builder.class)
public abstract class QuestTypeMixin {

    @Shadow(remap = false)
    private String type;
    @Shadow(remap = false)
    private String id;
    @Shadow(remap = false)
    private String name;
    @Shadow(remap = false)
    private DescriptionData descriptionData;
    @Shadow(remap = false)
    private ResourceLocation icon;
    @Shadow(remap = false)
    private ResourceLocation targetId;
    @Shadow(remap = false)
    private float targetProgress;
    @Shadow(remap = false)
    private String unlockedBy;
    @Shadow(remap = false)
    private Quest.QuestReward reward;

    @Inject(
            method = "build",
            at = @At(
                    value = "FIELD",
            target = "Liskallia/vault/quest/base/Quest$Builder;type:Ljava/lang/String;",
            opcode = Opcodes.GETFIELD),
            remap = false,
            cancellable = true)
    private void addJarPickupQuestInBuild(CallbackInfoReturnable<Quest> cir) {
        AttackeightVHMod.LOGGER.info("a8vh1");
        if (this.type.equals("jar_pickup")) {
            cir.setReturnValue(new AnimalJarPickupQuest(this.id, this.name, this.descriptionData, this.icon, this.targetId, this.targetProgress, this.unlockedBy, this.reward));
        }
    }
}
