package com.example.uzvend;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uzvend.Adapters.SimpleVerticalAdapter;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.Models.SimpleVerticalModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductList extends AppCompatActivity {

    private List<SimpleVerticalModel> simpleVerticalModelList;

    private SimpleVerticalAdapter simpleVerticalAdapter;
    private RecyclerView greatOffersRecyclerViewVertical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        loadData();
    }

    void loadData(){

        String url = "";
        if(getIntent().getStringExtra("query")!=null){
            url = Constants.root_url+"searchProduct.php?query="+getIntent().getStringExtra("query");
        }else{
            url = Constants.root_url+"productList.php?cat="+getIntent().getStringExtra("cat");
        }

        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray products = response.getJSONArray("product");

                    ///  new great offers vertical slider starts///
                    greatOffersRecyclerViewVertical = (RecyclerView) findViewById(R.id.greatOffersRecyclerViewVertical);
                    LinearLayoutManager layoutManagerVerticalGreatOffers = new LinearLayoutManager(ProductList.this);
                    layoutManagerVerticalGreatOffers.setOrientation(RecyclerView.VERTICAL);
                    greatOffersRecyclerViewVertical.setLayoutManager(layoutManagerVerticalGreatOffers);

                    simpleVerticalModelList = new ArrayList<>();

                    for (int i=0;i<products.length();i++){
                        JSONArray arr = products.getJSONArray(i);
                        simpleVerticalModelList.add(new SimpleVerticalModel(arr.get(0).toString(),arr.get(3).toString(),arr.get(1).toString(),arr.get(4).toString().length() > 30 ? arr.get(4).toString().substring(0, 30):arr.get(4).toString() ,"",Constants.currency+" "+arr.get(2).toString(),
                                "Designed & offered by UZVEND",""));
                    }


                    simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,ProductList.this);
                    greatOffersRecyclerViewVertical.setAdapter(simpleVerticalAdapter);
                    simpleVerticalAdapter.notifyDataSetChanged();
                    ///   new great offers vertical slider  ends ///


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductList.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("api-key", "2a5588cf-4cf3-4f1c-9548-cc1db4b54ae3");
                return headers;
            }
        };
        Volley.newRequestQueue(ProductList.this).add(requestForChangePass);


    }
}