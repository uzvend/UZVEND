package com.example.uzvend.Validator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.uzvend.Models.SimpleVerticalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TextFieldValidator {
    public static boolean isValid(String s){
        return s.length()<6;
    }
    public static boolean phoneNumberCheck(String s){
        return s.length()<10;
    }


    public static boolean orderingProductCheck(Context ct){

        Gson gson = new Gson();
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ct);
        //Shared Preference will work only in Android device and The test is running into the compiler.
        // Compiler don't have shared preference function. That's why this test is failing.
        String json = appSharedPrefs.getString("basket", "");

        Type type = new TypeToken<List<SimpleVerticalModel>>(){}.getType();
        List<SimpleVerticalModel> basket = gson.fromJson(json, type);

        if(basket == null){
            basket = new ArrayList<>();
        }

        return basket.size() == 0;
    }
}
