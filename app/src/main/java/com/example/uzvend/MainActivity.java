package com.example.uzvend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.uzvend.Adapters.PlateAdapter;
import com.example.uzvend.EmailLoginRegister.EmailLoginMainActivity;
import com.example.uzvend.Models.PlateModel;
import com.example.uzvend.PhoneLoginRegister.PhoneLoginActivity;
import com.example.uzvend.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PlateModel> plateModelList;
    private PlateAdapter plateAdapter;
    private LinearLayout emailContinue, phoneContinue;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        /// status bar hide starts ///
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /// status bar ends ///

        phoneContinue = (LinearLayout) findViewById(R.id.linearLayout3);
        emailContinue = (LinearLayout) findViewById(R.id.linearLayout4);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setKeepScreenOn(true);
        recyclerView.setHasFixedSize(true);

        plateModelList = new ArrayList<>();
        plateModelList.add(new PlateModel(R.drawable.log2));
        plateModelList.add(new PlateModel(R.drawable.log3));
        plateModelList.add(new PlateModel(R.drawable.log4));
        plateModelList.add(new PlateModel(R.drawable.log5));
        plateModelList.add(new PlateModel(R.drawable.log6));
        plateModelList.add(new PlateModel(R.drawable.log7));

        plateAdapter = new PlateAdapter(plateModelList, this);
        recyclerView.setAdapter(plateAdapter);
        plateAdapter.notifyDataSetChanged();

        ///call for  auto scrolling starts///
        autoScroll();
        /// auto scrolling eds///

        /// continue with email ///
        emailContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EmailLoginMainActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

        /// continue with phone ///
        phoneContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

    }

   /// from here ////
    public void autoScroll() {
        final int speedScroll = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count == plateAdapter.getItemCount())
                    count = 0;
                if (count < plateAdapter.getItemCount()) {
                    recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }
    //// end of scrolling ////

    public void goToHomePage(View view) {
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSwipeLeft(this);
    }

    @Override
    protected void onStart() {
            super.onStart();
            if(sessionManager.isLogin())
            {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                Animatoo.animateSwipeLeft(this);
            }
            else
            {

            }
    }
}