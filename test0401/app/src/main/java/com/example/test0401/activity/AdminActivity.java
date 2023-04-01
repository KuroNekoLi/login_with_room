package com.example.test0401.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUsers(users);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etSearchUser.getText().toString();
                userViewModel.getUserByUsername(username).subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 添加订阅到CompositeDisposable，以便在ViewModel清理时正确取消订阅
                        userViewModel.addDisposable(d);
                    }

                    @Override
                    public void onSuccess(@NonNull User user) {
                        // 显示查询到的用户信息，可以用一个Dialog展示
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(AdminActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = String.valueOf((int)(Math.random()*49+1));
                String b = String.valueOf((int)(Math.random()*49+1));
                User user = new User(a,b);
                userViewModel.insert(user).observe(AdminActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(Long rowId) {
                        if (rowId != null && rowId > 0) {
                            // 数据库插入成功
                            userAdapter.addUser(user); // 在 UserAdapter 中添加 addUser 方法
                        }
                    }
                });
            }
        });

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(User user) {
                userViewModel.delete(user).observe(AdminActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer affectedRows) {
                        if (affectedRows != null && affectedRows > 0) {
                            // 数据库删除成功
                            int position = userAdapter.removeUser(user); // 在 UserAdapter 中添加 removeUser 方法
                            userAdapter.notifyItemRemoved(position);
                            Toast.makeText(AdminActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
