package pw.sadbd.tourpatron;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pw.sadbd.tourpatron.Adapters.MomentAdapter;
import pw.sadbd.tourpatron.Interface.EventHandelActivityToFragmentListiner;
import pw.sadbd.tourpatron.PojoClass.Moment;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.FragmentMomentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MomentFragment extends Fragment implements EventHandelActivityToFragmentListiner {

    private FragmentMomentBinding binding;
    private RecyclerView recyclerView;
    private MomentAdapter adapter;
    private DatabaseReference momentRef;
    private List<Moment> momentList =new ArrayList<>();
    public MomentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_moment,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        momentRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Event")
                .child(StaticData.eventID)
                .child("Moments");
        momentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                momentList.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Moment moment = d.getValue(Moment.class);
                    momentList.add(moment);
                }
                recyclerView = binding.momentRecycler;
                adapter = new MomentAdapter(getActivity(), momentList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void getActivityStatus(String status) {
        if(!StaticData.eventID.equals("NA")) {
            BottomSheetMomentFragment momentFragment = new BottomSheetMomentFragment();
            momentFragment.show(getChildFragmentManager(), "moment");
        }else {
            Toast.makeText(getActivity(), "Event is not save", Toast.LENGTH_SHORT).show();
        }
    }
}
