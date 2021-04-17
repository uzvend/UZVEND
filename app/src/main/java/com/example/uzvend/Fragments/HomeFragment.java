package com.example.uzvend.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.example.uzvend.Adapters.BannerAdapter;
import com.example.uzvend.Adapters.GreatOffersAdapter;
import com.example.uzvend.Adapters.SimpleVerticalAdapter;
import com.example.uzvend.Constants.Constants;
import com.example.uzvend.MainActivity;
import com.example.uzvend.Models.BannerModel;
import com.example.uzvend.Models.GreatOffersModel;
import com.example.uzvend.Models.SimpleVerticalModel;
import com.example.uzvend.ProductList;
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


public class HomeFragment<recyclerViewBanner> extends Fragment implements View.OnClickListener {

    public HomeFragment() {
        // Required empty public constructor
    }
    DrawerLayout drawerLayout;
    ImageView navigationBar;
    NavigationView navigationView;
    private View view;
    private RelativeLayout bookmarks,basket;
    private TextView textView4, textView5, name_view,send_feedback, report_safety_emergency,rate_us;
    SessionManager sessionManager;
    private TextView login, logout;


    /// Banner slider Starts ////
    private RecyclerView recyclerViewBanner;
    private BannerAdapter bannerAdapter;
    private List<BannerModel> bannerModelList;
    /// Banner slider Ends ////


    /// simple vertical slider  starts///
    private RecyclerView recyclerViewSimple;
    private SimpleVerticalAdapter simpleVerticalAdapter;
    private List<SimpleVerticalModel> simpleVerticalModelList;
    ///  simple vertical slider ends ///


    ///  great offers horizontal starts///
    private RecyclerView greatGreatOffersHorizontal;
    private List<GreatOffersModel> greatOffersModel;
    private GreatOffersAdapter greatOffersAdapter;
    ///great offers horizontal ends///

    ///  great offers vertical slider starts///
    private RecyclerView greatOffersRecyclerViewVertical;
    ///   great offers vertical slider  ends ///

    ///  new arrivals horizontal slider starts///
    private RecyclerView newArrivalHorizontalRecyclerview;
    ///   new arrivals horizontal slider  ends ///

    ///  new arrivals vertical slider starts///
    private RecyclerView newArrivalVerticalRecyclerview;
    ///   new arrivals vertical slider  ends ///

    ///  UZVEND exclusive vertical slider starts///
    private RecyclerView exclusiveVerticalRecyclerview;
    ///   UZVEND exclusive vertical slider  ends ///

    ///  UZVEND exclusive horizontal slider starts///
    private RecyclerView exclusiveHorizontalRecyclerview;
    ///   UZVEND exclusive horizontal slider  ends ///


