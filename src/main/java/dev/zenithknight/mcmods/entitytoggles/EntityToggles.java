package dev.zenithknight.mcmods.entitytoggles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.GameRules;

import java.util.UUID;

import static net.minecraft.core.component.DataComponents.*;
import static net.minecraft.world.item.CreativeModeTabs.OP_BLOCKS;
import static net.minecraft.world.level.block.entity.BlockEntityType.COMMAND_BLOCK;


public class EntityToggles implements ModInitializer {
    public static final String MOD_ID = "entitytoggles";
    public static final GameRules.Key<GameRules.BooleanValue> CHICKENS_LAY_EGGS = GameRuleRegistry.register("chickensLayEggs", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> EGGS_HATCH = GameRuleRegistry.register("eggsHatch", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> ENDER_PEARL_DAMAGE = GameRuleRegistry.register("enderPearlDamage", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> ENDERMITE_SPAWN = GameRuleRegistry.register("spawnEndermite", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> PARROTS_FOLLOW = GameRuleRegistry.register("parrotsFollow", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> FROGSPAWN_HATCH = GameRuleRegistry.register("frogspawnHatch", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> CORAL_DRIES = GameRuleRegistry.register("coralDries", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    private static void modifyEntries(FabricItemGroupEntries entries) {
        assert Minecraft.getInstance().level != null;
        HolderLookup.Provider provider = Minecraft.getInstance().level.registryAccess();

        ItemStack itemStack = new ItemStack(Items.COMMAND_BLOCK);

        CompoundTag customData = (new CompoundTag());
        customData.putBoolean("movingPiston", true);
        itemStack.set(CUSTOM_DATA, CustomData.of(customData));

        CompoundTag blockEntityData = new CompoundTag();
        blockEntityData.putBoolean("auto", true);
        blockEntityData.putString("id", "command_block");
        blockEntityData.putString("Command", "setblock ~ ~ ~ moving_piston");
        itemStack.set(BLOCK_ENTITY_DATA, TypedEntityData.of(COMMAND_BLOCK, blockEntityData));

        itemStack.set(ITEM_MODEL, ResourceLocation.parse("entitytoggles:moving_piston"));
        itemStack.set(ITEM_NAME, Component.literal("Moving Piston").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
        entries.accept(itemStack);

        entries.accept(createStack("debug_stick[lore=[{\"color\":\"light_purple\",\"italic\":true,\"text\":\"This one can interact with Moving Pistons\"}],custom_data={movingPiston:1b}]", provider));
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(OP_BLOCKS).register(EntityToggles::modifyEntries);
    }
    public static ItemStack createStack(String string, HolderLookup.Provider provider) {
        ItemParser.ItemResult result = null;
        try {
            result = new ItemParser(provider).parse(new StringReader(string));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return new ItemStack(result.item(), 1, result.components());
    }
}
