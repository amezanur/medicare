package com.example.medicare;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;

public class SignIn extends AppCompatActivity {

    private static final int REQUEST_CODE = 101010;
    EditText email, password;
    Button btnSignIn;
    ImageView fingerprint;
    TextView textViewFingerprint, textViewOR;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        password = (MaterialEditText) findViewById(R.id.password);
        email = (MaterialEditText) findViewById(R.id.email);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        fingerprint = (ImageView) findViewById(R.id.fingerprint);
        textViewFingerprint = (TextView) findViewById(R.id.textViewFingerprint);
        textViewOR = (TextView) findViewById(R.id.textViewOR);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String em = email.getText().toString();
                String pass = password.getText().toString();

                if(TextUtils.isEmpty(em)) {
                    Toast.makeText(SignIn.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignIn.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                PerformAuth(em, pass);
            }
        });


        sharedPreferences =  getSharedPreferences("data", MODE_PRIVATE);
        boolean isLogin=sharedPreferences.getBoolean("isLogin", false);
        if (isLogin)
        {
            textViewOR.setVisibility(View.VISIBLE);
            textViewFingerprint.setVisibility(View.VISIBLE);
            fingerprint.setVisibility(View.VISIBLE);
        }

        //Fingerprint sensor
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Fingerprint sensor does not exist", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Sensor not available", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SignIn.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                String em = sharedPreferences.getString("email", "");
                String pass = sharedPreferences.getString("password", "");

                PerformAuth(em, pass);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        fingerprint.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

    }

    private void PerformAuth(String em, String pass) {

        progressDialog.setMessage("Sign In");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putString("email", em);
                            editor.putString("password", pass);
                            editor.putBoolean("isLogin", true);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
