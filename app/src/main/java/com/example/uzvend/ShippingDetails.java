package com.example.uzvend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.Sessions.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShippingDetails extends AppCompatActivity {

    Button payment_button;
    String user_id;
    EditText fname, lname, email,phone,add_1, add_2;
    ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        add_1 = (EditText) findViewById(R.id.add_1);
        add_2 = (EditText) findViewById(R.id.add_2);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        payment_button = (Button) findViewById(R.id.payment_button);
        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chackandContinue();
            }
        });

        SessionManager sessionManager = new SessionManager(ShippingDetails.this);

        user_id = sessionManager.getUserDetail().get(SessionManager.USER_ID);
    }

    void chackandContinue(){

        if (fname.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter firstname", Toast.LENGTH_SHORT).show();
        }else if (lname.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter lastname", Toast.LENGTH_SHORT).show();
        }else if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }else if (phone.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter phone", Toast.LENGTH_SHORT).show();
        }else if (add_1.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter address line 1", Toast.LENGTH_SHORT).show();
        }else if (add_2.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter address line 2", Toast.LENGTH_SHORT).show();
        }

        payment_button.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"payment.php?c_id="+user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String token = response.getString("token");
                    DropInRequest dropInRequest = new DropInRequest()
                            .tokenizationKey(token);
                    startActivityForResult(dropInRequest.getIntent(ShippingDetails.this), 202);
                }catch (JSONException io){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShippingDetails.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(ShippingDetails.this).add(requestForChangePass);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                makePayment(result.getPaymentMethodNonce().getNonce());
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {

                payment_button.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);

                Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            } else {

                payment_button.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);

                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void makePayment(String paymentNonce){

        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"makePayment.php?nonce="+paymentNonce+"&price="+getIntent().getStringExtra("price")+"&u_id="+user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ShippingDetails.this, ""+response.toString(), Toast.LENGTH_SHORT).show();

                try{
                    String success = response.getString("success");
                    if(success.equals("true")){
                        submit_order();
                    }else{
                        payment_button.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(ShippingDetails.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException io){
                    payment_button.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShippingDetails.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("nonce",paymentNonce);
                return headers;
            }
        };
        Volley.newRequestQueue(ShippingDetails.this).add(requestForChangePass);

    }

    void submit_order(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(ShippingDetails.this);

        String json = appSharedPrefs.getString("basket", "");
        String url = Constants.root_url + "makeOrder.php?fname="+fname.getText().toString()+"&lname="+lname.getText().toString()+"&u_id="+user_id+"&email="+email.getText().toString()+"&phone="+phone.getText().toString()+"&add_1="+add_1.getText().toString()+"&add_2="+add_2.getText().toString()+"&product="+json;
        StringRequest requestForChangePass = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("success").equals("true")){
                        appSharedPrefs.edit().clear().apply();
                        Toast.makeText(ShippingDetails.this, "Order has been successfully placed.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ShippingDetails.this, "Some error occur placing your order.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        Volley.newRequestQueue(ShippingDetails.this).add(requestForChangePass);
    }
}