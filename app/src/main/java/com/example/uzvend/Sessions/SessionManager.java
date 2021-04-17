package com.example.uzvend.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public  SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;


    public static final String PREF_NAME= "LOGIN";
    public static final String LOGIN= "IS_LOGIN";
    public static final String USER_ID= "USER_ID";
    public static final String USER_NAME= "USER_NAME";
//    private static final String EMAIL= "EMAIL";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession (String user_id, String username){

        editor.putBoolean(LOGIN,true);
        editor.putString(USER_ID,user_id);
        editor.putString(USER_NAME,username);
        editor.apply();
    }

    public boolean isLogin(){
        return  sharedPreferences.getBoolean(LOGIN,false);
    }

    public HashMap<String, String > getUserDetail(){
        HashMap<String, String > user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID,null));
        user.put(USER_NAME, sharedPreferences.getString(USER_NAME,null));
        return user;
    }


}
