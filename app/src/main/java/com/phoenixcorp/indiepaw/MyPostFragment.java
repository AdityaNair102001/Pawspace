package com.phoenixcorp.indiepaw;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPostFragment newInstance(String param1, String param2) {
        MyPostFragment fragment = new MyPostFragment();
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

    RecyclerView myPostList;
    CircularProgressIndicator pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypost, container, false);

        myPostList = view.findViewById(R.id.myPosts);
        pd = view.findViewById(R.id.progressBarMyPosts);

        pd.setVisibility(View.VISIBLE);

        ArrayList<String> documentID = new ArrayList<>();

        HashMap<String, String> imageUrlsFromDB=new HashMap<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.collection("Users").document(currentUser).collection("my posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                        documentID.add(documentSnapshot.getId());

                        db.collection("Users").document(currentUser).collection("my posts").document(documentSnapshot.getId()).collection("urls").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<DocumentSnapshot> documentList = task.getResult().getDocuments();

                                        imageUrlsFromDB.put(documentSnapshot.getId(),documentList.get(documentList.size()-1).getString("url"));

                                        adapterHandler(imageUrlsFromDB,documentID,pd);
                                    }
                                });
                    }
                }
            }
        });
        


    return view;
    }

    private void adapterHandler(HashMap<String,String>imageUrls,ArrayList<String>documentID, CircularProgressIndicator pd) {


        pd.setVisibility(View.INVISIBLE);
        MyPostListAdapter adapter=new MyPostListAdapter(imageUrls,documentID, this);
        myPostList.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        myPostList.setItemViewCacheSize(20);
        myPostList.setDrawingCacheEnabled(true);
        myPostList.setAdapter(adapter);
    }
    }
