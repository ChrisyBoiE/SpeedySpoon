package nje.gamf.speedyspoon.Repositories;

public interface UserRegistrationCallback {
    void onRegistrationSuccess();
    void onRegistrationFailure(String errorMessage);
    void onUserAlreadyExists();
}
