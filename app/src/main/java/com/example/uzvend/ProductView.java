package com.example.uzvend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.Models.SimpleVerticalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductView extends AppCompatActivity {

    Button btn_add_to_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        btn_add_to_cart = (Button) findViewById(R.id.add_to_cart);
        Glide.with(this).load(getIntent().getStringExtra("image")).fitCenter().into(((ImageView) findViewById(R.id.selected_image)));
        ((TextView) findViewById(R.id.title)).setText(getIntent().getStringExtra("title"));
        ((TextView) findViewById(R.id.description)).setText(getIntent().getStringExtra("desc"));
        ((TextView) findViewById(R.id.price)).setText("Price: "+getIntent().getStringExtra("price"));
        ((TextView) findViewById(R.id.offered_by)).setText(getIntent().getStringExtra("status"));



        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_qty_dialog();
            }
        });

        loadData();

    }
    Dialog d = null;
    void show_qty_dialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(ProductView.this);

        builder.setTitle("Pick quantity...");
        View v = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(v);
        Button b1 = (Button) v.findViewById(R.id.button1);
        Button b2 = (Button) v.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(np.getValue()>0){
                    addItemToBasket(np.getValue());
                    finish();
                }else{
                    Toast.makeText(ProductView.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d = builder.create();
        d.show();
    }

    void addItemToBasket(int Qty){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("basket", "");
        Type type = new TypeToken<List<SimpleVerticalModel>>(){}.getType();
        List<SimpleVerticalModel> basket = gson.fromJson(json, type);

        if(basket == null){
            basket = new ArrayList<>();
        }

        boolean added = false;
        if(basket.size()>0){
            for (int i=0;i<basket.size();i++){
                if (basket.get(i).getId().equals(getIntent().getStringExtra("id"))){
                    basket.get(i).addCart_qty(Qty);
                    added = true;
                }
            }
        }

        if(!added){
            SimpleVerticalModel model = Utils.shared_model;
            model.addCart_qty(Qty);
            basket.add(model);
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        json = gson.toJson(basket);
        prefsEditor.putString("basket", json);
        prefsEditor.apply();
        Toast.makeText(this, "Product Added to basket", Toast.LENGTH_SHORT).show();
    }

    void loadData(){
        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"product.php?id="+getIntent().getStringExtra("id"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray product = response.getJSONArray("product");
                    String dtStart = product.get(7).toString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = format.parse(dtStart);
                        if (date.after(new Date())){
                            btn_add_to_cart.setVisibility(View.GONE);
                        }else{
                            btn_add_to_cart.setVisibility(View.VISIBLE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductView.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(this).add(requestForChangePass);


    }

}