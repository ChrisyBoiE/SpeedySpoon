package nje.gamf.speedyspoon.Repositories;

import nje.gamf.speedyspoon.Models.User;

public interface UserLoginCallback {
    void onLoginSuccess(User user);
    void onLoginFailure(String errorMessage);
    void onUserNotFound();
    void onIncorrectPassword();
}
