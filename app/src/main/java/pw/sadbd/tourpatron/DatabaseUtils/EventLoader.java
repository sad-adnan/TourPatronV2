package pw.sadbd.tourpatron.DatabaseUtils;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;

import pw.sadbd.tourpatron.Interface.LoadEventListiner;
import pw.sadbd.tourpatron.PojoClass.Event;
import pw.sadbd.tourpatron.PojoClass.EventMember;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventLoader {
    private Context context;
    private FirebaseAuth auth;

    private List<Event> eventList = new ArrayList<>();
    private List<EventMember> memberList = new ArrayList<>();
    private ProgressDialog dialog;
    private FirebaseUser user;
    private DatabaseReference eventRef;

    private LoadEventListiner listiner;

    public EventLoader(Context context, FirebaseAuth auth) {
        this.context = context;
        this.auth = auth;
        this.dialog = new ProgressDialog(context);
        this.auth = auth;
        this.eventRef = FirebaseDatabase.getInstance().getReference().child("Event");
        this.user = auth.getCurrentUser();
    }

    private void firebaseEventget(){
        dialog.setMessage("Loading..");
        dialog.show();
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();
                for(DataSnapshot d :dataSnapshot.getChildren()){
                    Event eventDetailsObj = d.getValue(Event.class);

                    memberList.clear();
                    for(DataSnapshot dd :d.child("MemberList").getChildren()){
                        EventMember member = dd.getValue(EventMember.class);
                        memberList.add(member);
                    }

                    if(memberList.toString().contains(user.getUid().toString())){
                        eventList.add(eventDetailsObj);
                    }

                }

                getListiner().onComplete(eventList);
                dialog.dismiss();
                //recyclerAdapterSet();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getListiner().onError(databaseError.getMessage());
                dialog.dismiss();
            }
        });

    }

    private LoadEventListiner getListiner() {
        return listiner;
    }

    public void setListiner(LoadEventListiner listiner) {
        this.listiner = listiner;
        firebaseEventget();
    }
}
