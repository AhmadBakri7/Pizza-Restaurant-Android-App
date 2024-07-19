package com.example.resturantproject.Activities.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.OrderDataBaseHelper;
import com.example.resturantproject.models.Order;
import com.example.resturantproject.models.Pizza;

import java.util.Date;

public class OrderFragment extends DialogFragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private String pizzaName;
    private OrderDataBaseHelper dbHelper;

    public static OrderFragment newInstance(String pizzaName) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, pizzaName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        TextView pizzaNameTextView = rootView.findViewById(R.id.pizzaNameTextView);
        Spinner sizeSpinner = rootView.findViewById(R.id.spinnerSize);
        EditText quantityEditText = rootView.findViewById(R.id.quantityEditText);
        Button submitButton = rootView.findViewById(R.id.submitButton);

        // Set the pizza name in the TextView
        pizzaNameTextView.setText(pizzaName);
        dbHelper = new OrderDataBaseHelper(getContext());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSize = sizeSpinner.getSelectedItem().toString();
                String quantityText = quantityEditText.getText().toString();

                if (quantityText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityText);

                for(Pizza p: LoginSignUp.pizzaList){
                    if(p.getName().equalsIgnoreCase(pizzaName)){
                        dbHelper.addOrder(new Order(Login.userEmail, pizzaName, selectedSize,new Date() ,quantity, p.getPrice()*quantity));
                    }
                }
                dismiss();
            }
        });

        return rootView;
    }

}
