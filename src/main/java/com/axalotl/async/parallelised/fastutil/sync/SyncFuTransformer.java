package com.axalotl.async.parallelised.fastutil.sync;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyncFuTransformer implements PreLaunchEntrypoint {
    private static final Logger syncFuTransformerLogger = LogManager.getLogger();

    @Override
    public void onPreLaunch() {
        syncFuTransformerLogger.info("SyncFuTransformer preload...");
        try {
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$ValueIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$KeySet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$KeyIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$MapEntrySet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$EntryIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$MapIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$MapEntry");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap$FastEntryIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap$MapIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap$FastEntryIterator");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.longs.Long2ObjectMap$FastEntrySet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap");
            Knot.getLauncher().loadIntoTarget("it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
