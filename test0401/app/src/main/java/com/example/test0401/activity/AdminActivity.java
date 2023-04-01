package com.example.test0401.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test0401.R;
import com.example.test0401.UserAdapter;
import com.example.test0401.data.User;
import com.example.test0401.viewmodel.UserViewModel;

import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvUserList;
    private UserViewModel userViewModel;
    private EditText etSearchUser;
    private Button btnSearchUser, btnAddUser;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvUserList = findViewById(R.id.rvUserList);
        etSearchUser = findViewById(R.id.etSearchUser);
        btnSearchUser = findViewById(R.id.btnSearchUser);
        btnAddUser = findViewById(R.id.btnAddUser);

        rvUserList.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter();
        rvUserList.setAdapter(userAdapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, userAdapter::setUsers);

        btnSearchUser.setOnClickListener(v -> {
            String username = etSearchUser.getText().toString();
            userViewModel.searchUsersByUsername(username).subscribe(new SingleObserver<List<User>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    userViewModel.addDisposable(d);
                }

                @Override
                public void onSuccess(@NonNull List<User> users) {
                    userAdapter.setUsers(users);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Toast.makeText(AdminActivity.this, "Error searching users", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnAddUser.setOnClickListener(v -> {
            // 创建一个对话框来让用户输入用户名和密码
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
            builder.setView(dialogView);

            EditText etUsername = dialogView.findViewById(R.id.etUsername);
            EditText etPassword = dialogView.findViewById(R.id.etPassword);

            builder.setPositiveButton("Add User", (dialog, which) -> {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User(username, password);
                userViewModel.insert(user).observe(AdminActivity.this, rowId -> {
                    if (rowId != null && rowId > 0) {
                        userAdapter.addUser(user);
                    }
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        userAdapter.setOnItemClickListener(user -> {
            userViewModel.delete(user).observe(AdminActivity.this, affectedRows -> {
                if (affectedRows != null && affectedRows > 0) {
                    int position = userAdapter.removeUser(user);
                    userAdapter.notifyItemRemoved(position);
                    Toast.makeText(AdminActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                }
            });
        });

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString();
                if (username.isEmpty()) {
                    userViewModel.getAllUsers().observe(AdminActivity.this, userAdapter::setUsers);
                } else {
                    userViewModel.searchUsersByUsername(username).subscribe(new SingleObserver<List<User>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            userViewModel.addDisposable(d);
                        }

                        @Override
                        public void onSuccess(@NonNull List<User> users) {
                            userAdapter.setUsers(users);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(AdminActivity.this, "Error searching users", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
