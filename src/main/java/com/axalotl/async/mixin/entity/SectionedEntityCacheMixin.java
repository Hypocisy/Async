package com.axalotl.async.mixin.entity;

import com.axalotl.async.parallelised.fastutil.ConcurrentLongSortedSet;
import com.axalotl.async.parallelised.fastutil.Long2ObjectConcurrentHashMap;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;
import org.spongepowered.asm.mixin.*;

import java.util.concurrent.locks.ReentrantLock;


@Mixin(SectionedEntityCache.class)
public abstract class SectionedEntityCacheMixin<T extends EntityLike> {

    @Unique
    private static final ReentrantLock lock = new ReentrantLock();

    @Shadow
    private final Long2ObjectMap<EntityTrackingSection<T>> trackingSections = new Long2ObjectConcurrentHashMap<>();

    @Shadow
    @Final
    @Mutable
    private LongSortedSet trackedPositions = new ConcurrentLongSortedSet();

    @WrapMethod(method = "forEachInBox")
    private void remove(Box box, LazyIterationConsumer<EntityTrackingSection<T>> consumer, Operation<Void> original) {
        synchronized (lock) {
            original.call(box, consumer);
        }
    }
}