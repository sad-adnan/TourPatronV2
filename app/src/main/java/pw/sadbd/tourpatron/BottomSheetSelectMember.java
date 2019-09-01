package pw.sadbd.tourpatron;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pw.sadbd.tourpatron.Adapters.SelectMembersAdapter;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.PojoClass.User;
import pw.sadbd.tourpatron.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetSelectMember extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private SelectMembersAdapter adapter;
    private DatabaseReference userRef;
    private List<User> userList = new ArrayList<>();
    private FirebaseAuth auth;
    private String myUid="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.bottom_sheet_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        myUid  =auth.getCurrentUser().getUid();


        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        recyclerView = view.findViewById(R.id.bottomRecycler);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    User user = d.getValue(User.class);
                    if(!user.getUid().equals(myUid)){
                        userList.add(user);
                    }

                }

                   ArrayList<String> uids= getArguments().getStringArrayList("eventuserlist");

                adapter = new SelectMembersAdapter(getActivity(),userList, StaticData.eventID,uids);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
