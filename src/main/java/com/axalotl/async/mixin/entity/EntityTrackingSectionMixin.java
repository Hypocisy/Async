package com.axalotl.async.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.locks.ReentrantLock;

@Mixin(EntityTrackingSection.class)
public class EntityTrackingSectionMixin<T extends EntityLike> {
    @Unique
    private static final ReentrantLock lock = new ReentrantLock();

    @WrapMethod(method = "add")
    private void add(T entity, Operation<Void> original) {
        synchronized (lock) {
            original.call(entity);
        }
    }
    @WrapMethod(method = "remove")
    private boolean remove(T entity, Operation<Boolean> original) {
        synchronized (lock) {
            return original.call(entity);
        }
    }
}
