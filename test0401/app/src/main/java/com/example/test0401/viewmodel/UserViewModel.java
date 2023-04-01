package com.example.test0401.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.test0401.activity.MainActivity;
import com.example.test0401.data.User;
import com.example.test0401.repository.UserRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUsers();
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public Single<User> findUser(String username, String password) {
        return userRepository.findUser(username, password);
    }

    public Single<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public void register(String username, String password) {
        Disposable disposable = getUserByUsername(username)
                .subscribe(
                        user -> {
                            // 用户名已存在
                        },
                        throwable -> {
                            // 用户名可用，创建新用户
                            insert(new User(username, password));
                        });
        compositeDisposable.add(disposable);
    }

    public void login(String username, String password, Context context) {
        Disposable disposable = findUser(username, password)
                .subscribe(
                        user -> {
                            // 用户验证成功，跳转到购物网站主界面
                            Intent mainIntent = new Intent(context, MainActivity.class);
                            context.startActivity(mainIntent);
                        },
                        throwable -> {
                            // 用户验证失败，显示错误消息
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                        });
        compositeDisposable.add(disposable);
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
