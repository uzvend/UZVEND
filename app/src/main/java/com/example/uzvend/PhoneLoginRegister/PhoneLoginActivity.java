package com.example.uzvend.PhoneLoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.uzvend.HomeActivity;
import com.example.uzvend.MainActivity;
import com.example.uzvend.OperationRetrofitApi.ApiClient;
import com.example.uzvend.OperationRetrofitApi.ApiInterface;
import com.example.uzvend.OperationRetrofitApi.Users;
import com.example.uzvend.R;
import com.example.uzvend.Sessions.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneLoginActivity extends AppCompatActivity {
    private EditText phone,otp;
    private Button btnOtp,btnLogin;
    public static ApiInterface apiInterface;
    String user_id;

    /// phone otp ///
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    //////////////////

    ImageView dialog;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        /// status bar hide starts ///
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /// status bar ends ///

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        init();
    }

    private void init() {
        phone = (EditText) findViewById(R.id.phone);
        otp = (EditText) findViewById(R.id.otp);
        btnOtp = (Button) findViewById(R.id.button3);
        btnLogin = (Button) findViewById(R.id.button2);

        ///progress dialog loading starts ///
        dialog = (ImageView) findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.loader).into(dialog);
         /// progress dialog loading end ///

        /// phone OTP callback start///
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                dialog.setVisibility(View.GONE);
                otp.setVisibility(View.GONE);
                phone.setVisibility(View.VISIBLE);
                btnOtp.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();

            }
            public void onCodeSent (String verificationId,
                                    PhoneAuthProvider.ForceResendingToken token)
            {
                /// save verification ID and resending token so we can use them later ///
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(PhoneLoginActivity.this, "Code has been sent, please check and verify", Toast.LENGTH_SHORT).show();

                otp.setVisibility(View.VISIBLE);
                phone.setVisibility(View.GONE);
                btnOtp.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
                dialog.setVisibility(View.GONE);
            }
        };

        /// phone OTP callback end///



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_phone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(user_phone))
                {
                    phone.setError("Phone is required!");
                }
                if (user_phone.length()!=10)
                {
                    phone.setError(("Phone should be of 10 digit!"));
                }
                else {
                    dialog.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            ///Phone number to verify///
                            "+99" + user_phone, // 8 code(93,94,90,91 etc) number(xxx xx xx) <=> (8 + prefix code + number)

                            ///timeout duration///
                            60,
                            /// Unit of Time ///
                            TimeUnit.SECONDS,
                            /// Activity for callback binding
                            PhoneLoginActivity.this,
                            ///onVerificationStateChangedCallBacks
                            callbacks);
                }
            }
        });
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getText().toString().equals(""))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Please enter your 6 digit otp", Toast.LENGTH_SHORT).show();
                }
                if(otp.getText().toString().length() !=6)
                {
                    Toast.makeText(PhoneLoginActivity.this, "Invalid otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialog.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,otp.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void Login() {

        Call<Users> call = apiInterface.performPhoneLogin(phone.getText().toString());
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if (response.body().getResponse().equals("ok"))
                {
                    user_id = response.body().getUserId();
                    sessionManager.createSession(user_id, response.body().getUsername());

                    Intent intent = new Intent(PhoneLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSwipeLeft(PhoneLoginActivity.this);

                    dialog.setVisibility(View.GONE);
                }
                else if (response.body().getResponse().equals("no_account"))
                {
                    Toast.makeText(PhoneLoginActivity.this, "No Account Found!", Toast.LENGTH_SHORT).show();
                    dialog.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(PhoneLoginActivity.this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Login();
                        }
                        else
                        {
                            dialog.setVisibility(View.GONE);
                        }

                    }
                });
    }


    public void goToRegister(View view) {

        Intent intent = new Intent(PhoneLoginActivity.this,PhoneRegisterActivity.class);
        startActivity(intent);
        Animatoo.animateSlideDown(this);
        finish();
    }

    public void backToMainPage(View view) {
            Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
            startActivity(intent);
            Animatoo.animateSlideRight(this);
            finish();
        }

}