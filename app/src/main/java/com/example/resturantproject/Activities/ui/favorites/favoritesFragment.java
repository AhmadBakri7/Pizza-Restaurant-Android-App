package com.example.resturantproject.Activities.ui.favorites;

import android.media.metrics.LogSessionId;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.Activities.fragments.PizzaFragment;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.User;
import com.example.resturantproject.models.Pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class favoritesFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";


    private String mParam1;

    private DataBaseHelper dbHelper;

    public favoritesFragment() {
        // Required empty public constructor
    }

    public static favoritesFragment newInstance(String param1) {
        favoritesFragment fragment = new favoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        dbHelper = new DataBaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        fillContainer(rootView);

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



        User user = dbHelper.getUserByEmail(Login.userEmail);
        Log.d("SYS120" , user.toString());

        for(Pizza pizza: LoginSignUp.pizzaList){
            if(user.getFavorites().contains(pizza.getName())){
                Log.d("SYS120", pizza.getName() );
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
}