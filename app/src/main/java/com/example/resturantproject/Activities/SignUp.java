package com.example.resturantproject.Activities;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.Hash;
import com.example.resturantproject.Security.User;

import java.util.ArrayList;
import java.util.HashSet;

public class SignUp extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner genderSpinner;
    private DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        phoneNumberEditText = findViewById(R.id.phone_number);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        genderSpinner = findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);



        dbHelper = new DataBaseHelper(this);

        findViewById(R.id.create_account_button).setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        genderSpinner = findViewById(R.id.gender_spinner);

        if (!validateInputs(firstName, lastName, phoneNumber, email, password, confirmPassword)) {
            return;
        }

        // Encrypt password
        String hashedPassword = Hash.hashPassword(password);

        // Create User object
        User user = new User(email, firstName, lastName, phoneNumber, gender, hashedPassword, new HashSet<>(), false);

        // Save the user to database
        try {
            // MAKE SURE IF EMAIL ALREADY EXISTS -> GIVE ERROR MESSAGE
            dbHelper.insertUser(user);
            // Redirect to login page or show success message...
            Toast.makeText(this, "User Added !!!",
                    Toast.LENGTH_SHORT).show();
        }catch (java.sql.SQLException e) {
            Toast.makeText(this, "Email already exists. Please use a different email.",
                    Toast.LENGTH_SHORT).show();
            emailEditText.setError("Email Already in Use");
            return;
        }
        // Redirect to login page
        Intent intent = new Intent(SignUp.this, Login.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            firstNameEditText.setError("First name must be at least 3 characters");
            return false;
        }

        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            lastNameEditText.setError("Last name must be at least 3 characters");
            return false;
        }

        if (!phoneNumber.matches("05\\d{8}")) {
            phoneNumberEditText.setError("Phone number must be exactly 10 digits starting with '05'");
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            passwordEditText.setError("Password must be at least 8 characters, including 1 letter and 1 number");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

}
