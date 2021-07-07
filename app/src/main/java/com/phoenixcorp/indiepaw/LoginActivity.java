package com.phoenixcorp.indiepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    Button signup,login;
    GifImageView gif;
    TextView signinText,greetings;
    TextInputLayout email, password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing all the variables

        signup = findViewById(R.id.SignUp);
        login = findViewById(R.id.Login);
        gif = findViewById(R.id.gifImageView);
        signinText = findViewById(R.id.textView2);
        greetings = findViewById(R.id.Greetings);
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.PasswordTextField);

        auth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);

                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(gif,"gifImageView2");
                pairs[1] = new Pair<View,String>(greetings,"text_animation");
                pairs[2] = new Pair<View,String>(signinText,"textview2_trans");
                pairs[3] = new Pair<View,String>(email,"email_trans");
                pairs[4] = new Pair<View,String>(password,"password_trans");
                pairs[5] = new Pair<View,String>(login,"button1_trans");
                pairs[6] = new Pair<View,String>(signup,"button2_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String emailText = email.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();

                if(passwordText.isEmpty()){
                    password.setError("Field cannot be empty");
                }
                else if( emailText.isEmpty()){
                    email.setError("Field cannot be empty");
                }
                else{
                    loginUser(emailText,passwordText);
                }

            }
        });
    }

    private void loginUser(String emailText, String passwordText) {



        auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();

//                    String UID=auth.getCurrentUser().getUid();
//
//                    String url = "https://api.mesibo.com/api.php?token=a2s0tmtyfsvwz9oitxs0ikjtgjq4wgt4zddk35dfnc769xfcr1z39n1ocnl19do1&op=useradd&appid=com.phoenixcorp.indiepaw&addr="+UID;
//
//                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//
//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    Log.e("mesibo!!!!!!!!!!!", "onResponse: "+response);
//                                }
//                            }, new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    // TODO: Handle error
//                                    Log.e("mesibo!!!!!!!!!", "onResponse: "+error);
//                                }
//                            });
//
//                    queue.add(jsonObjectRequest);



                    Intent intent = new Intent(LoginActivity.this, DefaultPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    email.setError("");
                    password.setError("");
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}