package com.example.task1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.task1.utils.UserDbHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDbHelper(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        ImageView ivWechat = findViewById(R.id.iv_wechat_login);
        ImageView ivApple = findViewById(R.id.iv_apple_login);
        TextView tvRegister = findViewById(R.id.tv_register);

        // 注册点击事件
        tvRegister.setOnClickListener(v -> {
            // 跳转到RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 登录按钮逻辑
        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();

            if (dbHelper.login(user, pwd)) {
                // 登录成功
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                // 个人中心SP存取
                saveUserInfoToSP(user, "这是我的个性签名");

                // 跳转至主页面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
        ivWechat.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "触发微信登录", Toast.LENGTH_SHORT).show());
        ivApple.setOnClickListener(v -> Toast.makeText(LoginActivity.this, "触发Apple登录", Toast.LENGTH_SHORT).show());
    }

    private void saveUserInfoToSP(String name, String sign) {
        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.putString("sign", sign);
        editor.apply();
    }
}