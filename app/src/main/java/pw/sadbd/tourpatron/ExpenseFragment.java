package pw.sadbd.tourpatron;


import android.app.ProgressDialog;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pw.sadbd.tourpatron.Adapters.ExpenseAdapter;
import pw.sadbd.tourpatron.Interface.EventHandelActivityToFragmentListiner;
import pw.sadbd.tourpatron.PojoClass.EventDetails;
import pw.sadbd.tourpatron.PojoClass.Expence;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.FragmentExpenseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ExpenseFragment extends Fragment implements EventHandelActivityToFragmentListiner{


    private FragmentExpenseBinding binding;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private DatabaseReference databaseReference;
    private DatabaseReference eventRef;
    private ProgressDialog dialog;
    private List<Expence> expenceList = new ArrayList<>();
    private float totalCost =0f;
    private String budget_s ="0";

    private DatabaseReference expenseRef;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expense,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dialog = new ProgressDialog(getActivity());
       /* dialog.setMessage("Loading..");
        dialog.show();*/
        databaseReference.child("Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!StaticData.eventID.equals("NA")) {
                    asyncRecycler(StaticData.eventID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void asyncRecycler(String id){
        eventRef = databaseReference.child("Event").child(id);
        eventRef.child("Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        EventDetails eventDetails = dataSnapshot.getValue(EventDetails.class);
                        try {
                            budget_s = eventDetails.getEventBudet();
                        }catch (Exception e){}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        expenseRef  = eventRef.child("Expense");
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenceList.clear();
                totalCost = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Expence expence = d.getValue(Expence.class);
                    totalCost += Float.parseFloat(expence.getCostName());
                    expenceList.add(expence);
                }
                binding.budgetTV.setText("Budget: " + budget_s + " .TK");
                binding.totalCostTV.setText("Total Cost: " + String.valueOf(totalCost) + " .TK");

                float remain = Float.parseFloat(budget_s) - totalCost;
                binding.RemainingCostTV.setText("Remaining: " + String.valueOf(remain) + " .TK");
                recyclerView = binding.expenseRecycler;
                adapter = new ExpenseAdapter(getActivity(), expenceList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

               // dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getActivityStatus(String status) {
        final String id = StaticData.eventID;
        if (!id.equals("NA")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.add_expence_alertdialog, null);
            builder.setView(view);
            final EditText expName = view.findViewById(R.id.expenseNmae);
            final EditText costName = view.findViewById(R.id.costNameA);
            Button closebtn = view.findViewById(R.id.closeButton);
            Button addbtn = view.findViewById(R.id.expenseAddbutton);
            final AlertDialog alertDialog = builder.show();
            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = expName.getText().toString();
                    String cost = costName.getText().toString();
                    if (name.isEmpty()) {
                        expName.setError(getString(R.string.required_field));
                    }
                    if (cost.isEmpty()) {
                        costName.setError(getString(R.string.required_field));
                    }
                    if (!name.isEmpty() && !cost.isEmpty()) {

                        dialog.setMessage("Please Wait..");
                        dialog.show();


                        eventRef = databaseReference.child("Event").child(id);
                        DatabaseReference expenseRef = eventRef.child("Expense");
                        DatabaseReference expush = expenseRef.push();
                        String phKey = expush.getKey();
                        Expence expence = new Expence(phKey, name, cost);
                        expush.setValue(expence).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    alertDialog.dismiss();
                                    if (StaticData.eventID != null) {
                                        asyncRecycler(StaticData.eventID);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                        alertDialog.dismiss();
                        Toast.makeText(getActivity(), "Event is not save ", Toast.LENGTH_SHORT).show();
                    }
                }


            });
        }else {
            Toast.makeText(getActivity(), "Event is not save", Toast.LENGTH_SHORT).show();
        }


    }

   /* @Override
    public void onExpenseDelete(String id) {
        Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
        expenseRef.child(id).removeValue();
    }*/
}
