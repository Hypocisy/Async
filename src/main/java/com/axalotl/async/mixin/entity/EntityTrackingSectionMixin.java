package com.axalotl.async.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.EntityTrackingStatus;
import org.spongepowered.asm.mixin.*;

import java.util.stream.Stream;

@Mixin(value = EntityTrackingSection.class)
public class EntityTrackingSectionMixin<T extends EntityLike> {

    @WrapMethod(method = "remove")
    public synchronized boolean add(T entity, Operation<Boolean> original) {
        return original.call(entity);
    }

    @WrapMethod(method = "remove")
    public synchronized boolean remove(T entity, Operation<Boolean> original) {
        return original.call(entity);
    }

    @WrapMethod(method = "forEach(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;")
    public synchronized LazyIterationConsumer.NextIteration forEach(Box box, LazyIterationConsumer<T> consumer, Operation<LazyIterationConsumer.NextIteration> original) {
        return original.call(box, consumer);
    }

    @WrapMethod(method = "forEach(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/math/Box;Lnet/minecraft/util/function/LazyIterationConsumer;)Lnet/minecraft/util/function/LazyIterationConsumer$NextIteration;")
    public synchronized  <U extends T> LazyIterationConsumer.NextIteration forEach(TypeFilter<T, U> type, Box box, LazyIterationConsumer<? super U> consumer, Operation<LazyIterationConsumer.NextIteration> original) {
       return original.call(type, box, consumer);
    }

    @WrapMethod(method = "isEmpty")
    public synchronized boolean isEmpty(Operation<Boolean> original) {
        return original.call();
    }

    @Debug
    @WrapMethod(method = "size")
    public synchronized int size(Operation<Integer> original) {
        return original.call();
    }

    @WrapMethod(method = "stream")
    public synchronized Stream<T> stream(Operation<Stream<T>> original) {
        return original.call();
    }

    @WrapMethod(method = "getStatus")
    public synchronized EntityTrackingStatus getStatus(Operation<EntityTrackingStatus> original) {
        return original.call();
    }

    @WrapMethod(method = "swapStatus")
    public synchronized EntityTrackingStatus swapStatus(EntityTrackingStatus status, Operation<EntityTrackingStatus> original) {
        return original.call(status);
    }
}
