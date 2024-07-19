package com.example.resturantproject.Activities.ui.proflle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.resturantproject.Activities.Login;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.DataBaseHelper;
import com.example.resturantproject.Security.Hash;
import com.example.resturantproject.Security.User;
import java.io.IOException;
import java.sql.SQLException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;

    private EditText firstNameEditText, lastNameEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    private Button saveButton, changePictureButton;
    private ImageView profilePictureImageView;

    private User currentUser;
    private DataBaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        dbHelper = new DataBaseHelper(getContext());
        loadCurrentUser();
        populateUI();
        setupSaveButton();
        setupChangePictureButton();
    }

    private void initializeViews(View view) {
        firstNameEditText = view.findViewById(R.id.first_name);
        lastNameEditText = view.findViewById(R.id.last_name);
        phoneNumberEditText = view.findViewById(R.id.phone_number);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password);
        saveButton = view.findViewById(R.id.save_button);
        changePictureButton = view.findViewById(R.id.change_picture_button);
        profilePictureImageView = view.findViewById(R.id.profile_image);
    }

    private void loadCurrentUser() {
        String userEmail = Login.userEmail;
        currentUser = dbHelper.getUserByEmail(userEmail);
    }

    private void populateUI() {
        if (currentUser != null) {
            firstNameEditText.setText(currentUser.getFirstName());
            lastNameEditText.setText(currentUser.getLastName());
            phoneNumberEditText.setText(currentUser.getPhoneNumber());
            if (currentUser.getProfilePictureUri() != null) {
                profilePictureImageView.setImageURI(Uri.parse(currentUser.getProfilePictureUri()));
            }
        }
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            if (validateInputs(firstNameEditText.getText().toString()
                    , lastNameEditText.getText().toString()
                    , phoneNumberEditText.getText().toString()
                    , passwordEditText.getText().toString()
                    , confirmPasswordEditText.getText().toString())) {
                updateUserInfo();
            }
        });
    }

    private void setupChangePictureButton() {
        changePictureButton.setOnClickListener(v -> checkPermission());
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            openImageChooser();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profilePictureImageView.setImageBitmap(bitmap);
                currentUser.setProfilePictureUri(imageUri.toString());
                try {
                    dbHelper.updateUser(currentUser); // Save the URI to the database
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputs(String firstName, String lastName, String phoneNumber, String password, String confirmPassword) {
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

    private void updateUserInfo() {
        if (currentUser != null) {
            currentUser.setFirstName(firstNameEditText.getText().toString());
            currentUser.setLastName(lastNameEditText.getText().toString());
            currentUser.setPhoneNumber(phoneNumberEditText.getText().toString());
            currentUser.setHashedPassword(Hash.hashPassword(passwordEditText.getText().toString()));

            // Update the user's information in the database
            try {
                dbHelper.updateUser(currentUser);
                Toast.makeText(requireContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
