package com.example.test0401.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test0401.R;
import com.example.test0401.viewmodel.UserViewModel;


public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnRegister;
    private UserViewModel userViewModel;
    private static final int TOUCH_MARGIN = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                userViewModel.register(username, password);
            }
        });
    }

}
