package com.example.task1.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.task1.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.task1.LoginActivity; // 导入登录 Activity

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {
    private TextView tvDisplayName;
    private TextView tvDisplaySignature;
    private LinearLayout llNameItem;
    private LinearLayout llSignatureItem;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        tvDisplayName = view.findViewById(R.id.tv_display_name);
        tvDisplaySignature = view.findViewById(R.id.tv_display_sign);
        llNameItem = view.findViewById(R.id.ll_name_item);
        llSignatureItem = view.findViewById(R.id.ll_sign_item);
        btnLogout = view.findViewById(R.id.btn_logout);

        // 加载用户数据
        loadUserData();

        // 设置点击事件
        setupClickListeners();

        return view;
    }

    // 刷新数据
    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        if (getActivity() == null) return; // 检查Activity是否存在
        SharedPreferences sp = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);

        // 如果用户未设置，则显示默认数据
        String username = sp.getString("name", "未设置");
        String signature = sp.getString("signature", "这个人很懒，什么都没留下");

        tvDisplayName.setText(username);
        tvDisplaySignature.setText(signature);
    }

    // 设置点击事件
    private void setupClickListeners() {
        // 点击用户名/签名时显示Toast
        llNameItem.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "点击了用户名", Toast.LENGTH_SHORT).show();
        });

        llSignatureItem.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "点击了签名", Toast.LENGTH_SHORT).show();
        });

        // 退出登录功能
        btnLogout.setOnClickListener(v -> {
            if (getActivity() == null) return;
            Toast.makeText(getActivity(), "已退出登录", Toast.LENGTH_SHORT).show();

            // 跳转LoginActivity，清除Activity栈
            Intent intent = new Intent(getActivity(), LoginActivity.class);

            // 清除所有旧的Activity，确保用户不能通过返回键回到个人中心
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            getActivity().finish();
        });
    }
}