package com.phoenixcorp.indiepaw;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.lang.Character.compare;

public class AddNewPostFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    SliderView sliderView;             //int[] images = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six};

    ArrayList<Uri> images;
    Button upload,post;
    EditText breed, age, location, description;

    FirebaseFirestore db;
    ProgressDialog pd;

    public AddNewPostFragment() {
        // Required empty public constructor
    }
    public static AddNewPostFragment newInstance(String param1, String param2) {
        AddNewPostFragment fragment = new AddNewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_new_post, container, false);


        images = new ArrayList<>();
        sliderView = view.findViewById(R.id.imageSlider);
        upload = view.findViewById(R.id.upload);
        post = view.findViewById(R.id.post);
        breed = view.findViewById(R.id.breed);
        age = view.findViewById(R.id.age);
        location = view.findViewById(R.id.location);
        description = view.findViewById(R.id.description);

        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(getContext());
        pd.setCancelable(false);


        upload.setOnClickListener(v-> {
            if(ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setType("image/*");
            startActivityForResult(intent,1);

        });

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        post.setOnClickListener(v->{
            String breedVal = breed.getText().toString().trim();
            String ageVal = age.getText().toString().trim();
            String locationVal = location.getText().toString().trim();
            String descriptionVal = description.getText().toString().trim();

            final String timeStamp = Long.toString(System.currentTimeMillis());
            final String documentID = timeStamp+currentUser;

            if(images.isEmpty()){

                Toast.makeText(getActivity(),"Please select images.",Toast.LENGTH_LONG).show();

            }
            else{

                if(TextUtils.isEmpty(locationVal)){
                    location.setError("Location is required");
                    return;
                   // Toast.makeText(getActivity(), "Location is required", Toast.LENGTH_SHORT).show();
                }
                if(descriptionWordCount(descriptionVal)>=20){
                    description.setError("Word limit of 20");
                    return;
                }
                else{
                    uploadImages(db,pd,documentID,currentUser);
                    uploadData(db,pd,documentID,currentUser,breedVal,ageVal,locationVal,descriptionVal);
                }
            }
        });




        return view;
    }

    private int descriptionWordCount(String descriptionVal) {
        int i =0;
        for(int j=0;j<descriptionVal.length();j++){
            if(compare(descriptionVal.charAt(j),' ')==0)
            {i++;}
        }
      return i;
    }


    private void uploadImages(FirebaseFirestore db, ProgressDialog pd, String documentID, String currentUser) {

        pd.setMessage("Uploading Images");
        pd.show();

        StorageReference ImageFolder= FirebaseStorage.getInstance().getReference().child("Images");

        for(int i=0;i<images.size();i++){

            Uri individualImage=images.get(i);
            StorageReference imageName= ImageFolder.child("image"+individualImage.getLastPathSegment());

            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url= String.valueOf(uri);

                            Log.d("URL", "onSuccess: "+url);

                            Map<String,Object> urlSet=new HashMap<>();
                            urlSet.put("url",url);

                            db.collection("posts").document(documentID).collection("urls").add(urlSet)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Toast.makeText(getActivity(), "Images Uploaded", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(getActivity(),DefaultPageActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Couldn't Post "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            db.collection("Users").document(currentUser).collection("my posts").document(documentID).collection("urls").add(urlSet)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Images added to my posts", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    });
                }
            });

        }

    }


    private void uploadData(FirebaseFirestore db, ProgressDialog pd, String documentID, String currentUser, String breedVal, String ageVal, String locationVal, String descriptionVal) {


        Map<String,Object> postData=new HashMap<>();
        postData.put("Breed",breedVal);
        postData.put("Age",ageVal);
        postData.put("Description",descriptionVal);
        postData.put("location",locationVal);
        postData.put("UID",currentUser);

        db.collection("posts").document(documentID).set(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Data Posted",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(),"Couldn't post."+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });


        db.collection("Users").document(currentUser).collection("my posts").document(documentID).set(postData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Added to my posts", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    private void adapterHandler(ArrayList<Uri> images) {

        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK ){
            List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData=data.getClipData();

            if(clipData!=null){
                for(int i=0;i<clipData.getItemCount();i++){
                    Uri imageURI=clipData.getItemAt(i).getUri();
                    images.add(imageURI);
                    adapterHandler(images);
                }
            }else{
                Uri imageURI=data.getData();
                images.add(imageURI);
                adapterHandler(images);
            }

        }
    }

}