    EditText searchbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sessionManager = new SessionManager(getContext());
        onSetNavigationDrawerEvents();
        searchbar = (EditText) view.findViewById(R.id.searchbar);
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        loadData();
        //init();
        return view;
    }

    void performSearch(){
        String query = searchbar.getText().toString();
        Intent intent = new Intent(getContext(), ProductList.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    private void init() {

        /// banner model list ends///

        /// simple vertical model list starts ///
        //recyclerViewSimple = (RecyclerView) view.findViewById(R.id.recyclerViewSimple);
        LinearLayoutManager layoutManagerSimpleVerticalSlider = new LinearLayoutManager(getContext());
        layoutManagerSimpleVerticalSlider.setOrientation(RecyclerView.VERTICAL);
        //recyclerViewSimple.setLayoutManager(layoutManagerSimpleVerticalSlider);

        /// simple vertical model list ends///

        /// great offers model list starts ///
        //greatGreatOffersHorizontal = (RecyclerView) view.findViewById(R.id.recyclerViewGreatOffersHorizontal);
        LinearLayoutManager layoutManagerGreatOffers = new LinearLayoutManager(getContext());
        layoutManagerGreatOffers.setOrientation(RecyclerView.HORIZONTAL);
        //greatGreatOffersHorizontal.setLayoutManager(layoutManagerGreatOffers);

        greatOffersModel = new ArrayList<>();
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));
        greatOffersModel.add(new GreatOffersModel(R.drawable.dress2,"Dior New Collection","49 mins","70 % OFF","4.8"));

        greatOffersAdapter = new GreatOffersAdapter (greatOffersModel,getContext());
        //greatGreatOffersHorizontal.setAdapter(greatOffersAdapter);
        greatOffersAdapter.notifyDataSetChanged();
        /// great offers model list ends///




        ///  new arrival horizontal model list slider starts///
        //newArrivalHorizontalRecyclerview = (RecyclerView) view.findViewById(R.id.newArrivalHorizontalRecyclerview);
        LinearLayoutManager layoutManagerhorizonNewArrival = new LinearLayoutManager(getContext());
        layoutManagerhorizonNewArrival.setOrientation(RecyclerView.HORIZONTAL);
        //newArrivalHorizontalRecyclerview.setLayoutManager(layoutManagerhorizonNewArrival);

//        simpleVerticalModelList = new ArrayList<>();
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));

        simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
        //newArrivalHorizontalRecyclerview.setAdapter(simpleVerticalAdapter);
        simpleVerticalAdapter.notifyDataSetChanged();
        ///   new arrival horizontal model list slider  ends ///



        ///  new arrival horizontal model list slider starts///
        //exclusiveHorizontalRecyclerview = (RecyclerView) view.findViewById(R.id.exclusiveHorizontalRecyclerview);
        LinearLayoutManager layoutManagerexclusiveHorizon = new LinearLayoutManager(getContext());
        layoutManagerexclusiveHorizon.setOrientation(RecyclerView.HORIZONTAL);
        //exclusiveHorizontalRecyclerview.setLayoutManager(layoutManagerexclusiveHorizon);

        simpleVerticalModelList = new ArrayList<>();
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));
//        simpleVerticalModelList.add(new SimpleVerticalModel(R.drawable.dress,"GIORGIA ARMANI","Desirable and fashionable model","Paired 2| 13 $","50 % OFF - user code UZVEND",
//                "Designed by Armani, price 50% Off","4.8"));

        simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
        //exclusiveHorizontalRecyclerview.setAdapter(simpleVerticalAdapter);
        simpleVerticalAdapter.notifyDataSetChanged();
        ///   new arrival horizontal model list slider  ends ///


    }

    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        navigationBar = (ImageView) view.findViewById(R.id.navigationBar);

        login =(TextView) view.findViewById(R.id.login);
        logout =(TextView) view.findViewById(R.id.signout__);

        bookmarks =(RelativeLayout) view.findViewById(R.id.relativeLayout3);
        basket =(RelativeLayout) view.findViewById(R.id.relativeLayout4);

