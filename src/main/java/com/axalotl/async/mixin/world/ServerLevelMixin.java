package com.axalotl.async.mixin.world;

import com.axalotl.async.ParallelProcessor;
import com.axalotl.async.parallelised.ConcurrentCollections;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(value = ServerLevel.class, priority = 1500)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
	@Unique
	ConcurrentLinkedQueue<BlockEventData> async$syncedBlockEventQueue;
	@Shadow
	@Final
	@Mutable
	Set<Mob> navigatingMobs;

	protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
		super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		navigatingMobs = ConcurrentCollections.newHashSet();
		async$syncedBlockEventQueue = new ConcurrentLinkedQueue<>();
	}

	@Redirect(method = {"lambda$tick$2"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V"))
	private void overwriteEntityTicking(ServerLevel instance, Consumer<Entity> consumer, Entity entity) {
		ParallelProcessor.callEntityTick(consumer, entity);
	}

	@Redirect(method = "blockEvent", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;add(Ljava/lang/Object;)Z"))
	private boolean overwriteQueueAdd(ObjectLinkedOpenHashSet<BlockEventData> objectLinkedOpenHashSet, Object object) {
		return async$syncedBlockEventQueue.add((BlockEventData) object);
	}

	@Redirect(method = "clearBlockEvents", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;removeIf(Ljava/util/function/Predicate;)Z"))
	private boolean overwriteQueueRemoveIf(ObjectLinkedOpenHashSet<BlockEventData> objectLinkedOpenHashSet, Predicate<BlockEventData> filter) {
		return async$syncedBlockEventQueue.removeIf(filter);
	}

	@Redirect(method = "runBlockEvents", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;isEmpty()Z"))
	private boolean overwriteEmptyCheck(ObjectLinkedOpenHashSet<BlockEventData> objectLinkedOpenHashSet) {
		return async$syncedBlockEventQueue.isEmpty();
	}

	@Redirect(method = "runBlockEvents", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;removeFirst()Ljava/lang/Object;"))
	private Object overwriteQueueRemoveFirst(ObjectLinkedOpenHashSet<BlockEventData> objectLinkedOpenHashSet) {
		return async$syncedBlockEventQueue.poll();
	}

	@Redirect(method = "runBlockEvents", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;addAll(Ljava/util/Collection;)Z"))
	private boolean overwriteQueueAddAll(ObjectLinkedOpenHashSet<BlockEventData> instance, Collection<? extends BlockEventData> c) {
		return async$syncedBlockEventQueue.addAll(c);
	}

	@Redirect(method = "sendBlockUpdated", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerLevel;isUpdatingNavigations:Z", opcode = Opcodes.PUTFIELD))
	private void skipSendBlockUpdatedCheck(ServerLevel instance, boolean value) {
	}
}