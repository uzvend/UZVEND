package com.example.uzvend.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.uzvend.Adapters.CatAdapter;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.MainActivity;
import com.example.uzvend.Models.CategoryModel;
import com.example.uzvend.R;
import com.example.uzvend.Sessions.SessionManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryFragment extends Fragment implements View.OnClickListener {


    public CategoryFragment() {
        // Required empty public constructor
    }

    DrawerLayout drawerLayout;
    ImageView navigationBar;
    NavigationView navigationView;
    private View view;
    private RelativeLayout  bookmarks, basket;
    private TextView textView4, textView5, textView6, name_view, send_feedback, report_safety_emergency, rate_us;
    SessionManager sessionManager;
    private TextView login, logout;

    /// category slider starts///
    private RecyclerView recyclerViewCategory;
    private CatAdapter catAdapter;
    private List<CategoryModel> categoryModelList;
    /// category slider ends///



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        sessionManager = new SessionManager(getContext());
        onSetNavigationDrawerEvents();
        loadData();
        return view;
    }


    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        navigationBar = (ImageView) view.findViewById(R.id.navigationBar);

        login =(TextView) view.findViewById(R.id.login);
        logout =(TextView) view.findViewById(R.id.signout__);
        bookmarks = (RelativeLayout) view.findViewById(R.id.relativeLayout3);
        basket = (RelativeLayout) view.findViewById(R.id.relativeLayout4);
        name_view = (TextView) view.findViewById(R.id.logout);

//        textView4 = (TextView) view.findViewById(R.id.textView4);
//        textView5 = (TextView) view.findViewById(R.id.textView5);
//        textView6 = (TextView) view.findViewById(R.id.textView6);
//        textView7 = (TextView) view.findViewById(R.id.textView7);
        send_feedback = (TextView) view.findViewById(R.id.send_feedback);
        report_safety_emergency = (TextView) view.findViewById(R.id.report_safety_emergency);
        rate_us = (TextView) view.findViewById(R.id.rate_us);

        navigationBar.setOnClickListener(this);
        logout.setOnClickListener(this);
        login.setOnClickListener(this);
        bookmarks.setOnClickListener(this);
        basket.setOnClickListener(this);

//        textView4.setOnClickListener(this);
//        textView5.setOnClickListener(this);
//        textView6.setOnClickListener(this);
//        textView7.setOnClickListener(this);
        send_feedback.setOnClickListener(this);
        report_safety_emergency.setOnClickListener(this);
        rate_us.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigationBar:
                drawerLayout.openDrawer(navigationView, true);
                break;
            case R.id.login:
                Login();
                break;
            case R.id.signout__:
                Logout();
                break;
            case R.id.relativeLayout3:
                Toast.makeText(getContext(),"Reserved for Future Implementations", Toast.LENGTH_LONG).show();
                break;
            case R.id.relativeLayout4:
                Fragment fr=new BasketFragment();
                FragmentTransaction tras = getFragmentManager().beginTransaction();
                tras.replace(R.id.frameLayout, fr);
                tras.commit();
                break;
//            case R.id.textView4:
//                Toast.makeText(getContext(),"textView4", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.textView5:
//                Toast.makeText(getContext(),"textView5", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.textView6:
//                Toast.makeText(getContext(),"textView6", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.textView7:
//                Toast.makeText(getContext(),"textView7", Toast.LENGTH_LONG).show();
//                break;
            case R.id.send_feedback:
                startBrowser(1);
                break;
            case R.id.report_safety_emergency:
                startBrowser(2);
                break;
            case R.id.rate_us:
                startBrowser(3);
                break;
        }
    }


    void startBrowser(int type){
        if(type==1){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        }else if(type==2){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        }else if(type==3){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        }
    }

    private void Logout() {

        sessionManager.editor.clear();
        sessionManager.editor.commit();


        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        Animatoo.animateSwipeRight(getContext());

    }

    private void Login() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        Animatoo.animateSwipeRight(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();

        if(sessionManager.isLogin()){

            login.setVisibility(View.GONE);

            String s = sessionManager.getUserDetail().get(SessionManager.USER_NAME);
            name_view.setText(s);
            name_view.setVisibility(View.VISIBLE);
        }
    }


    void loadData(){
        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"category.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray category = response.getJSONArray("category");

                    /// category model list start///
                    recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recyclerViewCategory);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerViewCategory.setLayoutManager(layoutManager);

                    categoryModelList = new ArrayList<>();



                    for (int i=0;i<category.length();i++){
                        JSONArray arr = category.getJSONArray(i);
                        categoryModelList.add(new CategoryModel(arr.get(0).toString(),arr.get(2).toString(),arr.get(1).toString()));
                    }

                    catAdapter = new CatAdapter(categoryModelList,getContext());
                    recyclerViewCategory.setAdapter(catAdapter);
                    catAdapter.notifyDataSetChanged();

                    /// category model list ends ///

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.d("qwerty", response.toString());
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

}



