package dev.zenithknight.mcmods.entitytoggles.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

import static net.minecraft.core.component.DataComponents.CUSTOM_DATA;

@Mixin(MovingPistonBlock.class)
public class MovingPistonMixin {
    Level level = null;
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getShapeMixin(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        boolean render = false;
        BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);
//        System.out.println("Getting Shape");
        if (blockEntity != null || level != null) {
            if (blockEntity != null) {
                level = Objects.requireNonNull(blockEntity.getLevel());
            }
//            System.out.println("Checking for Players");
//            level.addParticle(ParticleTypes.ANGRY_VILLAGER, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1,1,1);
            List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(blockPos).inflate(8));
            for (Player player : players) {
//                System.out.println("Checking Player");
                CustomData item = player.getWeaponItem().getComponents().get(CUSTOM_DATA);
//                System.out.println(item);
                if (item != null && item.contains("movingPiston")) {
//                    System.out.println("Found Moving Piston");
                    render = true;
                }
            }
        }
        if (render) {
            cir.setReturnValue(Shapes.block());
        } else {
            cir.setReturnValue(Shapes.empty());
        }
    }
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.FAIL);
        cir.cancel();
    }
}
