package com.axalotl.async.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.locks.ReentrantLock;

@Mixin(Brain.class)
public class BrainMixin {
	@Unique
	private static final ReentrantLock async$lock = new ReentrantLock();

	@WrapMethod(method = "tick")
	private void tick(ServerLevel level, LivingEntity entity, Operation<Void> original) {
		synchronized (async$lock) {
			original.call(level, entity);
		}
	}
}