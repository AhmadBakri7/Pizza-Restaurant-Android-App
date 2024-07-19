package com.example.resturantproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.resturantproject.R;
import com.example.resturantproject.models.Pizza;

import java.util.ArrayList;

public class LoginSignUp extends AppCompatActivity {

    public static ArrayList<Pizza> pizzaList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        addPizzas();

        Button loginButton = findViewById(R.id.Login);
        Button signUpButton = findViewById(R.id.SignUp);

        // Click listener for Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignUp.this, Login.class);
                startActivity(intent);
            }
        });

        // Click listener for Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignUp.this, SignUp.class);
                startActivity(intent);
            }
        });
    }


    private void addPizzas(){
        pizzaList.add(new Pizza("Tandoori Chicken Pizza",
                "A flavorful pizza topped with tandoori chicken, red onions, and cilantro.",
                16.0, "Chicken", R.drawable.tandoori));
        pizzaList.add(new Pizza("BBQ Chicken Pizza",
                "A savory pizza topped with BBQ chicken, red onions, and cilantro.", 16.0,
                "Chicken", R.drawable.bbq));
        pizzaList.add(new Pizza("Buffalo Chicken Pizza",
                "A spicy pizza topped with buffalo chicken, blue cheese, and celery.",
                16.5, "Chicken", R.drawable.buffalo));
        pizzaList.add(new Pizza("Pesto Chicken Pizza",
                "A delicious pizza topped with pesto chicken, sun-dried tomatoes, and mozzarella.",
                17.0, "Chicken", R.drawable.pesto));

        pizzaList.add(new Pizza("Vegetarian Pizza",
                "A healthy pizza topped with bell peppers, onions, mushrooms, and olives.",
                13.5, "Veggies", R.drawable.veggie));
        pizzaList.add(new Pizza("Mushroom Truffle Pizza",
                "A gourmet pizza topped with mushrooms and drizzled with truffle oil.",
                19.0, "Veggies", R.drawable.mushroom));

        pizzaList.add(new Pizza("Margarita",
                "A classic Italian pizza with fresh tomatoes, mozzarella cheese, and basil.",
                12.5, "Other", R.drawable.margarita));
        pizzaList.add(new Pizza("Neapolitan",
                "An authentic Neapolitan pizza with a thin crust, San Marzano tomatoes, mozzarella, and a drizzle of olive oil.",
                13.0, "Other", R.drawable.neapolitan));
        pizzaList.add(new Pizza("Hawaiian",
                "A sweet and savory pizza topped with pineapple, ham, and mozzarella cheese.",
                14.0, "Other", R.drawable.hawaiian));
        pizzaList.add(new Pizza("Pepperoni",
                "A popular pizza topped with pepperoni slices, mozzarella cheese, and tomato sauce.",
                12.0, "Other", R.drawable.pepperoni));
        pizzaList.add(new Pizza("New York Style",
                "A large, thin-crust pizza topped with tomato sauce and mozzarella cheese.",
                15.0, "Other", R.drawable.nyc));
        pizzaList.add(new Pizza("Calzone",
                "A folded pizza stuffed with ricotta, mozzarella, and your choice of toppings.",
                14.5, "Other", R.drawable.calzone));
        pizzaList.add(new Pizza("Seafood Pizza",
                "A delectable pizza topped with shrimp, calamari, and mussels.",
                18.0, "Other", R.drawable.seafood));
    }
}