package com.example.uzvend.EmailLoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.uzvend.HomeActivity;
import com.example.uzvend.MainActivity;
import com.example.uzvend.OperationRetrofitApi.ApiClient;
import com.example.uzvend.OperationRetrofitApi.ApiInterface;
import com.example.uzvend.OperationRetrofitApi.Users;
import com.example.uzvend.R;
import com.example.uzvend.Sessions.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLoginMainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btnLogin;
    public static ApiInterface apiInterface;
    String  user_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login_main);

        /// status bar hide starts ///

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /// status bar ends ///

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);

        init();
    }

    private void init() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.button2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

    }

    private void Login() {

        String user_email = email.getText().toString().trim();
        String user_password = password.getText().toString().trim();

        if(TextUtils.isEmpty(user_email))
        {
            email.setError("Email is required!");
        } else if(TextUtils.isEmpty(user_password))
        {
            password.setError("Password is required!");
        } else
        {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Logging...");
            dialog.setMessage("Please  wait while we are checking your credentials");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);


            Call<Users> call = apiInterface.performEmailLogin(user_email, user_password);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if(response.body().getResponse().equals("ok"))
                    {

                          user_id = response.body().getUserId();
                        String username = response.body().getUsername();
                          sessionManager.createSession(user_id, username);

                          Intent intent = new Intent(EmailLoginMainActivity.this, HomeActivity.class);
                          startActivity(intent);
                          finish();
                          Animatoo.animateSwipeLeft(EmailLoginMainActivity.this);

                          dialog.dismiss();
                    }
                   else  if(response.body().getResponse().equals("no_account"))
                    {
                        Toast.makeText(EmailLoginMainActivity.this, "No Account Found!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(EmailLoginMainActivity.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }

    public void goToRegister(View view) {
        Intent intent = new Intent(EmailLoginMainActivity.this,EmailRegisterMainActivity.class);
        startActivity((intent));
        Animatoo.animateSlideRight(this);
        finish();
    }

    public void backToMainPage(View view) {
        Intent intent = new Intent(EmailLoginMainActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(this);
        finish();
    }
}