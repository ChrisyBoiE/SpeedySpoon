package nje.gamf.speedyspoon.Repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import nje.gamf.speedyspoon.Models.User;

public class UserRepository {

    private DatabaseReference databaseReference;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://speedyspoon-be378-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference("users");
    }

    public void registerUser(User user, UserRegistrationCallback callback) {
        // Check if user with the same email already exists
        Query emailQuery = databaseReference.orderByChild("email").equalTo(user.getEmail());
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onUserAlreadyExists();
                } else {
                    // User does not exist, proceed with registration
                    String userId = databaseReference.push().getKey();
                    if (userId != null) {
                        databaseReference.child(userId).setValue(user)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onRegistrationSuccess();
                                    } else {
                                        callback.onRegistrationFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
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

    public void loginUser(String email, String password, UserLoginCallback callback) {
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getPassword().equals(password)) { // In a real app, use hashed passwords and verification
                                callback.onLoginSuccess(user);
                                return;
                            } else {
                                callback.onIncorrectPassword();
                                return;
                            }
                        }
                    }
                    // Should not happen if data is consistent, but as a fallback:
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
}
