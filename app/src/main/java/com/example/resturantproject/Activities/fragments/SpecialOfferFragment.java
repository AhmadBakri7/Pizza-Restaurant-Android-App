package com.example.resturantproject.Activities.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.OrderDataBaseHelper;
import com.example.resturantproject.models.Order;
import com.example.resturantproject.models.SpecialOffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpecialOfferFragment extends Fragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_PIZZA_SIZE = "pizza_size";
    private static final String ARG_OFFER_START_DATE = "offer_start_date";
    private static final String ARG_OFFER_END_DATE = "offer_end_date";
    private static final String ARG_TOTAL_PRICE = "total_price";
    private static final String ARG_PIZZA_COUNT = "pizza_count";

    private String pizzaName;
    private String pizzaSize;
    private Date offerStartDate;
    private Date offerEndDate;
    private double totalPrice;
    private int pizzaCount;

    public static SpecialOfferFragment newInstance(SpecialOffer specialOffer) {
        SpecialOfferFragment fragment = new SpecialOfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, specialOffer.getPizzaName());
        args.putString(ARG_PIZZA_SIZE, specialOffer.getPizzaSize());
        args.putLong(ARG_OFFER_START_DATE, specialOffer.getOfferStartDate().getTime());
        args.putLong(ARG_OFFER_END_DATE, specialOffer.getOfferEndDate().getTime());
        args.putDouble(ARG_TOTAL_PRICE, specialOffer.getTotalPrice());
        args.putInt(ARG_PIZZA_COUNT, specialOffer.getPizzaCount());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            pizzaSize = getArguments().getString(ARG_PIZZA_SIZE);
            offerStartDate = new Date(getArguments().getLong(ARG_OFFER_START_DATE));
            offerEndDate = new Date(getArguments().getLong(ARG_OFFER_END_DATE));
            totalPrice = getArguments().getDouble(ARG_TOTAL_PRICE);
            pizzaCount = getArguments().getInt(ARG_PIZZA_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offer, container, false);

        TextView pizzaNameTextView = view.findViewById(R.id.textView_pizza_name);
        TextView pizzaSizeTextView = view.findViewById(R.id.textView_pizza_size);
        TextView offerDatesTextView = view.findViewById(R.id.textView_offer_dates);
        TextView totalPriceTextView = view.findViewById(R.id.textView_total_price);
        Button orderButton = view.findViewById(R.id.button_order);
        ImageView pizzaImageView = view.findViewById(R.id.imageView_pizza);

        // Set values from SpecialOffer model
        pizzaNameTextView.setText(pizzaName);
        pizzaSizeTextView.setText(pizzaSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String offerDates = dateFormat.format(offerStartDate) + " - " + dateFormat.format(offerEndDate);
        offerDatesTextView.setText(offerDates);
        totalPriceTextView.setText(String.valueOf(totalPrice));

        // Set pizza image (Replace 'R.drawable.pizza_placeholder' with the actual image resource)
        pizzaImageView.setImageResource(R.drawable.margarita);
        OrderDataBaseHelper orderDataBaseHelper = new OrderDataBaseHelper(getContext());

        // Handle order button click
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDataBaseHelper.addOrder(new Order(Login.userEmail, pizzaName,
                        pizzaSize, new Date(), pizzaCount, totalPrice));
                Toast.makeText(getContext(), "Order Successfully Made!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}