package com.example.resturantproject.API;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.resturantproject.Activities.LoginSignUp;
import com.example.resturantproject.Activities.MainActivity;

import java.util.List;

public class FetchPizzaTypesTask extends AsyncTask<String, Void, String> {

    private Context context;
    private List<String> pizzaTypes;

    public FetchPizzaTypesTask(Context context, List<String> pizzaTypes) {
        this.context = context;
        this.pizzaTypes = pizzaTypes;
    }

    @Override
    protected String doInBackground(String... urls) {
        return HttpManager.getData(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        } else {
            List<String> fetchedPizzaTypes = JSONParser.getPizzaTypesFromJson(result);
            if (fetchedPizzaTypes != null) {
                pizzaTypes.clear();
                pizzaTypes.addAll(fetchedPizzaTypes);
                Toast.makeText(context, "Data fetched successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, LoginSignUp.class);
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "Failed to parse data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
