package com.axalotl.async.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Mixin(Animal.class)
public abstract class AnimalMixin extends Entity {
	@Unique
	private static final ReentrantLock async$lock = new ReentrantLock();
	@Unique
	private final AtomicBoolean breedingFlag = new AtomicBoolean(false);
	@Unique
	private final AtomicBoolean breedingBabyFlag = new AtomicBoolean(false);

	public AnimalMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * Utility method to acquire a consistent lock ordering for two animals.
	 */
	@Unique
	private static Object[] async$getOrderedLocks(Animal a, Animal b) {
		if (System.identityHashCode(a) <= System.identityHashCode(b)) {
			return new Object[]{a, b};
		} else {
			return new Object[]{b, a};
		}
	}

	@WrapMethod(method = "spawnChildFromBreeding")
	private void breed(ServerLevel world, Animal other, Operation<Void> original) {
		if (this.getId() > other.getId()) {
			return;
		}
		AnimalMixin otherMixin = (AnimalMixin) (Object) other;
		if (this.breedingFlag.compareAndSet(false, true) && otherMixin.breedingFlag.compareAndSet(false, true)) {
			try {
				original.call(world, other);
			} finally {
				this.breedingFlag.set(false);
				otherMixin.breedingFlag.set(false);
			}
		}
	}

	@WrapMethod(method = "canMate")
	private boolean canBreedWith(Animal other, Operation<Boolean> original) {
		synchronized (async$lock) {
			return original.call(other);
		}
	}

	@WrapMethod(method = "finalizeSpawnChildFromBreeding")
	private void breed(ServerLevel world, Animal other, AgeableMob baby, Operation<Void> original) {
		if (this.getId() > other.getId()) return;
		AnimalMixin otherMixin = (AnimalMixin) (Object) other;
		if (this.breedingBabyFlag.compareAndSet(false, true) && otherMixin.breedingBabyFlag.compareAndSet(false, true)) {
			try {
				original.call(world, other, baby);
			} finally {
				this.breedingBabyFlag.set(false);
				otherMixin.breedingBabyFlag.set(false);
			}
		}
	}

}