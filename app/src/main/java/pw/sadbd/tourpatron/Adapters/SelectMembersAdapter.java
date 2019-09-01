package pw.sadbd.tourpatron.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import pw.sadbd.tourpatron.PojoClass.User;
import pw.sadbd.tourpatron.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectMembersAdapter extends RecyclerView.Adapter<SelectHolder> {
    private List<User> userList;
    private Context context;
    private String eventID;
    private DatabaseReference eventRef;
    private DatabaseReference keyphush;
    private String[] keylist = new String[300];
    private ArrayList<String> oldselectuser;
    private List<Boolean> checklist = new ArrayList<>();

    public SelectMembersAdapter(Context context, List<User> userList, String eventID, ArrayList<String> uids) {
        this.context = context;
        this.userList = userList;
        this.eventID = eventID;
        this.oldselectuser = uids;
    }

    @NonNull
    @Override
    public SelectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_member_row_member_recycler,parent,false);
        return new SelectHolder(view,oldselectuser,userList);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectHolder holder, final int position) {

        eventRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Event")
                .child(eventID)
                .child("MemberList");
        final User user = userList.get(position);
        if(oldselectuser.toString().contains(user.getUid())){
            holder.checkBox.setChecked(true);
            keylist[position]=user.getUid();

        }else {
            keylist[position]="NA";
        }


        Picasso.get()
                .load(user.getImgURL())
                .into(holder.circleImageView);
        holder.name.setText(user.getName());
        holder.emailorPhone.setText(user.getPhone());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                }else {
                    holder.checkBox.setChecked(true);
                }
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // Toast.makeText(context, keylist, Toast.LENGTH_SHORT).show();
                if(holder.checkBox.isChecked()){
                    keyphush = eventRef.push();
                    String key = keyphush.getKey();
                    keyphush.child("UserID").setValue(user.getUid());
                    keyphush.child("memberid").setValue(key);
                    keylist[position]=user.getUid();
                }
                else if(!holder.checkBox.isChecked() ){
                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot d:dataSnapshot.getChildren()){
                                HashMap<String,String> map = (HashMap<String, String>) d.getValue();
                                String uid = map.get("UserID");
                                String key_ = map.get("memberid");
                                if(!holder.checkBox.isChecked() && uid.equals(keylist[position])){
                                    try {
                                        eventRef.child(key_).removeValue();
                                        keylist[position]="NA";
                                    }catch (Exception e){}

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
class SelectHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    CircleImageView circleImageView;
    TextView name;
    TextView emailorPhone;
    CheckBox checkBox;
    public SelectHolder(View itemView, ArrayList<String> oldselectuser, List<User> userList) {
        super(itemView);
        cardView = itemView.findViewById(R.id.bootom_rec_card);
        circleImageView = itemView.findViewById(R.id.row_member_image_bottom);
        name = itemView.findViewById(R.id.member_row_name_bottom);
        emailorPhone = itemView.findViewById(R.id.row_userEmail_bottom);
        checkBox = itemView.findViewById(R.id.checkBox_bottom);


    }


}
