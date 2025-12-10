package com.example.task1.monitor;

import android.util.Log;
import android.view.Choreographer;

public class FluencyMonitor {
    private static FluencyMonitor instance;
    private Choreographer.FrameCallback frameCallback;
    private long lastFrameTimeNanos = 0;
    private boolean isMonitoring = false;

    public interface FluencyListener {
        void onFpsUpdate(double fps);
    }

    private FluencyListener listener;

    public static FluencyMonitor getInstance() {
        if (instance == null) instance = new FluencyMonitor();
        return instance;
    }

    public void setListener(FluencyListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (isMonitoring) return;
        isMonitoring = true;
        frameCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (lastFrameTimeNanos != 0) {
                    long diff = frameTimeNanos - lastFrameTimeNanos;
                    // 计算FPS: 1秒(10^9纳秒) / 帧间隔
                    double fps = 1000000000.0 / diff;
                    if (listener != null) {
                        listener.onFpsUpdate(fps);
                    }
                }
                lastFrameTimeNanos = frameTimeNanos;
                if (isMonitoring) {
                    Choreographer.getInstance().postFrameCallback(this);
                }
            }
        };
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    public void stop() {
        isMonitoring = false;
        lastFrameTimeNanos = 0;
        Choreographer.getInstance().removeFrameCallback(frameCallback);
    }
}