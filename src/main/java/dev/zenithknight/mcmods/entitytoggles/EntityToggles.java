package dev.zenithknight.mcmods.entitytoggles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;


import static net.minecraft.core.component.DataComponents.*;
import static net.minecraft.world.item.CreativeModeTabs.OP_BLOCKS;
import static net.minecraft.world.level.block.entity.BlockEntityTypes.COMMAND_BLOCK;


public class EntityToggles implements ModInitializer {
    public static final String MOD_ID = "entitytoggles";

    public static final GameRule<Boolean> CHICKENS_LAY_EGGS = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("chickens_lay_eggs"));
    public static final GameRule<Boolean> EGGS_HATCH = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("eggs_hatch"));
    public static final GameRule<Boolean> ENDER_PEARL_DAMAGE = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MISC).buildAndRegister(EntityToggles.id("ender_pearl_damage"));
    public static final GameRule<Boolean> ENDERMITE_SPAWN = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.SPAWNING).buildAndRegister(EntityToggles.id("spawn_endermite"));
    public static final GameRule<Boolean> PARROTS_FOLLOW = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("parrots_follow"));
    public static final GameRule<Boolean> FROGSPAWN_HATCH = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("frogspawn_hatch"));
    public static final GameRule<Boolean> CORAL_DRIES = GameRuleBuilder.forBoolean(true).category(GameRuleCategory.MISC).buildAndRegister(EntityToggles.id("coral_dries"));
    public static final GameRule<Boolean> BABY_VILLAGER_INTERACT = GameRuleBuilder.forBoolean(false).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("baby_villager_interact"));
    public static final GameRule<Boolean> LOBOTOMIZE_VILLAGERS = GameRuleBuilder.forBoolean(false).category(GameRuleCategory.MOBS).buildAndRegister(EntityToggles.id("lobotomize_villagers"));

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void modifyEntries(FabricCreativeModeTabOutput entries) {
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

        itemStack.set(ITEM_MODEL, Identifier.parse("entitytoggles:moving_piston"));
        itemStack.set(ITEM_NAME, Component.literal("Moving Piston").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
        entries.accept(itemStack);

        entries.accept(createStack("debug_stick[lore=[{\"color\":\"light_purple\",\"italic\":true,\"text\":\"This one can interact with Moving Pistons\"}],custom_data={movingPiston:1b}]", provider));
        entries.accept(createStack("wooden_axe[lore=[{\"italic\":false,\"text\":\"World Edit Wand\"}]]", provider));
    }

    @Override
    public void onInitialize() {
        CreativeModeTabEvents.modifyOutputEvent(OP_BLOCKS).register(EntityToggles::modifyEntries);
    }
    public static ItemStack createStack(String string, HolderLookup.Provider registries) {
        ItemInput result = null;
        try {
            result = new ItemParser(registries).parse(new StringReader(string));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return new ItemStack(result.item(), 1, result.components());
    }
}
