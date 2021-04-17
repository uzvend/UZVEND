package com.example.uzvend.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.R;
import com.example.uzvend.Sessions.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    TextView not_logged_in;
    ListView payment_history;
    String user_id = "";
    ArrayList<PaymentHistoryModel> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        not_logged_in = (TextView) view.findViewById(R.id.not_logged_in);
        if (!new SessionManager(getContext()).isLogin()){
            not_logged_in.setVisibility(View.VISIBLE);
            not_logged_in.setText("You are not logged in. Please login to continue");

        }else{
            SessionManager sessionManager = new SessionManager(getContext());

            user_id = sessionManager.getUserDetail().get(SessionManager.USER_ID);
            payment_history = view.findViewById(R.id.payment_history);
            loadData();
        }


        return view;
    }


    void loadData(){
        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"payhistory.php?u_id="+user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                list= new ArrayList<>();
                try {
                    JSONArray history = response.getJSONArray("history");
                    for (int i=0;i<history.length();i++){
                        JSONArray arr = history.getJSONArray(i);
                        list.add(new PaymentHistoryModel(arr.get(0).toString(), arr.get(2).toString(),arr.get(3).toString(), arr.get(4).toString()));
                    }
                    payment_history.setAdapter(new PaymentHistoryAdapter());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        Volley.newRequestQueue(getContext()).add(requestForChangePass);
    }

    class PaymentHistoryModel{
        String id, payment, type, date;

        public PaymentHistoryModel(String id, String payment, String type, String date) {
            this.id = id;
            this.payment = payment;
            this.type = type;
            this.date = date;
        }
    }

    class PaymentHistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.payment_row, null);
            TextView price, type, date;
            price = view1.findViewById(R.id.total_payment);
            type = view1.findViewById(R.id.status);
            date = view1.findViewById(R.id.date);

            price.setText("Price: $"+list.get(i).payment);
            if(list.get(i).type.equals("1")){
                type.setText("Status: Success");
                type.setTextColor(Color.parseColor("#008000"));
            }else{
                type.setText("Status: Failed");
                type.setTextColor(Color.parseColor("#FF0000"));
            }
            //type.setText("Status: "+list.get(i).type == "1"?"Success":"Failed");
            date.setText("Date: "+list.get(i).date);

            return view1;
        }
    }
}