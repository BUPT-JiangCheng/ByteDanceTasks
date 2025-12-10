package com.example.task1;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal);

        TextView tvName = findViewById(R.id.tv_display_name);
        TextView tvSign = findViewById(R.id.tv_display_sign);
        LinearLayout llNameItem = findViewById(R.id.ll_name_item);
        LinearLayout llSignItem = findViewById(R.id.ll_sign_item);
        Button btnLogout = findViewById(R.id.btn_logout);

        // 用户名称、签名使用SharePreference存取
        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String name = sp.getString("name", "未登录");
        String sign = sp.getString("sign", "无签名");

        tvName.setText(name);
        tvSign.setText(sign);

        // 每个信息条目点击后都能弹Toast
        llNameItem.setOnClickListener(v -> Toast.makeText(PersonalActivity.this, "点击了用户名称", Toast.LENGTH_SHORT).show());
        llSignItem.setOnClickListener(v -> Toast.makeText(PersonalActivity.this, "点击了签名", Toast.LENGTH_SHORT).show());

        // 退出登录逻辑
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(PersonalActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}