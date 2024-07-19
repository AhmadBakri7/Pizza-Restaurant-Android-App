package com.example.resturantproject.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resturantproject.databinding.ActivityAdminBinding;

public class Admin extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdmin.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

//        View headerView = navigationView.getHeaderView(0);
//
//        // Find the TextView and ImageView elements in the header view
//        TextView textViewName = headerView.findViewById(R.id.name);
//        TextView textViewEmail = headerView.findViewById(R.id.textView);
//        ImageView imageViewProfile = headerView.findViewById(R.id.imageView);
//        DataBaseHelper dbHelper = new DataBaseHelper(this);
//
//        User use = dbHelper.getUserByEmail(Login.userEmail);
//        // Update the views with new data
//        textViewName.setText(use.getFirstName() + " " + use.getLastName());
//        textViewEmail.setText(use.getEmail());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_addadmin, R.id.nav_allorders, R.id.nav_addoffer, R.id.nav_logout, R.id.nav_statistics)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}