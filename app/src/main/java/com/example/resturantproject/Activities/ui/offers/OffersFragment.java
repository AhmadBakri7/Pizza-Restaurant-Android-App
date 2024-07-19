package com.example.resturantproject.Activities.ui.offers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.resturantproject.Activities.fragments.SpecialOfferFragment;
import com.example.resturantproject.R;
import com.example.resturantproject.Security.SpecialOfferDatabaseHelper;
import com.example.resturantproject.models.SpecialOffer;
import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {

    private List<SpecialOffer> specialOffers;

    public OffersFragment() {
        specialOffers = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        LinearLayout fragmentContainer = view.findViewById(R.id.fragmentContainer);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isAdded()) {
                transaction.remove(fragment);
            }
        }
        transaction.commit();

        SpecialOfferDatabaseHelper specialOfferDatabaseHelper = new SpecialOfferDatabaseHelper(getContext());
        specialOffers = specialOfferDatabaseHelper.getAllSpecialOffers();

        for (SpecialOffer offer : specialOffers) {
            SpecialOfferFragment fragment = SpecialOfferFragment.newInstance(offer);
            getChildFragmentManager().beginTransaction().add(fragmentContainer.getId(), fragment).commit();
        }

        return view;
    }
}