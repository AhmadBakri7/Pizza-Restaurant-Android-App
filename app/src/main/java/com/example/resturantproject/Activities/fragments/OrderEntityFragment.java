package com.example.resturantproject.Activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.R;
import com.example.resturantproject.models.Order;
import com.example.resturantproject.models.Pizza;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class OrderEntityFragment extends Fragment {

    private static final String ARG_ORDER = "order";

    private Order order;

    private boolean detailsVisible = false; // Flag to track visibility state

    public OrderEntityFragment() {
        // Required empty public constructor
    }

    public static OrderEntityFragment newInstance(Order order) {
        OrderEntityFragment fragment = new OrderEntityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_entity, container, false);

        // Find views by ID
        TextView tvPizzaName = view.findViewById(R.id.tvPizzaName);
        TextView tvPizzaSize = view.findViewById(R.id.tvPizzaSize);
        TextView tvPizzaCount = view.findViewById(R.id.tvPizzaCount);
        TextView tvOrderDate = view.findViewById(R.id.tvOrderDate);
        TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        TextView tvPizzaPrice = view.findViewById(R.id.tvPizzaPrice);
        ImageView ivPizzaImage = view.findViewById(R.id.ivPizzaImage);

        tvPizzaSize.setVisibility(View.GONE);
        tvPizzaCount.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        tvPizzaPrice.setVisibility(View.GONE);

        // Set values to views
        tvPizzaName.setText(order.getPizzaName());
        tvPizzaSize.setText("Size: " + order.getPizzaSize());
        tvPizzaCount.setText("Count: " + order.getPizzaCount());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tvOrderDate.setText("Order Date: " + dateFormat.format(order.getOrderDate()));
        tvUserEmail.setText("Email: " + order.getUserEmail());
        ivPizzaImage.setImageResource(R.drawable.margarita);
        tvPizzaPrice.setText("Total Price : " + "$"+ order.getCost());


        // Set an example image for the pizza (replace with actual image loading logic)
        for(Pizza p : LoginSignUp.pizzaList){
            if(p.getName().equalsIgnoreCase(order.getPizzaName())){
                ivPizzaImage.setImageResource(p.getImageSrc());

            }
        }

        View cardView = view.findViewById(R.id.orderCardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDetailsVisibility(view);
            }
        });

        return view;
    }

    private void toggleDetailsVisibility(View view) {
        if (detailsVisible) {
            // Hide details
            TextView tvPizzaSize = view.findViewById(R.id.tvPizzaSize);
            TextView tvPizzaCount = view.findViewById(R.id.tvPizzaCount);
            TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
            TextView tvPizzaPrice = view.findViewById(R.id.tvPizzaPrice);

            tvPizzaSize.setVisibility(View.GONE);
            tvPizzaCount.setVisibility(View.GONE);
            tvUserEmail.setVisibility(View.GONE);
            tvPizzaPrice.setVisibility(View.GONE);
        } else {
            // Show details
            TextView tvPizzaSize = view.findViewById(R.id.tvPizzaSize);
            TextView tvPizzaCount = view.findViewById(R.id.tvPizzaCount);
            TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
            TextView tvPizzaPrice = view.findViewById(R.id.tvPizzaPrice);

            tvPizzaSize.setVisibility(View.VISIBLE);
            tvPizzaCount.setVisibility(View.VISIBLE);
            tvUserEmail.setVisibility(View.VISIBLE);
            tvPizzaPrice.setVisibility(View.VISIBLE);
        }
        detailsVisible = !detailsVisible;
    }
}
