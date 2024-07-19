package com.example.resturantproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.Hash;
import com.example.resturantproject.Security.SharedPrefManager;

public class Login extends AppCompatActivity {

    public static String userEmail;
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private DataBaseHelper dbHelper;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rememberMeCheckBox = findViewById(R.id.remember_me);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        Intent intent = getIntent();
        if(intent.hasExtra("EMAIL")){
            emailEditText.setText(intent.getStringExtra("EMAIL"));
        }
        else{
            emailEditText.setText(sharedPrefManager.readString("EMAIL",""));
            passwordEditText.setText(sharedPrefManager.readString("PASSWORD", ""));

        }


        dbHelper = new DataBaseHelper(this);

        // dummy user
//        dbHelper.insertUser(new User("admin@gmail.com", "Admin", "Admin", "0598000518"
//                , "Male", Hash.hashPassword("Admin")));

        findViewById(R.id.sign_in_button).setOnClickListener(v -> login());

        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(Login.this, SignUp.class);
            startActivity(signUpIntent);
        });
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        //Log.d("SYS120", email);
        //Log.d("SYS120", password);


        // Encrypt password
        String hashedPassword = Hash.hashPassword(password);

        //Log.d("SYS120", hashedPassword);
        // Authenticate user
        if (authenticateUser(email, hashedPassword)) {
            Log.d("SYS120", "AUTH DONE");
            userEmail = email;
            // Redirect to the main activity or another activity
            if(rememberMeCheckBox.isChecked()){
                sharedPrefManager.writeString("EMAIL", emailEditText.getText().toString());
                sharedPrefManager.writeString("PASSWORD", passwordEditText.getText().toString());
            }
            if(!dbHelper.isAdmin(userEmail)){
                Intent intent = new Intent(Login.this, Customer.class);
                startActivity(intent);
                finish();
            }else{
                // take to admin board
                Intent intent = new Intent(Login.this, Admin.class);
                startActivity(intent);
                finish();
            }

        } else {
            // Show error message
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean authenticateUser(String email, String hashedPassword) {
        Cursor cursor = dbHelper.findUserByEmail(email);
        if (cursor != null) {
            int hashedPasswordIndex = cursor.getColumnIndex("HASHED_PASSWORD");
            if (hashedPasswordIndex >= 0 && cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(hashedPasswordIndex);
                //Log.d("SYS120", "Stored:" + storedHashedPassword);
                cursor.close();
                return storedHashedPassword.equals(hashedPassword);
            }
            cursor.close();
        }
        return false;
    }


}