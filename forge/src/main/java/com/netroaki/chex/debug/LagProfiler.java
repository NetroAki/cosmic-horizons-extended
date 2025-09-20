package com.netroaki.chex.debug;

import com.netroaki.chex.CHEX;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Simple sampling profiler for the server thread. Starts a background sampler that captures
 * stack traces at a fixed interval and aggregates top frames. Use for quick hotspot diagnosis.
 */
public final class LagProfiler {

    private static final long DEFAULT_SAMPLE_PERIOD_MS = 20; // 50 Hz

    private static final ScheduledExecutorService SAMPLER_EXEC = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "CHEX-LagProfiler");
        t.setDaemon(true);
        return t;
    });

    private static volatile boolean running = false;
    private static ScheduledFuture<?> future;
    private static final ConcurrentHashMap<String, Integer> topFrameCounts = new ConcurrentHashMap<>();
    private static volatile long samples = 0;

    private LagProfiler() {}

    public static synchronized boolean start(long periodMs) {
        if (running) return false;
        topFrameCounts.clear();
        samples = 0;
        running = true;
        long p = Math.max(1, periodMs);
        future = SAMPLER_EXEC.scheduleAtFixedRate(LagProfiler::sampleOnce, 0, p, TimeUnit.MILLISECONDS);
        CHEX.LOGGER.info("LagProfiler started (period={}ms)", p);
        return true;
    }

    public static synchronized boolean stop(ServerPlayer feedbackTarget) {
        if (!running) return false;
        running = false;
        if (future != null) {
            future.cancel(false);
            future = null;
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(topFrameCounts.entrySet());
        entries.sort(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed());

        int total = (int) samples;
        int limit = Math.min(15, entries.size());
        CHEX.LOGGER.info("LagProfiler stopped. Total samples: {}", total);
        if (feedbackTarget != null) {
            feedbackTarget.sendSystemMessage(Component.literal("§6CHEX LagProfiler Results (" + total + " samples)"));
        }
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> e = entries.get(i);
            int percent = total > 0 ? (int) Math.round(e.getValue() * 100.0 / total) : 0;
            String line = String.format("%3d%% %s", percent, e.getKey());
            CHEX.LOGGER.info("[LagProfiler] {}", line);
            if (feedbackTarget != null) feedbackTarget.sendSystemMessage(Component.literal("§7" + line));
        }
        return true;
    }

    public static boolean isRunning() { return running; }

    private static void sampleOnce() {
        if (!running) return;
        try {
            Optional<Thread> serverThread = Thread.getAllStackTraces().keySet().stream()
                    .filter(t -> {
                        String n = t.getName();
                        return n != null && (n.contains("Server thread") || n.equals("Server") || n.contains("IntegratedServer"));
                    })
                    .findFirst();
            if (serverThread.isEmpty()) return;
            StackTraceElement[] trace = serverThread.get().getStackTrace();
            if (trace.length == 0) return;
            // Use the top non-java frame for aggregation
            String key = firstInterestingFrame(trace);
            if (key == null) return;
            topFrameCounts.merge(key, 1, Integer::sum);
            samples++;
        } catch (Throwable t) {
            CHEX.LOGGER.warn("LagProfiler sampling error: {}", t.toString());
        }
    }

    private static String firstInterestingFrame(StackTraceElement[] trace) {
        for (StackTraceElement e : trace) {
            String cls = e.getClassName();
            if (cls.startsWith("java.") || cls.startsWith("jdk.") || cls.startsWith("sun.")) continue;
            if (cls.startsWith("org.lwjgl")) continue;
            return cls + "." + e.getMethodName() + ":" + e.getLineNumber();
        }
        StackTraceElement e = trace[0];
        return e.getClassName() + "." + e.getMethodName() + ":" + e.getLineNumber();
    }

    public static String status() {
        return running ? "running" : "stopped";
    }

    public static void startForDuration(Duration duration, ServerPlayer feedbackTarget) {
        long ms = Math.max(1000, duration.toMillis());
        if (!start(DEFAULT_SAMPLE_PERIOD_MS)) {
            if (feedbackTarget != null) feedbackTarget.sendSystemMessage(Component.literal("§cLagProfiler already running"));
            return;
        }
        SAMPLER_EXEC.schedule(() -> stop(feedbackTarget), ms, TimeUnit.MILLISECONDS);
    }
}


