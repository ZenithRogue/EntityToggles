package dev.zenithknight.mcmods.entitytoggles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public class EntityTogglesCodecs {
    public static record PlayerAction(UUID player, long timestamp) {
        public static final Codec<EntityTogglesCodecs.PlayerAction> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(UUIDUtil.CODEC.fieldOf("player").forGetter(EntityTogglesCodecs.PlayerAction::player), Codec.LONG.fieldOf("timestamp").forGetter(EntityTogglesCodecs.PlayerAction::timestamp)).apply(instance, EntityTogglesCodecs.PlayerAction::new);
        });

        public PlayerAction(UUID player, long timestamp) {
            this.player = player;
            this.timestamp = timestamp;
        }

        public UUID player() {
            return this.player;
        }

        public long timestamp() {
            return this.timestamp;
        }
    }
}
