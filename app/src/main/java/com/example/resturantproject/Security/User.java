package com.example.resturantproject.Security;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String hashedPassword;
    private Set<String> favorites;
    private boolean isAdmin; // New attribute
    private String profilePictureUri;


    public User(){}

    public User(String email, String firstName, String lastName, String phoneNumber, String gender, String hashedPassword, Set<String> favorites, boolean isAdmin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.hashedPassword = hashedPassword;
        this.favorites = favorites;
        this.isAdmin = isAdmin; // New attribute
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<String> favorites) {
        this.favorites = favorites;
    }

    public boolean isAdmin() { // New getter
        return isAdmin;
    }

    public void setAdmin(boolean admin) { // New setter
        isAdmin = admin;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", favorites=" + favorites +
                ", isAdmin=" + isAdmin + // New attribute
                '}';
    }
}
