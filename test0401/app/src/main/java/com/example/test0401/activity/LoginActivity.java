package com.example.test0401.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test0401.R;
import com.example.test0401.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private UserViewModel userViewModel;
    private static final int TOUCH_MARGIN = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                userViewModel.login(username, password, LoginActivity.this);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float x = event.getX();
        float y = event.getY();

        boolean topLeft = x <= TOUCH_MARGIN && y <= TOUCH_MARGIN;
        boolean topRight = x >= screenWidth - TOUCH_MARGIN && y <= TOUCH_MARGIN;
        boolean bottomLeft = x <= TOUCH_MARGIN && y >= screenHeight - TOUCH_MARGIN;
        boolean bottomRight = x >= screenWidth - TOUCH_MARGIN && y >= screenHeight - TOUCH_MARGIN;

        if (event.getAction() == MotionEvent.ACTION_DOWN && (topLeft || topRight || bottomLeft || bottomRight)) {
            // 进入管理员模式，例如打开管理员页面
            Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(adminIntent);
            return true;
        }

        return super.onTouchEvent(event);
    }
}
