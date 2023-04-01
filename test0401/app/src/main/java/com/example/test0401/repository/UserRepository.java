package com.example.test0401.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.test0401.data.AppDatabase;
import com.example.test0401.data.User;
import com.example.test0401.data.UserDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = LiveDataReactiveStreams.fromPublisher(
                userDao.getAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable());
    }

    public void insert(User user) {
        userDao.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void delete(User user) {
        userDao.delete(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public Single<User> findUser(String username, String password) {
        return userDao.findUser(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
