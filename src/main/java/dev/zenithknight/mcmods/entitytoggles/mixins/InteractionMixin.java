package dev.zenithknight.mcmods.entitytoggles.mixins;

import dev.zenithknight.mcmods.entitytoggles.GenericMerchant;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.desktop.SystemEventListener;
import java.util.UUID;

@Mixin(Interaction.class)
public class InteractionMixin implements GenericMerchant {
    private @Nullable Player tradingPlayer;
    private MerchantOffers offers;

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.tradingPlayer;
    }
    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    public MerchantOffers getOffers() {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot load offers on the client");
        } else {
            if (this.offers == null) {
                this.offers = new MerchantOffers();
            }
            return this.offers;
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (!valueInput.read("trade_target", UUIDUtil.CODEC).isEmpty()) {
            UUID playerUUID = valueInput.read("trade_target", UUIDUtil.CODEC).get();
            this.startTrading(entity.level().getPlayerByUUID(playerUUID), entity.getDisplayName());
        }
        if (!valueInput.read("data", CustomData.CODEC).isEmpty()) {
            this.offers = (MerchantOffers)valueInput.child("data").get().read("Offers", MerchantOffers.CODEC).orElse(null);
        }
    }
    @Inject(method = "interact", at = @At("HEAD"))
    public void interact(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Entity entity = (Entity) (Object) this;
        if (!this.getOffers().isEmpty() && !entity.getTags().contains("hideTrades")) {
            this.startTrading(player, entity.getDisplayName());
        }
    }

}
