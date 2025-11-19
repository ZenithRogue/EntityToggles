package dev.zenithknight.mcmods.entitytoggles.mixins;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.zenithknight.mcmods.entitytoggles.EntityToggles.PARROTS_FOLLOW;

@Mixin(Parrot.class)
public class ParrotMixin {
    @Definition(id = "goalSelector", field = "Lnet/minecraft/world/entity/animal/Parrot;goalSelector:Lnet/minecraft/world/entity/ai/goal/GoalSelector;")
    @Definition(id = "addGoal", method = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V")
    @Definition(id = "FollowMobGoal", type = FollowMobGoal.class)
    @Expression("this.goalSelector.addGoal(?, new FollowMobGoal(?, ?, ?, ?))")
    @WrapWithCondition(method = "registerGoals", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean cancelFollow(GoalSelector instance, int i, Goal goal) {
        Parrot parrot = (Parrot) (Object) this;
        ServerLevel level = (ServerLevel) parrot.level();
        return level.getGameRules().getBoolean(PARROTS_FOLLOW);
    }
}
