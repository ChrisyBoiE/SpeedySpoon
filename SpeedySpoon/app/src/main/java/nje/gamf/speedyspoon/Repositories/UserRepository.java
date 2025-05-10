package nje.gamf.speedyspoon.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Models.User;

public class UserRepository {
    private DatabaseReference usersRef;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void fetchUsers(final UserCallback callback) {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : dataSnapshot.getChildren()) {
                    User user = menuItemSnapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                Log.d("testing", "Users fetched: " + users.size());
                callback.onUsersLoaded(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    // Fetch one user callback
    public interface SingleUserCallback {
        void onUserLoaded(User user);
        void onError(DatabaseError databaseError);
        void onUserNotFound();
    }

    public void fetchUserByEmail(String email, final SingleUserCallback callback) {
        Query query = usersRef.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User foundUser = null;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    foundUser = userSnapshot.getValue(User.class);
                    if (foundUser != null) {
                        break;
                    }
                }

                if (foundUser != null) {
                    callback.onUserLoaded(foundUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}
