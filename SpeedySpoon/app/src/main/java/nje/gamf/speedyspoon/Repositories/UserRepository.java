package nje.gamf.speedyspoon.Repositories;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import nje.gamf.speedyspoon.Models.User;

public class UserRepository {
    private DatabaseReference databaseReference;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://speedyspoon-be378-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference("users");
    }

    // Regisztrációs metódus a Register-login ágból
    public void registerUser(User user, UserRegistrationCallback callback) {
        Query emailQuery = databaseReference.orderByChild("email").equalTo(user.getEmail());
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onUserAlreadyExists();
                } else {
                    String userId = databaseReference.push().getKey();
                    if (userId != null) {
                        databaseReference.child(userId).setValue(user)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    callback.onRegistrationSuccess();
                                } else {
                                    callback.onRegistrationFailure(task.getException() != null ? 
                                        task.getException().getMessage() : "Unknown error");
                                }
                            });
                    } else {
                        callback.onRegistrationFailure("Could not generate user ID");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRegistrationFailure(databaseError.getMessage());
            }
        });
    }

    // Bejelentkezési metódus a Register-login ágból
    public void loginUser(String email, String password, UserLoginCallback callback) {
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getPassword().equals(password)) {
                                callback.onLoginSuccess(user);
                                return;
                            } else {
                                callback.onIncorrectPassword();
                                return;
                            }
                        }
                    }
                    callback.onUserNotFound();
                } else {
                    callback.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onLoginFailure(databaseError.getMessage());
            }
        });
    }

    // Felhasználók lekérdezése a main ágból
    public void fetchUsers(final UserCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                Log.d("UserRepository", "Users fetched: " + users.size());
                callback.onUsersLoaded(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    // Callback interfészek egyesítése
    public interface UserRegistrationCallback {
        void onRegistrationSuccess();
        void onRegistrationFailure(String errorMessage);
        void onUserAlreadyExists();
    }

    public interface UserLoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailure(String errorMessage);
        void onUserNotFound();
        void onIncorrectPassword();
    }

    public interface UserCallback {
        void onUsersLoaded(List<User> users);
        void onError(DatabaseError databaseError);
    }

    public interface SingleUserCallback {
        void onUserLoaded(User user);
        void onError(DatabaseError databaseError);
        void onUserNotFound();
    }
}
