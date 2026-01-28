package dev.zenithknight.mcmods.entitytoggles.mixins;

import dev.zenithknight.mcmods.entitytoggles.EntityTogglesCodecs;
import dev.zenithknight.mcmods.entitytoggles.GenericMerchant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mannequin.class)
public class MannequinMixin extends Avatar implements GenericMerchant {
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
        if (!this.level().isClientSide()) {
            MerchantOffers merchantOffers = this.getOffers();
            if (valueOutput.child("data").isEmpty()) {
                valueOutput.store("data", CustomData.CODEC, CustomData.of(new CompoundTag()));
            }
            if (!merchantOffers.isEmpty()) {
                valueOutput.child("data").storeNullable("Offers", MerchantOffers.CODEC, merchantOffers);
            }
        }
        valueOutput.storeNullable("interaction", EntityTogglesCodecs.PlayerAction.CODEC, this.interaction);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        if (!valueInput.read("data", CustomData.CODEC).isEmpty()) {
            this.offers = (MerchantOffers)valueInput.child("data").get().read("Offers", MerchantOffers.CODEC).orElse(null);
        }
        System.out.println(this.offers);
        this.interaction = (EntityTogglesCodecs.PlayerAction)valueInput.read("interaction", EntityTogglesCodecs.PlayerAction.CODEC).orElse(null);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        super.interact(player, interactionHand);
        this.interaction = new EntityTogglesCodecs.PlayerAction(player.getUUID(), this.level().getGameTime());
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!this.level().isClientSide()) {
            boolean bl = this.getOffers().isEmpty();
            if (bl) {
                return InteractionResult.CONSUME;
            }
            this.startTrading(player, this.getDisplayName());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.tradingPlayer;
    }
    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }
}
