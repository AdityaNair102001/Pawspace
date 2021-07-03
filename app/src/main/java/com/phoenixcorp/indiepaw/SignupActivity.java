package com.phoenixcorp.indiepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SignupActivity extends AppCompatActivity {

    Button signup, login, googleSignUp;
    GifImageView gif;
    TextView signinText,greetings;
    TextInputLayout email, password, fullName;

    String userID;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    DocumentReference documentReference;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.SignUp);
        login = findViewById(R.id.Login);
        gif = findViewById(R.id.gifImageView);
        signinText = findViewById(R.id.textView2);
        greetings = findViewById(R.id.Greetings);
        email = findViewById(R.id.emailTextField);
        password = findViewById(R.id.PasswordTextField);
        fullName = findViewById(R.id.Description);
        googleSignUp = findViewById(R.id.google_signup);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Validate and store data in firebase

                String fullNameText = fullName.getEditText().getText().toString();
                String emailText = email.getEditText().getText().toString().trim();
                String passwordText = password.getEditText().getText().toString().trim();

                registerUser(fullNameText,emailText,passwordText);


            }
        });

        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(gif,"gifImageView2");
                pairs[1] = new Pair<View,String>(greetings,"text_animation");
                pairs[2] = new Pair<View,String>(signinText,"textview2_trans");
                pairs[3] = new Pair<View,String>(email,"email_trans");
                pairs[4] = new Pair<View,String>(password,"password_trans");
                pairs[5] = new Pair<View,String>(login,"button1_trans");
                pairs[6] = new Pair<View,String>(signup,"button2_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        });



        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRequest();
                signIn();
            }
        });



    }


    private void registerUser(String fullNameText, String emailText, String passwordText) {

        if(validateName(fullNameText) && validateEmail(emailText) && validatePassword(passwordText)){

            auth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignupActivity.this,"Sign Up Successful!",Toast.LENGTH_SHORT).show();


                        //storing the data in firestore
                        userID = auth.getCurrentUser().getUid();
                        documentReference = fStore.collection("Users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullname", fullNameText);
                        user.put("email", emailText);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignupActivity.this, "Data Stored", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                       /* fStore.collection("Users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "User Added to the database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });*/
                       /* documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SignupActivity.this, "New User Data Stored", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });*/



                        //next activity
                        Intent intent = new Intent(SignupActivity.this, DefaultPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{

                        Toast.makeText(SignupActivity.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });




        }


    }

    private boolean validateName(String fullNameText) {
        if(fullNameText.isEmpty()){
            fullName.setError("Required");
            return false;
        }
        else{
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail(String emailText) {
        if(emailText.isEmpty()){
            email.setError("Required");
            return false;
        }
        else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(String passwordText) {
        if(passwordText.isEmpty()){
            password.setError("Required");
            return false;
        }
        else if(passwordText.length()<6){
            password.setError("Password is too short");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
             //   Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               //Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),DefaultPageActivity.class));
                            finish();
                        } 
                        else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Sorry! Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}