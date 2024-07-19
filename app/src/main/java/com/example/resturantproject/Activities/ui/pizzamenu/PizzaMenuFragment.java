package com.example.resturantproject.Activities.ui.pizzamenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.Activities.fragments.PizzaFragment;
import com.example.resturantproject.R;
import com.example.resturantproject.models.Pizza;

import java.util.ArrayList;
import java.util.List;

public class PizzaMenuFragment extends Fragment /* implements  PizzaFragment.OnFragmentInteractionListener */{

    private static final String ARG_PARAM = "param";

    private String mParam;

    public PizzaMenuFragment() {
        // Required empty public constructor
    }

    public static PizzaMenuFragment newInstance(String param) {
        PizzaMenuFragment fragment = new PizzaMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);

        Spinner sizeSpinner = view.findViewById(R.id.sizeSpinner);
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        Spinner priceSpinner = view.findViewById(R.id.priceSpinner);



        // Adapter for size spinner
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.size_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Adapter for category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Adapter for price spinner
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.price_array, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pizza_menu, container, false);

        Button searchButton = view.findViewById(R.id.searchButton);
        EditText searchEditText = view.findViewById(R.id.searchEditText);

        fillContainer2(rootView, priceSpinner.getSelectedItem().toString(),
                categorySpinner.getSelectedItem().toString(), searchEditText.getText().toString());


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillContainer2(rootView, priceSpinner.getSelectedItem().toString(),
                        categorySpinner.getSelectedItem().toString(), searchEditText.getText().toString());
            }
        });


        return view;
    }

    public void fillContainer(View rootView){
        LinearLayout fragmentContainer = rootView.findViewById(R.id.fragmentContainer);


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded()) { // Check if fragment is actually added
                transaction.remove(fragment);
            }
        }
        transaction.commit();

        for (Pizza pizza : LoginSignUp.pizzaList) {
            String priceString = "$" + pizza.getPrice();
            PizzaFragment pizzaFragment = PizzaFragment.newInstance(
                    pizza.getName(),
                    pizza.getDescription(),
                    priceString,
                    pizza.getImageSrc()
            );
            getChildFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), pizzaFragment)
                    .commit();
        }
    }

    public void fillContainer2(View rootView, String price, String Category, String Query) {
        // Log.d("SYS120", Query);
        LinearLayout fragmentContainer = rootView.findViewById(R.id.fragmentContainer);


        boolean isPriceFilterActive = !price.equals("All");
        boolean isCategoryFilterActive = !Category.equals("All");
        boolean isSearchActive = !Query.isEmpty();

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;
        if (isPriceFilterActive) {
            // Parse the price range (e.g., "10-20" -> minPrice=10, maxPrice=20)
            String[] priceRange = price.split("-");
            minPrice = Double.parseDouble(priceRange[0]);
            maxPrice = Double.parseDouble(priceRange[1]);
        }

        FragmentManager fragmentManager = getChildFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remove existing fragments
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded()) {
                transaction.remove(fragment);
            }
        }
        transaction.commit();

        // Filter the pizza list based on the constraints
        List<Pizza> filteredPizzaList = new ArrayList<>();
        for (Pizza pizza : LoginSignUp.pizzaList) {
            boolean matchesPrice = !isPriceFilterActive || (pizza.getPrice() >= minPrice && pizza.getPrice() <= maxPrice);
            boolean matchesCategory = !isCategoryFilterActive || pizza.getCategory().equalsIgnoreCase(Category);
            boolean matchesSearch = !isSearchActive || pizza.getName().toLowerCase().contains(Query.toLowerCase());
            //Log.d("SYS120", pizza.getName().toLowerCase() + " == ? " + Query.toLowerCase());

            if (matchesPrice  && matchesCategory && matchesSearch) {
                filteredPizzaList.add(pizza);
            }
        }

        // Add the filtered pizzas as fragments
        for (Pizza pizza : filteredPizzaList) {
            String priceString = "$" + pizza.getPrice();
            PizzaFragment pizzaFragment = PizzaFragment.newInstance(
                    pizza.getName(),
                    pizza.getDescription(),
                    priceString,
                    pizza.getImageSrc()
            );
            getChildFragmentManager().beginTransaction()
                    .add(fragmentContainer.getId(), pizzaFragment)
                    .commit();
        }
    }


}
