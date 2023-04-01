package com.example.test0401.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.test0401.activity.MainActivity;
import com.example.test0401.data.User;
import com.example.test0401.repository.UserRepository;

import java.util.List;

import io.reactivex.rxjava3.core.CompletableObserver;
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

    public LiveData<Long> insert(User user) {
        MutableLiveData<Long> rowId = new MutableLiveData<>();
        userRepository.insert(user)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        rowId.postValue(1L); // 数据库插入成功，通知观察者
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        rowId.postValue(-1L); // 数据库插入失败，通知观察者
                    }
                });
        return rowId;
    }

    public LiveData<Integer> delete(User user) {
        MutableLiveData<Integer> affectedRows = new MutableLiveData<>();
        userRepository.delete(user)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        affectedRows.postValue(1); // 数据库删除成功，通知观察者
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        affectedRows.postValue(-1); // 数据库删除失败，通知观察者
                    }
                });
        return affectedRows;
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
