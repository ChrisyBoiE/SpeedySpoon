package nje.gamf.speedyspoon.Repositories;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import nje.gamf.speedyspoon.Models.User;

public interface UserCallback {
    void onUsersLoaded(List<User> users);
    void onError(DatabaseError error);
}
