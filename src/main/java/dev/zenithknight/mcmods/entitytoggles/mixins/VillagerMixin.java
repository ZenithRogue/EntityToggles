package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.*;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
    public VillagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected void startTrading(Player playerByUUID){}

    @Shadow
    private int villagerXp;

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (!valueInput.read("trade_target", UUIDUtil.CODEC).isEmpty()) {
            UUID playerUUID = valueInput.read("trade_target", UUIDUtil.CODEC).get();
            this.startTrading(villager.level().getPlayerByUUID(playerUUID));
        }
    }

    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/villager/Villager;startTrading(Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean hideTrades(Villager villager, Player player){
        return !villager.entityTags().contains("hideTrades");
    }

//    @ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;isBaby()Z"))
//    private boolean babyInteractions(boolean isBaby) {
//        Villager villager = (Villager) (Object) this;
//        if (!villager.level().isClientSide()) {
//            ServerLevel level = (ServerLevel) villager.level();
//            if (level.getGameRules().getBoolean(BABY_VILLAGER_INTERACT)) {
//                return false;
//            } else {
//                return isBaby;
//            }
//        } else {
//            return isBaby;
//        }
//    }
    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/villager/Villager;setUnhappy()V"))
    private void babyInteractions(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Villager villager = (Villager) (Object) this;
        if (!villager.level().isClientSide()) {
            ServerLevel level = (ServerLevel) villager.level();
            if (level.getGameRules().get(BABY_VILLAGER_INTERACT)) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
        }
    }

    @Inject(method = "registerBrainGoals", at = @At("HEAD"), cancellable = true)
    private void registerBrainGoalsMixin(Brain<Villager> brain, CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            if (((ServerLevel) this.level()).getGameRules().get(LOBOTOMIZE_VILLAGERS)) {
                ci.cancel();
            }
        }
    }

    public void registerGoals() {
        if (!this.level().isClientSide()) {
            if (((ServerLevel) this.level()).getGameRules().get(LOBOTOMIZE_VILLAGERS)) {
                this.goalSelector.addGoal(0, new FloatGoal(this));
                this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, (double)0.5F));
                this.goalSelector.addGoal(4, new PanicGoal(this, 0.75F));
                this.goalSelector.addGoal(4, new AvoidEntityGoal(this, AbstractIllager.class, 6.0F, (double)0.75F, 1.2));
                this.goalSelector.addGoal(4, new AvoidEntityGoal(this, Zombie.class, 6.0F, (double)0.75F, 1.2));
                this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Villager.class, 6.0F));
                this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
                this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mannequin.class, 6.0F));
                this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Panda.class, 6.0F));
                this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Parrot.class, 6.0F));
                this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
                this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
            }
        }

    }
}
