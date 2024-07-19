package com.example.resturantproject.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<String> getPizzaTypesFromJson(String json) {
        List<String> pizzaTypes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("types");

            for (int i = 0; i < jsonArray.length(); i++) {
                pizzaTypes.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return pizzaTypes;
    }
}

