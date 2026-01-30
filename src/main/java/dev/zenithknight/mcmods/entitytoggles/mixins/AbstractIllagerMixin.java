package dev.zenithknight.mcmods.entitytoggles.mixins;

import dev.zenithknight.mcmods.entitytoggles.EntityTogglesCodecs;
import dev.zenithknight.mcmods.entitytoggles.GenericMerchant;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(AbstractIllager.class)
public abstract class AbstractIllagerMixin extends LivingEntity implements GenericMerchant {
    private MerchantOffers offers;
    private EntityTogglesCodecs.PlayerAction interaction;
    private @Nullable Player tradingPlayer;

    protected AbstractIllagerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
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
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.storeNullable("interaction", EntityTogglesCodecs.PlayerAction.CODEC, this.interaction);
    }

    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        if (!valueInput.read("trade_target", UUIDUtil.CODEC).isEmpty()) {
            UUID playerUUID = valueInput.read("trade_target", UUIDUtil.CODEC).get();
            this.startTrading(this.level().getPlayerByUUID(playerUUID), this.getDisplayName());
        }
        if (!valueInput.read("data", CustomData.CODEC).isEmpty()) {
            this.offers = (MerchantOffers)valueInput.child("data").get().read("Offers", MerchantOffers.CODEC).orElse(null);
        }
        this.interaction = (EntityTogglesCodecs.PlayerAction)valueInput.read("interaction", EntityTogglesCodecs.PlayerAction.CODEC).orElse(null);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            this.interaction = new EntityTogglesCodecs.PlayerAction(player.getUUID(), this.level().getGameTime());
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (!this.level().isClientSide()) {
                boolean bl = this.getOffers().isEmpty();
                if (bl) {
                    return InteractionResult.CONSUME;
                }
                if (!this.getTags().contains("hideTrades")) {
                    this.startTrading(player, this.getDisplayName());
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
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
