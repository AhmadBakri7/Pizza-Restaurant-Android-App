package com.example.resturantproject.Activities.ui.addoffers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.SpecialOfferDatabaseHelper;
import com.example.resturantproject.models.Pizza;
import com.example.resturantproject.models.SpecialOffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addOffers extends Fragment {

    private Spinner spinnerPizzaName, spinnerPizzaSize;
    private EditText etOfferStartDate, etOfferEndDate, etTotalPrice, etPizzaCount;
    private Button btnAddOffer;
    private SpecialOfferDatabaseHelper databaseHelper;
    private SimpleDateFormat dateFormat;

    public addOffers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_offers, container, false);

        spinnerPizzaName = view.findViewById(R.id.spinnerPizzaName);
        spinnerPizzaSize = view.findViewById(R.id.spinnerPizzaSize);
        etOfferStartDate = view.findViewById(R.id.etOfferStartDate);
        etOfferEndDate = view.findViewById(R.id.etOfferEndDate);
        etTotalPrice = view.findViewById(R.id.etTotalPrice);
        etPizzaCount = view.findViewById(R.id.etPizzaCount); // Add this line
        btnAddOffer = view.findViewById(R.id.btnAddOffer);

        databaseHelper = new SpecialOfferDatabaseHelper(getContext());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        setupPizzaNameSpinner();
        setupPizzaSizeSpinner();

        etOfferStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etOfferStartDate);
            }
        });

        etOfferEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etOfferEndDate);
            }
        });

        btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpecialOffer();
            }
        });

        return view;
    }

    private void setupPizzaNameSpinner() {
        List<String> pizzaNames = new ArrayList<>();
        for (Pizza pizza : LoginSignUp.pizzaList) {
            pizzaNames.add(pizza.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, pizzaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaName.setAdapter(adapter);
    }

    private void setupPizzaSizeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaSize.setAdapter(adapter);
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        dateEditText.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addSpecialOffer() {
        String pizzaName = spinnerPizzaName.getSelectedItem().toString();
        String pizzaSize = spinnerPizzaSize.getSelectedItem().toString();
        String offerStartDateString = etOfferStartDate.getText().toString().trim();
        String offerEndDateString = etOfferEndDate.getText().toString().trim();
        String totalPriceString = etTotalPrice.getText().toString().trim();
        String pizzaCountString = etPizzaCount.getText().toString().trim(); // Add this line

        if (pizzaName.isEmpty() || pizzaSize.isEmpty() || offerStartDateString.isEmpty() || offerEndDateString.isEmpty() || totalPriceString.isEmpty() || pizzaCountString.isEmpty()) { // Add this line
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Date offerStartDate = null;
        Date offerEndDate = null;
        double totalPrice;
        int pizzaCount; // Add this line

        try {
            offerStartDate = dateFormat.parse(offerStartDateString);
            offerEndDate = dateFormat.parse(offerEndDateString);
            totalPrice = Double.parseDouble(totalPriceString);
            pizzaCount = Integer.parseInt(pizzaCountString); // Add this line
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        SpecialOffer specialOffer = new SpecialOffer(pizzaName, pizzaSize, offerStartDate, offerEndDate, totalPrice, pizzaCount); // Add pizzaCount here
        databaseHelper.addSpecialOffer(specialOffer);

        Toast.makeText(getContext(), "Special Offer Added", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        spinnerPizzaName.setSelection(0);
        spinnerPizzaSize.setSelection(0);
        etOfferStartDate.setText("");
        etOfferEndDate.setText("");
        etTotalPrice.setText("");
        etPizzaCount.setText(""); // Add this line
    }
}
