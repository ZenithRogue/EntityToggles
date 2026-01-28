package dev.zenithknight.mcmods.entitytoggles;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public interface GenericMerchant extends Merchant, Npc {

    default boolean isClientSide() {
        return false;
    }

    @Override
    default void overrideOffers(MerchantOffers merchantOffers) {}

    @Override
    default void notifyTrade(MerchantOffer merchantOffer) {}

    @Override
    default void notifyTradeUpdated(ItemStack itemStack) {}

    @Override
    default int getVillagerXp() {return 0;}

    @Override
    default void overrideXp(int i) {}

    @Override
    public default boolean showProgressBar() {
        return false;
    }

    @Override
    default SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }

    default void playCelebrateSound() {
//        this.makeSound(SoundEvents.VILLAGER_CELEBRATE);
    }

    @Override
    default boolean stillValid(Player player) {
        return this.getTradingPlayer() == player;
    }

    default void startTrading(Player player, Component displayName) {
        this.setTradingPlayer(player);
        this.openTradingScreen(player, displayName, 1);
    }

    default void stopTrading() {
        this.setTradingPlayer((Player)null);
    }
}
