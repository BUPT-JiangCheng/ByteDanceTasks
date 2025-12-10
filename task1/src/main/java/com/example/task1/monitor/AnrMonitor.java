package com.example.task1.monitor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class AnrMonitor extends Thread {

    private static AnrMonitor instance;
    private AnrListener listener;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final int CHECK_INTERVAL = 5000; // 5秒检测一次

    // 用于判断主线程是否执行了任务
    private volatile int tick = 0;
    private volatile boolean isMonitoring = false;

    // 回调接口
    public interface AnrListener {
        void onAnrHappened(String stackTraceInfo);
    }

    private AnrMonitor() {
        setName("AnrWatchdogThread");
    }

    public static AnrMonitor getInstance() {
        if (instance == null) {
            instance = new AnrMonitor();
        }
        return instance;
    }


    // 初始化并开始监控
    public void startMonitor(AnrListener listener) {
        this.listener = listener;
        if (!isMonitoring) {
            isMonitoring = true;
            this.start(); // 启动ANR线程
        }
    }

    @Override
    public void run() {
        while (isMonitoring) {
            // 记录当前的tick值
            int lastTick = tick;

            // 向主线程发送任务，tick++
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    tick = (tick + 1) % Integer.MAX_VALUE;
                }
            });

            // 休眠5s
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 检查tick值，若没有变化，则认为主线程卡顿了
            if (tick == lastTick) {
                // 生成堆栈信息，帮助定位问题
                String stackTrace = getMainThreadStackTrace();
                // 回调通知(子线程)
                if (listener != null) {
                    listener.onAnrHappened(stackTrace);
                }
            }
        }
    }

    // 获取主线程当前的堆栈信息，用于分析卡顿原因
    private String getMainThreadStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}