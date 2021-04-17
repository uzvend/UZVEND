package com.example.uzvend.OperationRetrofitApi;

import com.google.gson.annotations.SerializedName;

public class Users {

        @SerializedName("response")
        public String Response;

        @SerializedName("user_id")
        private  String UserId;

        @SerializedName("username")
        private  String Username;

    public String getUsername() {
        return Username;
    }

    public String getResponse() {
            return Response;
        }

        public String getUserId() {
            return UserId;
        }
    }

