package pw.sadbd.tourpatron;


import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Toast;

import pw.sadbd.tourpatron.Interface.EventHandelActivityToFragmentListiner;
import pw.sadbd.tourpatron.PojoClass.EventDetails;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.FragmentEventDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EventDetailsFragment extends Fragment implements EventHandelActivityToFragmentListiner {


    private FragmentEventDetailsBinding binding;
    private Date startDate;
    private Date endDate;
    private DatabaseReference databaseReference;
    private DatabaseReference eventRefarance;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EventDetails eventDetails;
    private  SimpleDateFormat simpleDateFormat ;
    public EventDetailsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_event_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getActivity());
        eventRefarance = databaseReference.child("Event");
        auth = FirebaseAuth.getInstance();
        user =auth.getCurrentUser();
        getEventDetails();
        binding.picStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                Date date = new Date();
                DatePickerDialog datePickerDialog =
                      new DatePickerDialog(getActivity(), startDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });
        binding.picEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                Date date = new Date();
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(getActivity(), endDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });
        binding.openMapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void getEventDetails(){
        if(!StaticData.eventID.equals("NA")){
            eventRefarance
                    .child(StaticData.eventID)
                   .child("Details")
                   .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           try {


                               eventDetails = dataSnapshot.getValue(EventDetails.class);

                               binding.EventName.setText(eventDetails.getEventName());
                               binding.eventAddressET.setText(eventDetails.getEventAddress());
                               binding.eventBudgetET.setText(eventDetails.getEventBudet());
                               startDate = eventDetails.getEventStartDate();
                               endDate = eventDetails.getEventEndDate();
                               String start =
                                       simpleDateFormat.format(startDate.getTime());
                               String end =
                                       simpleDateFormat.format(endDate.getTime());

                               binding.picStartDate.setText(start);
                               binding.picEndDate.setText(end);
                           }catch (Exception e){}

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
        }

    }
    private  DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DATE,i2);
            String date =simpleDateFormat.format(calendar.getTime());
            startDate = calendar.getTime();
            binding.picStartDate.setText(date);
        }
    };
    private  DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            simpleDateFormat =
                    new SimpleDateFormat("dd MMM yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DATE,i2);
            String date =simpleDateFormat.format(calendar.getTime());
            endDate = calendar.getTime();
            binding.picEndDate.setText(date);
        }
    };

    @Override
    public void getActivityStatus(String status) {
       String eventName = binding.EventName.getText().toString();
       String eventAddress = binding.eventAddressET.getText().toString();
       String budget = binding.eventBudgetET.getText().toString();
       if(eventName.isEmpty()){
           binding.EventName.setError(getString(R.string.required_field));
       }
        if(eventAddress.isEmpty()){
            binding.eventAddressET.setError(getString(R.string.required_field));
        }
        if(budget.isEmpty()){
            binding.eventBudgetET.setError(getString(R.string.required_field));
        }
        if(!eventName.isEmpty() &&
                !eventAddress.isEmpty() &&
                ! budget.isEmpty() &&
                startDate !=null &&
                endDate !=null
        ){
            progressDialog.setMessage("PleaseWait..");
            progressDialog.show();
            if (eventDetails !=null){

                DatabaseReference eventid = eventRefarance.child(eventDetails.getEventId());
                String id = eventid.getKey();
                EventDetails eventObj = new EventDetails(id,
                        eventName,
                        eventAddress,
                        budget,
                        startDate,
                        endDate
                );
                eventid.child("Details").setValue(eventObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Check Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
               // eventid.child("MemberList").push().child("UserID").setValue(user.getUid());



            }else {
                DatabaseReference eventid = eventRefarance.push();
                String id = eventid.getKey();
                EventDetails eventObj = new EventDetails(id,
                        eventName,
                        eventAddress,
                        budget,
                        startDate,
                        endDate
                );
                StaticData.eventID = id;
                eventid.child("Details").setValue(eventObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Check Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                DatabaseReference d = eventid.child("MemberList").push();
                d.child("UserID").setValue(user.getUid());
                d.child("memberid").setValue(d.getKey());


            }




        }else {
            Toast.makeText(getActivity(), "Some field is missing", Toast.LENGTH_SHORT).show();
        }
    }
}
