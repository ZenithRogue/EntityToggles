package dev.zenithknight.mcmods.entitytoggles;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class EntityToggles implements ModInitializer {
    public static final GameRules.Key<GameRules.BooleanValue> CHICKENS_LAY_EGGS = GameRuleRegistry.register("chickensLayEggs", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> EGGS_HATCH = GameRuleRegistry.register("eggsHatch", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> ENDER_PEARL_DAMAGE = GameRuleRegistry.register("enderPearlDamage", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanValue> ENDERMITE_SPAWN = GameRuleRegistry.register("spawnEndermite", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
    }
}
