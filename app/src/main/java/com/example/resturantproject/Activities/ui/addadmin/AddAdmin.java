package com.example.resturantproject.Activities.ui.addadmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.Hash;
import com.example.resturantproject.Security.User;

import java.sql.SQLException;
import java.util.HashSet;

public class AddAdmin extends Fragment {

    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner genderSpinner;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        firstNameEditText = view.findViewById(R.id.first_name);
        lastNameEditText = view.findViewById(R.id.last_name);
        phoneNumberEditText = view.findViewById(R.id.phone_number);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password);
        genderSpinner = view.findViewById(R.id.gender_spinner);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        dbHelper = new DataBaseHelper(getContext());

        view.findViewById(R.id.create_admin_button).setOnClickListener(v -> createAdmin());

        return view;
    }

    private void createAdmin() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        if (!validateInputs(firstName, lastName, phoneNumber, email, password, confirmPassword)) {
            return;
        }

        String hashedPassword = Hash.hashPassword(password);

        User admin = new User(email, firstName, lastName, phoneNumber, gender, hashedPassword, new HashSet<>(), true);

        try {
            dbHelper.insertUser(admin);
            Toast.makeText(getContext(), "Admin Added Successfully!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            emailEditText.setError("Email Already in Use");
        }
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
