package com.example.uzvend.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.uzvend.Adapters.BasketVerticalAdapter;
import com.example.uzvend.EmailLoginRegister.EmailLoginMainActivity;
import com.example.uzvend.MainActivity;
import com.example.uzvend.Models.SimpleVerticalModel;
import com.example.uzvend.R;
import com.example.uzvend.Sessions.SessionManager;
import com.example.uzvend.ShippingDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class BasketFragment extends Fragment implements View.OnClickListener {



    public BasketFragment() {
        // Required empty public constructor
    }

    DrawerLayout drawerLayout;
    ImageView navigationBar;
    NavigationView navigationView;
    private View view;
    private RelativeLayout bookmarks,basket;
    private TextView textView4, textView5, textView6, name_view,send_feedback, report_safety_emergency,rate_us;
    SessionManager sessionManager;
    private TextView login, logout;

    ///  new arrivals vertical slider starts///
    private RecyclerView newArrivalVerticalRecyclerview;
    ///   new arrivals vertical slider  ends ///
    private BasketVerticalAdapter basketVerticalAdapter;
    private List<SimpleVerticalModel> simpleVerticalModelList;
    ///  simple vertical slider ends ///

    Button add_order;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_basket, container, false);
        add_order = view.findViewById(R.id.add_order);
        sessionManager = new SessionManager(getContext());
        onSetNavigationDrawerEvents();

        loadData();

        return view;
    }


    void loadData(){

        newArrivalVerticalRecyclerview = (RecyclerView) view.findViewById(R.id.newArrivalVerticalRecyclerview);
        LinearLayoutManager layoutManagerVerticalNewArrival = new LinearLayoutManager(getContext());
        layoutManagerVerticalNewArrival.setOrientation(RecyclerView.VERTICAL);
        newArrivalVerticalRecyclerview.setLayoutManager(layoutManagerVerticalNewArrival);

        simpleVerticalModelList = new ArrayList<>();

        Gson gson = new Gson();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        String json = appSharedPrefs.getString("basket", "");
        Type type = new TypeToken<List<SimpleVerticalModel>>(){}.getType();
        List<SimpleVerticalModel> basket = gson.fromJson(json, type);

        if(basket == null){
            basket = new ArrayList<>();
        }

        simpleVerticalModelList = basket;
        if(simpleVerticalModelList.size() ==0){
            add_order.setVisibility(View.GONE);
        }

        basketVerticalAdapter = new BasketVerticalAdapter(simpleVerticalModelList,getContext());
        newArrivalVerticalRecyclerview.setAdapter(basketVerticalAdapter);
        basketVerticalAdapter.notifyDataSetChanged();

        add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addOrder();
            }
        });
    }

    void addOrder(){
        Gson gson = new Gson();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        String json = appSharedPrefs.getString("basket", "");

        Type type = new TypeToken<List<SimpleVerticalModel>>(){}.getType();
        List<SimpleVerticalModel> basket = gson.fromJson(json, type);

        if(basket == null){
            basket = new ArrayList<>();
        }
        if(basket.size() == 0){
            Toast.makeText(getContext(), "Nothing found in the basket", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalPrice = 0;
        for (int i=0;i<basket.size();i++){
            totalPrice += (basket.get(i).getCart_qty() * Integer.parseInt(basket.get(i).getSimple_quantity().substring(1).trim()));
        }

        if (!sessionManager.isLogin()){
            Toast.makeText(getContext(), "You are not logged in. Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), EmailLoginMainActivity.class));
        }else{
            Intent intent = new Intent(getContext(), ShippingDetails.class);
            intent.putExtra("price",totalPrice+"");
            startActivity(intent);
        }
    }



    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        navigationBar = (ImageView) view.findViewById(R.id.navigationBar);

        login =(TextView) view.findViewById(R.id.login);
        logout =(TextView) view.findViewById(R.id.signout__);
        bookmarks =(RelativeLayout) view.findViewById(R.id.relativeLayout3);
        basket =(RelativeLayout) view.findViewById(R.id.relativeLayout4);
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


    private void Logout() {

        sessionManager.editor.clear();
        sessionManager.editor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        Animatoo.animateSwipeRight(getContext());

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

}