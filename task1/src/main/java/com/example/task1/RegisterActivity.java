package com.example.task1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task1.utils.UserDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化数据库
        dbHelper = new UserDbHelper(this);

        // 初始化视图
        etUsername = findViewById(R.id.et_reg_username);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    // 注册逻辑
    private void attemptRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 检查输入是否为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查两次密码是否一致
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查账号是否已存在
        if (dbHelper.checkUserExists(username)) {
            Toast.makeText(this, "该账号已存在，请更换用户名或直接登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将新账号密码写入数据库
        long result = registerUser(username, password);

        if (result > 0) {
            Toast.makeText(this, "注册成功！请登录", Toast.LENGTH_LONG).show();
            // 注册成功，返回登录页面
            finish();
        } else {
            Toast.makeText(this, "注册失败，请稍后再试！", Toast.LENGTH_SHORT).show();
        }
    }

    // 数据写入数据库
    private long registerUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserDbHelper.COLUMN_USERNAME, username);
        values.put(UserDbHelper.COLUMN_PASSWORD, password);

        // 初始化签名
        values.put(UserDbHelper.COLUMN_SIGNATURE, "这个人很懒，什么都没留下");

        long newRowId = db.insert(UserDbHelper.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
}