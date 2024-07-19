package com.example.resturantproject.Activities.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.Activities.fragments.OrderEntityFragment;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.OrderDataBaseHelper;
import com.example.resturantproject.models.Order;

import java.util.Collections;
import java.util.List;

public class OrdersFragment extends Fragment {

    private OrderDataBaseHelper orderDataBaseHelper;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize database helper
        orderDataBaseHelper = new OrderDataBaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Find the container LinearLayout
        LinearLayout fragmentContainer = view.findViewById(R.id.fragmentContainer);

        // Clear existing fragments
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded()) { // Check if fragment is actually added
                transaction.remove(fragment);
            }
        }
        transaction.commit();

        // Get all orders from the database
        List<Order> orders = orderDataBaseHelper.getOrdersByUserEmail(Login.userEmail);

        Collections.reverse(orders);


        // Dynamically add OrderEntityFragment for each order
        for (Order order : orders) {
            OrderEntityFragment orderEntityFragment = OrderEntityFragment.newInstance(order);

            getChildFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), orderEntityFragment)
                    .commit();
        }

        return view;
    }
}
