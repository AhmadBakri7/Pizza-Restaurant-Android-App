package com.example.resturantproject.Activities.ui.callfind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.resturantproject.R;

public class CallFindFragment extends Fragment {

    public CallFindFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_call_find, container, false);

        // Find the buttons
        Button callButton = rootView.findViewById(R.id.callButton);
        Button mapsButton = rootView.findViewById(R.id.mapsButton);
        Button emailButton = rootView.findViewById(R.id.emailButton);

        // Set onClick listeners
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to call the restaurant
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0599000000"));
                startActivity(callIntent);
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open Google Maps
                String geoUri = "geo:31.961013,35.190483?q=31.961013,35.190483(AdvancePizza)";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to send an email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "AdvancePizza@Pizza.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        return rootView;
    }
}
