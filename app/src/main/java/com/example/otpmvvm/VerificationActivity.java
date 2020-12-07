package com.example.otpmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button verify_btn;
    ProgressBar progressBar;
    EditText otp;
    String phoneNo;
    String otpSentBySystem;
    TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verify_btn = findViewById(R.id.verify_btn);
        otp= findViewById(R.id.otp);
        progressBar = findViewById(R.id.progress_bar);
        resend=findViewById(R.id.resendTv);
        mAuth=FirebaseAuth.getInstance();

        phoneNo=getIntent().getStringExtra("number");

        sendVerificationCodeToUser(phoneNo);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("OTP");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNo,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            otpSentBySystem=s;
            Log.d("OTP", "onCodeSent:" + s);
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (otp!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(VerificationActivity.this,HomeActivity.class));
                            finish();
                            // ...
                        } else {
                            Toast.makeText(VerificationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpSentBySystem, codeByUser);
        signInWithPhoneAuthCredential(credential);
    }
    public void verifyCode(View view) {
        if (otp.getText().toString()!=null&otp.getText().toString().length()==6){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpSentBySystem, otp.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }
        else{
            Toast.makeText(VerificationActivity.this,"Enter Correct OTP",Toast.LENGTH_SHORT).show();
        }
    }

    public void resendOTP(View view) {
        sendVerificationCodeToUser(phoneNo);
    }
}