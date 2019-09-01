package pw.sadbd.tourpatron.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import pw.sadbd.tourpatron.PojoClass.User;
import pw.sadbd.tourpatron.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberHolder> {
    private Context context;
    private List<String> userIdList;

    public MemberAdapter(Context context, List<String> userIdList) {
        this.context = context;
        this.userIdList = userIdList;
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.row_member_recycler, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberHolder holder, int position) {
        DatabaseReference userref =
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("User")
                        .child(userIdList.get(position));
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final User user = dataSnapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getImgURL())
                        .into(holder.userImg);
                holder.userName.setText(user.getName());
                holder.userEmailOrPhone.setText(user.getPhone());
                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getPhone()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userIdList.size();
    }
}
class MemberHolder extends RecyclerView.ViewHolder {

    CircleImageView userImg;
    TextView userName;
    TextView userEmailOrPhone;
    CardView cardView;
    ImageButton imageButton;
    public MemberHolder(View itemView) {
        super(itemView);
        userImg = itemView.findViewById(R.id.row_member_image);
        userName = itemView.findViewById(R.id.member_row_name);
        userEmailOrPhone = itemView.findViewById(R.id.row_userEmail);
        cardView = itemView.findViewById(R.id.membercard);
        imageButton = itemView.findViewById(R.id.imageButton);
    }
}