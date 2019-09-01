package pw.sadbd.tourpatron;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pw.sadbd.tourpatron.PojoClass.User;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private DatabaseReference databaseReference;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private User userDetails;

    private ProgressDialog dialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(getActivity());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userRef = databaseReference.child("User").child(user.getUid());

        firebaseUserget();

        binding.profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivity(intent);

            }
        });

    }

    private void firebaseUserget() {
        dialog.setMessage("Loading..");
        dialog.show();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(User.class);

               if (userDetails != null){
                   binding.profileUsername.setText(userDetails.getName());
                   binding.profileemails.setText(userDetails.getEmail());
                   binding.prfilephone.setText(userDetails.getPhone());
                   Picasso.get().load(userDetails.getImgURL()).into(binding.circleImageView);
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });





    }
}
