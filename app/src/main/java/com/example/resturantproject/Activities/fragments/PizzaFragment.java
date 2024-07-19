package com.example.resturantproject.Activities.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.User;

import java.sql.SQLException;

public class PizzaFragment extends Fragment {
    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_PIZZA_DESCRIPTION = "pizza_description";
    private static final String ARG_PIZZA_PRICE = "pizza_price";
    private static final String ARG_PIZZA_IMAGE_RES_ID = "pizza_image_res_id";

    private String pizzaName;
    private String pizzaDescription;
    private String pizzaPrice;
    private int pizzaImageResId;
    private DataBaseHelper dbHelper;

    private boolean detailsVisible = false; // Flag to track visibility state

    public PizzaFragment() {
        // Required empty public constructor
    }

    public static PizzaFragment newInstance(String pizzaName, String pizzaDescription, String pizzaPrice, int pizzaImageResId) {
        PizzaFragment fragment = new PizzaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, pizzaName);
        args.putString(ARG_PIZZA_DESCRIPTION, pizzaDescription);
        args.putString(ARG_PIZZA_PRICE, pizzaPrice);
        args.putInt(ARG_PIZZA_IMAGE_RES_ID, pizzaImageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            pizzaDescription = getArguments().getString(ARG_PIZZA_DESCRIPTION);
            pizzaPrice = getArguments().getString(ARG_PIZZA_PRICE);
            pizzaImageResId = getArguments().getInt(ARG_PIZZA_IMAGE_RES_ID);
        }
        dbHelper = new DataBaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza, container, false);

        TextView nameTextView = view.findViewById(R.id.PizzaName);
        TextView descriptionTextView = view.findViewById(R.id.PizzaDescription);
        TextView priceTextView = view.findViewById(R.id.PizzaPrice);
        ImageView imageView = view.findViewById(R.id.imageView2);
        Button favoriteButton = view.findViewById(R.id.favoriteButton);
        Button orderButton = view.findViewById(R.id.orderButton);
        ImageView heartImageView = view.findViewById(R.id.heartImageView);

        // Set initial values
        nameTextView.setText(pizzaName);
        descriptionTextView.setText(pizzaDescription);
        priceTextView.setText(pizzaPrice);
        imageView.setImageResource(pizzaImageResId);

        descriptionTextView.setVisibility(View.GONE);
        priceTextView.setVisibility(View.GONE);
        favoriteButton.setVisibility(View.GONE);
        orderButton.setVisibility(View.GONE);

        // Update favorite button text based on whether the pizza is a favorite
        User user = dbHelper.getUserByEmail(Login.userEmail);
        if (user.getFavorites().contains(pizzaName)) {
            favoriteButton.setText(R.string.unfavorite);
        } else {
            favoriteButton.setText(R.string.favorite);
        }

        // Set click listeners for the buttons
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User user = dbHelper.getUserByEmail(Login.userEmail);
                    if (user.getFavorites().contains(pizzaName)) {
                        // Remove from favorites
                        user.getFavorites().remove(pizzaName);
                        dbHelper.removeUserString(Login.userEmail, pizzaName);
                        favoriteButton.setText(R.string.favorite);
                    } else {
                        dbHelper.addUserString(Login.userEmail, pizzaName);
                        favoriteButton.setText(R.string.unfavorite);
                    }
                    animateHeart(heartImageView); // Show heart animation
                } catch (SQLException e) {
                    Toast.makeText(getContext(), "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment dialog = OrderFragment.newInstance(pizzaName);
                dialog.show(getParentFragmentManager(), "OrderFragment");
            }
        });

        // Set click listener for the CardView to toggle details visibility
        View cardView = view.findViewById(R.id.pizzaCardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDetailsVisibility(descriptionTextView, priceTextView, favoriteButton, orderButton);
            }
        });

        return view;
    }

    private void toggleDetailsVisibility(TextView descriptionTextView, TextView priceTextView, Button favoriteButton, Button orderButton) {
        if (detailsVisible) {
            animateVisibility(descriptionTextView, View.GONE);
            animateVisibility(priceTextView, View.GONE);
            animateVisibility(favoriteButton, View.GONE);
            animateVisibility(orderButton, View.GONE);
        } else {
            animateVisibility(descriptionTextView, View.VISIBLE);
            animateVisibility(priceTextView, View.VISIBLE);
            animateVisibility(favoriteButton, View.VISIBLE);
            animateVisibility(orderButton, View.VISIBLE);
        }
        detailsVisible = !detailsVisible;
    }

    private void animateVisibility(final View view, final int visibility) {
        float startAlpha = visibility == View.VISIBLE ? 0f : 1f;
        float endAlpha = visibility == View.VISIBLE ? 1f : 0f;
        float startTranslation = visibility == View.VISIBLE ? 30f : 0f;
        float endTranslation = visibility == View.VISIBLE ? 0f : 30f;

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("alpha", startAlpha, endAlpha),
                PropertyValuesHolder.ofFloat("translationY", startTranslation, endTranslation));
        animator.setDuration(500); // Increase duration for a stronger effect
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Use AccelerateDecelerateInterpolator for smooth effect
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (visibility == View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (visibility != View.VISIBLE) {
                    view.setVisibility(View.GONE);
                }
            }
        });
        animator.start();
    }

    private void animateHeart(ImageView heartImageView) {
        heartImageView.setVisibility(View.VISIBLE);
        heartImageView.setScaleX(0f);
        heartImageView.setScaleY(0f);
        heartImageView.setAlpha(0f);

        heartImageView.animate()
                .scaleX(1.5f) // Increase the scale for a stronger effect
                .scaleY(1.5f)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500) // Increase duration for a stronger effect
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        heartImageView.animate()
                                .scaleX(0f)
                                .scaleY(0f)
                                .alpha(0f)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        heartImageView.setVisibility(View.GONE);
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }
}
