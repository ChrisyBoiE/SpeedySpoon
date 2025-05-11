package nje.gamf.speedyspoon.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nje.gamf.speedyspoon.Models.Detail;
import nje.gamf.speedyspoon.Models.Order;
import nje.gamf.speedyspoon.Models.OrderDetail;
import nje.gamf.speedyspoon.Models.Status;

public class OrderRepository {
    private DatabaseReference orderRef;
    private DatabaseReference orderStatusRef;
    private DatabaseReference orderDetailRef;

    public OrderRepository(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        orderRef = database.getReference("orders");
        orderStatusRef = database.getReference("orderStatus");
        orderDetailRef = database.getReference("orderDetails");
    }

    public void fetchOrders(final OrderCallback callback) {
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orders.add(order);
                    }
                }
                Log.d("testing", "Orders fetched: " + orders.size());
                callback.onOrdersLoaded(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void fetchOrderByUserId(final String userID, final OrderCallback callback) {
        orderRef.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orders.add(order);
                    }
                }
                Log.d("testing", "Orders fetched for user: " + orders.size());
                callback.onOrdersLoaded(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void fetchOrderDetailByOrderId(final String orderID, final OrderCallback callback) {
        orderDetailRef.orderByChild("orderID").equalTo(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Detail> orderDetails = new ArrayList<>();
                for(DataSnapshot detailSnapshot : dataSnapshot.getChildren()) {
                    Detail detail = detailSnapshot.getValue(Detail.class);
                    if (detail != null) {
                        orderDetails.add(detail);
                    }
                }
                Log.d("testing", "Order details fetched: " + orderDetails.size());
                callback.onOrderDetailsLoaded(orderDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void fetchOrderStatus(final OrderCallback callback) {
        orderStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Status> ordersStatus = new ArrayList<>();
                for (DataSnapshot orderStatusSnapshot : dataSnapshot.getChildren()) {
                    Status status = orderStatusSnapshot.getValue(Status.class);
                    if (status != null) {
                        ordersStatus.add(status);
                    }
                }
                Log.d("testing", "Order statutes fetched: " + ordersStatus.size());
                callback.onOrderStatusLoaded(ordersStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}