//        textView4 = (TextView) view.findViewById(R.id.textView4);
//        textView5 = (TextView) view.findViewById(R.id.textView5);
//        textView6 = (TextView) view.findViewById(R.id.textView6);
        name_view = (TextView) view.findViewById(R.id.logout);
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
        JsonObjectRequest requestForChangePass = new JsonObjectRequest(Request.Method.POST, Constants.root_url+"home.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray category = response.getJSONArray("category");

                    bannerModelList = new ArrayList<>();

                    for (int i=0;i<category.length();i++){
                        JSONArray arr = category.getJSONArray(i);
                        Log.d("id",arr.get(0).toString());
                        bannerModelList.add(new BannerModel(category.getJSONArray(i).get(2).toString(),category.getJSONArray(i).get(0).toString()));
                    }

                    recyclerViewBanner = (RecyclerView) view.findViewById(R.id.recyclerViewBanner);
                    LinearLayoutManager layoutManagerBanner = new LinearLayoutManager(getContext());
                    layoutManagerBanner.setOrientation(RecyclerView.HORIZONTAL);
                    recyclerViewBanner.setLayoutManager(layoutManagerBanner);

                    bannerAdapter = new BannerAdapter (bannerModelList,getContext());
                    recyclerViewBanner.setAdapter(bannerAdapter);



                    simpleVerticalModelList = new ArrayList<>();

                    simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
                    simpleVerticalAdapter.notifyDataSetChanged();


                    JSONArray upcoming = response.getJSONArray("upcoming");

                    ///  new great offers vertical slider starts///
                    greatOffersRecyclerViewVertical = (RecyclerView) view.findViewById(R.id.greatOffersRecyclerViewVertical);
                    LinearLayoutManager layoutManagerVerticalGreatOffers = new LinearLayoutManager(getContext());
                    layoutManagerVerticalGreatOffers.setOrientation(RecyclerView.VERTICAL);
                    greatOffersRecyclerViewVertical.setLayoutManager(layoutManagerVerticalGreatOffers);

                    simpleVerticalModelList = new ArrayList<>();

                    for (int i=0;i<upcoming.length();i++){
                        JSONArray arr = upcoming.getJSONArray(i);
                        simpleVerticalModelList.add(new SimpleVerticalModel(arr.get(0).toString(),arr.get(3).toString(),arr.get(1).toString(),arr.get(4).toString().length() > 30 ? arr.get(4).toString().substring(0, 30):arr.get(4).toString() ,"",Constants.currency+" "+arr.get(2).toString(),
                                "Designed & offered by UZVEND",""));
                    }


                    simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
                    greatOffersRecyclerViewVertical.setAdapter(simpleVerticalAdapter);
                    simpleVerticalAdapter.notifyDataSetChanged();
                    ///   new great offers vertical slider  ends ///


                    JSONArray recent = response.getJSONArray("recent");

                    newArrivalVerticalRecyclerview = (RecyclerView) view.findViewById(R.id.newArrivalVerticalRecyclerview);
                    LinearLayoutManager layoutManagerVerticalNewArrival = new LinearLayoutManager(getContext());
                    layoutManagerVerticalNewArrival.setOrientation(RecyclerView.VERTICAL);
                    newArrivalVerticalRecyclerview.setLayoutManager(layoutManagerVerticalNewArrival);

                    simpleVerticalModelList = new ArrayList<>();
                    for (int i=0;i<recent.length();i++){
                        JSONArray arr = recent.getJSONArray(i);
                        simpleVerticalModelList.add(new SimpleVerticalModel(arr.get(0).toString(),arr.get(3).toString(),arr.get(1).toString(),arr.get(4).toString().length() > 30 ? arr.get(4).toString().substring(0, 30):arr.get(4).toString() ,"",Constants.currency+" "+arr.get(2).toString(),
                                "Designed & offered by UZVEND",""));
                    }
                    simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
                    newArrivalVerticalRecyclerview.setAdapter(simpleVerticalAdapter);
                    simpleVerticalAdapter.notifyDataSetChanged();
                    ///   new arrival vertical model list slider  ends ///

                    JSONArray released = response.getJSONArray("released");

                    ///  new arrival vertical model list slider starts///
                    exclusiveVerticalRecyclerview = (RecyclerView) view.findViewById(R.id.exclusiveVerticalRecyclerview);
                    LinearLayoutManager layoutManagerExclusiveVertical = new LinearLayoutManager(getContext());
                    layoutManagerExclusiveVertical.setOrientation(RecyclerView.VERTICAL);
                    exclusiveVerticalRecyclerview.setLayoutManager(layoutManagerExclusiveVertical);


                    simpleVerticalModelList = new ArrayList<>();
                    for (int i=0;i<released.length();i++){
                        JSONArray arr = released.getJSONArray(i);
                        simpleVerticalModelList.add(new SimpleVerticalModel(arr.get(0).toString(),arr.get(3).toString(),arr.get(1).toString(),arr.get(4).toString().length() > 30 ? arr.get(4).toString().substring(0, 30):arr.get(4).toString() ,"",Constants.currency+" "+arr.get(2).toString(),
                                "Designed & offered by UZVEND",""));
                    }
                    simpleVerticalAdapter = new SimpleVerticalAdapter (simpleVerticalModelList,getContext());
                    exclusiveVerticalRecyclerview.setAdapter(simpleVerticalAdapter);
                    simpleVerticalAdapter.notifyDataSetChanged();
                    ///   new arrival vertical model list slider  ends ///
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
                headers.put("api-key", "2a5588cf-4cf3-4f1c-9548-cc1db4b54ae3");
                return headers;
            }
        };
        Volley.newRequestQueue(getContext()).add(requestForChangePass);


    }

}