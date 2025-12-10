package com.example.task1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.task1.R;
import com.example.task1.monitor.AnrMonitor;
import com.example.task1.monitor.FluencyMonitor;

public class PerformanceFragment extends Fragment {

    private TextView tvFps;
    private TextView tvAnrLog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        tvFps = view.findViewById(R.id.tv_fps);
        tvAnrLog = view.findViewById(R.id.tv_anr_log);
        Button btnSimulateAnr = view.findViewById(R.id.btn_simulate_anr);

        // 开启流畅度监控
        FluencyMonitor.getInstance().setListener(fps -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                        tvFps.setText(String.format("当前流畅度: %.1f FPS", fps))
                );
            }
        });
        FluencyMonitor.getInstance().start();

        // 开启ANR监控
        AnrMonitor.getInstance().startMonitor(stackTraceInfo -> {
            // 子线程回调，更新UI需切回主线程
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    tvAnrLog.setText("⚠警告：检测到 ANR 卡顿！\n\n堆栈信息：\n" + stackTraceInfo);
                    Toast.makeText(getContext(), "发生 ANR！请查看日志", Toast.LENGTH_LONG).show();
                });
            }
        });

        // 模拟卡顿按钮
        btnSimulateAnr.setOnClickListener(v -> {
            Toast.makeText(getContext(), "开始模拟卡顿，请等待...", Toast.LENGTH_SHORT).show();
            // 模拟真实的主线程卡顿场景：执行耗时计算
            simulateRealAnr();
        });
        return view;
    }

    private void simulateRealAnr() {
        // 在主线程执行耗时操作
        new Thread(() -> {
            // 让ANR监控先开始检测
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 在主线程执行耗时计算
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // 模拟耗时计算（阻塞主线程）
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 10001) {
                        // 执行计算，模拟卡顿
                        double dummy = Math.sqrt(Math.random() * 1000);
                    }
                    Toast.makeText(getContext(), "模拟卡顿结束", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}