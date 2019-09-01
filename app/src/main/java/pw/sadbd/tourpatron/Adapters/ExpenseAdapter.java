package pw.sadbd.tourpatron.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import pw.sadbd.tourpatron.PojoClass.Expence;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenceHolder> {
    private Context context;
    private List<Expence> expenceList;


    private DatabaseReference databaseReference;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    private DatabaseReference eventRef;
    public ExpenseAdapter(Context context, List<Expence> expenceList) {
        this.context = context;
        this.expenceList = expenceList;

    }

    @NonNull
    @Override
    public ExpenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenceHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_expence_recycler,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenceHolder holder, final int position) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = databaseReference.child(currentUser.getUid());
        eventRef = FirebaseDatabase.getInstance().getReference().child("Event").child(StaticData.eventID);

        holder.costname.setText(expenceList.get(position).getExpenseName());
        holder.costTK.setText(expenceList.get(position).getCostName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventRef.child("Expense").child(expenceList.get(position).getExpenseID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Not Remove", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
               
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenceList.size();
    }

    public class ExpenceHolder extends RecyclerView.ViewHolder {

        TextView costname;
        TextView costTK;
        ImageButton delete;
        public ExpenceHolder(View itemView) {
            super(itemView);
            costname = itemView.findViewById(R.id.row_costName);
            costTK = itemView.findViewById(R.id.row_costTK);
            delete = itemView.findViewById(R.id.row_menu);
        }
    }

}


