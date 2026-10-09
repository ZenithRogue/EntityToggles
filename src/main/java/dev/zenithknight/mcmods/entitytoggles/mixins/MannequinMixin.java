package dev.zenithknight.mcmods.entitytoggles.mixins;

import dev.zenithknight.mcmods.entitytoggles.EntityTogglesCodecs;
import dev.zenithknight.mcmods.entitytoggles.GenericMerchant;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Mannequin.class)
public abstract class MannequinMixin extends Avatar implements GenericMerchant {
    protected MerchantOffers offers;
    private EntityTogglesCodecs.PlayerAction interaction;
    @Nullable
    private Player tradingPlayer;

    protected MannequinMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.level().isClientSide()) {
            throw new IllegalStateException("Cannot load offers on the client");
        } else {
            if (this.offers == null) {
                this.offers = new MerchantOffers();
            }
            return this.offers;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void addAdditionalSaveDataMixin(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.storeNullable("interaction", EntityTogglesCodecs.PlayerAction.CODEC, this.interaction);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        if (!valueInput.read("trade_target", UUIDUtil.CODEC).isEmpty()) {
            UUID playerUUID = valueInput.read("trade_target", UUIDUtil.CODEC).get();
            this.startTrading(this.level().getPlayerByUUID(playerUUID), this.getDisplayName());
        }
        if (!valueInput.read("data", CustomData.CODEC).isEmpty()) {
            this.offers = (MerchantOffers)valueInput.child("data").get().read("Offers", MerchantOffers.CODEC).orElse(null);
        }
        this.interaction = (EntityTogglesCodecs.PlayerAction)valueInput.read("interaction", EntityTogglesCodecs.PlayerAction.CODEC).orElse(null);
    }

    public InteractionResult interact(Player player, InteractionHand interactionHand, final Vec3 location) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            super.interact(player, interactionHand, location);
            this.interaction = new EntityTogglesCodecs.PlayerAction(player.getUUID(), this.level().getGameTime());
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (!this.level().isClientSide()) {
                boolean bl = this.getOffers().isEmpty();
                if (bl) {
                    return InteractionResult.CONSUME;
                }
                if (!this.entityTags().contains("hideTrades")) {
                    this.startTrading(player, this.getDisplayName());
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public boolean isClientSide() {
        return this.level().isClientSide();
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.tradingPlayer;
    }
    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

//    public void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
////        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
////        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
////        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Villager.class, 6.0F));
////        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
//        this.goalSelector.addGoal(1, new FollowMobGoal(this, (double)1.0F, 3.0F, 7.0F));
//        this.goalSelector.addGoal(2, new RandomStrollGoal(this, (double)0.5));
//        this.goalSelector.addGoal(2, new PanicGoal(this, 0.75));
//        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Villager.class, 6.0F));
//        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
//        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
//        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
//    }
}
