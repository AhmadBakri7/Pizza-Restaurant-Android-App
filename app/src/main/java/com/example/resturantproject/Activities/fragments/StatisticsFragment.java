package com.example.resturantproject.Activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.OrderDataBaseHelper;
import com.example.resturantproject.models.Pizza;


import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private Spinner spinnerPizzaTypes;
    private TextView tvOrderCount;
    private TextView tvTotalIncome;
    private TextView tvTotalIncomeAll;
    private Button buttonCalculate;
    private OrderDataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        spinnerPizzaTypes = view.findViewById(R.id.spinner_pizza_types);
        tvOrderCount = view.findViewById(R.id.tv_order_count);
        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvTotalIncomeAll = view.findViewById(R.id.tv_total_income_all);
        buttonCalculate = view.findViewById(R.id.button_calculate);

        dbHelper = new OrderDataBaseHelper(getContext());

        populatePizzaTypesSpinner();

        calculateTotalIncomeForAllTypes();

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPizza = (String) spinnerPizzaTypes.getSelectedItem();
                updateOrderCountAndIncome(selectedPizza);
            }
        });

        return view;
    }

    private void populatePizzaTypesSpinner() {
        List<String> pizzaNames = new ArrayList<>();
        for (Pizza pizza : LoginSignUp.pizzaList) {
            pizzaNames.add(pizza.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pizzaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaTypes.setAdapter(adapter);
    }

    private void updateOrderCountAndIncome(String pizzaName) {
        int orderCount = dbHelper.getOrderCountForPizzaName(pizzaName);
        double totalIncome = dbHelper.getTotalPriceForPizzaName(pizzaName);

        tvOrderCount.setText(String.valueOf(orderCount));
        tvTotalIncome.setText(String.format("$%.2f", totalIncome));
    }

    private void calculateTotalIncomeForAllTypes() {
        double totalIncomeAll = dbHelper.getTotalPriceForAllOrders();
        tvTotalIncomeAll.setText(String.format("$%.2f", totalIncomeAll));
    }
}