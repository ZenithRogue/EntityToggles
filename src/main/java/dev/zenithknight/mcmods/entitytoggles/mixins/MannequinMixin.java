package dev.zenithknight.mcmods.entitytoggles.mixins;

import dev.zenithknight.mcmods.entitytoggles.EntityToggles;
import dev.zenithknight.mcmods.entitytoggles.EntityTogglesCodecs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mannequin.class)
public class MannequinMixin extends Avatar implements Merchant, Npc {
    protected MerchantOffers offers;
    private EntityTogglesCodecs.PlayerAction interaction;
    @Nullable
    private Player tradingPlayer;

    protected MannequinMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }


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
        if (((Mannequin) (Object) this).level().isClientSide()) {
            throw new IllegalStateException("Cannot load Villager offers on the client");
        } else {
            if (this.offers == null) {
                this.offers = new MerchantOffers();
            }

            return this.offers;
        }
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {

    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {

    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {

    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int i) {

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    public void playCelebrateSound() {
        this.makeSound(SoundEvents.VILLAGER_CELEBRATE);
    }

    @Override
    public boolean isClientSide() {
        return ((Mannequin) (Object) this).level().isClientSide();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getTradingPlayer() == player;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void addAdditionalSaveDataMixin(ValueOutput valueOutput, CallbackInfo ci) {
        if (!((Mannequin) (Object) this).level().isClientSide()) {
            MerchantOffers merchantOffers = this.getOffers();
            if (!merchantOffers.isEmpty()) {
                valueOutput.store("Offers", MerchantOffers.CODEC, merchantOffers);
            }
        }
        valueOutput.storeNullable("interaction", EntityTogglesCodecs.PlayerAction.CODEC, this.interaction);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveDataMixin(ValueInput valueInput, CallbackInfo ci) {
        this.offers = (MerchantOffers)valueInput.read("Offers", MerchantOffers.CODEC).orElse(null);
        this.interaction = (EntityTogglesCodecs.PlayerAction)valueInput.read("interaction", EntityTogglesCodecs.PlayerAction.CODEC).orElse(null);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        this.interaction = new EntityTogglesCodecs.PlayerAction(player.getUUID(), this.level().getGameTime());
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!this.level().isClientSide()) {
            boolean bl = this.getOffers().isEmpty();
            if (bl) {
                return InteractionResult.CONSUME;
            }
            this.startTrading(player);
        }
        return InteractionResult.SUCCESS;
    }

    private void startTrading(Player player) {
        this.setTradingPlayer(player);
        this.openTradingScreen(player, this.getDisplayName(), 1);
    }

    protected void stopTrading() {
        this.setTradingPlayer((Player)null);
    }
}
