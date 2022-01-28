package com.phoenixcorp.indiepaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class MyPostsDescriptionActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;

    ArrayList<String> images;
    ArrayList<String> reverseImages;

    TextView description,username;

    Chip breed,age,vaccine,location,gender;

    String breedText,ageText,vaccineText,locationText,genderText,documentID,descriptionText,usernameText;

    private Toolbar mToolbar;


    SliderView postImageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts_description);

        mToolbar = findViewById(R.id.myPostDescriptionToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        images = new ArrayList<>();
        reverseImages = new ArrayList<>();

        breed = findViewById(R.id.breed);
        age = findViewById(R.id.age);
        vaccine = findViewById(R.id.vaccine);
        location = findViewById(R.id.location);
        gender = findViewById(R.id.gender);
        description=findViewById(R.id.Description);
        username = findViewById(R.id.Username);

        postImageSlider = findViewById(R.id.imageSlider);

        breedText = getIntent().getStringExtra("Breed");
        ageText = getIntent().getStringExtra("Age");
        vaccineText = getIntent().getStringExtra("Vaccine");
        locationText = getIntent().getStringExtra("Location");
        genderText = getIntent().getStringExtra("Gender");
        documentID = getIntent().getStringExtra("DocumentID");
    //    descriptionText = getIntent().getStringExtra("Description");

        breed.setText(breedText);
        age.setText(ageText);
        vaccine.setText(vaccineText);
        location.setText(locationText);
        gender.setText(genderText);
    //    description.setText(descriptionText);

        final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.collection("Users").document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usernameText = documentSnapshot.getString("fullname");
                username.setText(usernameText);
            }
        });

        db.collection("Users").document(currentUser).collection("my posts").document(documentID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       descriptionText = documentSnapshot.getString("Description");
                       description.setText(descriptionText);
                    }
                });

        db.collection("posts").document(documentID).collection("urls").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                    String imagStr = snapshot.getString("url");
                    images.add(imagStr);
                }
                for(int i=images.size()-1;i>=0;i--){
                    reverseImages.add(images.get(i));
                }
                adapterHandler(reverseImages);
            }

            private void adapterHandler(ArrayList<String> reverseImages) {
                MyPostDescriptionSliderAdapter adapter = new MyPostDescriptionSliderAdapter(images);
                postImageSlider.setSliderAdapter(adapter);

            }
        });




    }
}