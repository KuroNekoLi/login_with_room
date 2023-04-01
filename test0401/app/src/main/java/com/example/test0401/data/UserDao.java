package com.example.test0401.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {

    @Insert
    Completable insert(User user);

    @Delete
    Completable delete(User user);

    @Query("SELECT * FROM users")
    Single<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    Single<User> findUser(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    Single<User> getUserByUsername(String username);
}
