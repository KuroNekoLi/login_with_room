package com.example.test0401.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.example.test0401.data.AppDatabase;
import com.example.test0401.data.User;
import com.example.test0401.data.UserDao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private FirebaseAuth mAuth;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = LiveDataReactiveStreams.fromPublisher(
                userDao.getAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable());
        mAuth = FirebaseAuth.getInstance();
    }

    public Completable insert(User user) {
        return userDao.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(User user) {
        return userDao.delete(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> loginUser( String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }
}
