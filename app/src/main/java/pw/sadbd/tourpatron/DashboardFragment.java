package pw.sadbd.tourpatron;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import pw.sadbd.tourpatron.Adapters.ForcastAdapter;
import pw.sadbd.tourpatron.Interface.EventObjectListiner;
import pw.sadbd.tourpatron.PojoClass.Event;
import pw.sadbd.tourpatron.PojoClass.EventMember;
import pw.sadbd.tourpatron.PojoClass.Expence;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.WeatherUtils.CurrentWeather.CurrentWeatherClient;
import pw.sadbd.tourpatron.WeatherUtils.CurrentWeather.CurrentWeatherPojo.CurrentWeather;
import pw.sadbd.tourpatron.WeatherUtils.CurrentWeather.CurrentWeatherService;
import pw.sadbd.tourpatron.WeatherUtils.ForecastWeather.ForcastClient;
import pw.sadbd.tourpatron.WeatherUtils.ForecastWeather.ForecasetWeather;
import pw.sadbd.tourpatron.WeatherUtils.ForecastWeather.ForecastService;
import pw.sadbd.tourpatron.databinding.FragmentDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pw.sadbd.tourpatron.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ForcastAdapter adapter;
    private EventObjectListiner eventObjectListiner;

    private FusedLocationProviderClient client;
    private double currentLatitude, currentLongitude;

    private List<Event> eventList = new ArrayList<>();
    private List<EventMember> memberList = new ArrayList<>();
    private ProgressDialog dialog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference eventRef;
    private int position =-1;
    private int originalPosition = 0;
    private float totalCost =0f;
    private float budget =0f;
    public static final String baseUrl ="https://api.openweathermap.org/data/2.5/";
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        dialog = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        eventRef = FirebaseDatabase.getInstance().getReference().child("Event");
        user = auth.getCurrentUser();
        binding.forcastRecycler.setAdapter(adapter);
        binding.forcastRecycler.setLayoutManager(
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL,false
                ));

        firebaseEventget();
        binding.eventSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                originalPosition = i;

                if(i !=0) {
                    position = i-1;

                    getCurrentWeather(eventList.get(position).getDetails().getEventAddress());

                    getForcastWeather(eventList.get(position).getDetails().getEventAddress());

                    getCurrentBudgetProgress(i);
                }else {
                    getDeviceCurrentLocation();
                    binding.budgetStatus.setVisibility(View.GONE);
                    binding.textView5.setText("0");
                    binding.textView3.setText("0.00");
                    binding.progressBar.setProgress(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tempcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(originalPosition == 0){
                    Toast.makeText(getActivity(), "First select an event", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), EventActivity.class);

                    intent.putExtra("frag", 1);
                    StaticData.eventID = eventList.get(position).getDetails().getEventId();

                    startActivity(intent);
                }

            }
        });

    }

    private void getDeviceCurrentLocation() {
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    getLocationAddress();
                }else {
                    Toast.makeText(getActivity(), "Fails to locate current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLocationAddress() {
        final Geocoder geocoder = new Geocoder(getActivity());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);

            String street = addresses.get(0).getAddressLine(0);
            getCurrentWeather(street);
            getForcastWeather(street);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCurrentBudgetProgress(int i) {
        binding.budgetStatus.setVisibility(View.VISIBLE);

        String id = eventList.get(i-1).getDetails().getEventId();
        try {


            eventRef.child(id).child("Details").child("eventBudet").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {


                        String value = dataSnapshot.getValue(String.class);
                      //  binding.textView3.setText(value);
                        budget = Float.parseFloat(value);


                      //  int persent = (int) calculatePercentage(totalCost, budget);
                      //  binding.textView5.setText(String.valueOf(persent) + "%");
                       // binding.progressBar.setProgress(persent);
                    }catch (Exception ex){}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            eventRef.child(id).child("Expense").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    totalCost = 0;
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Expence expence = d.getValue(Expence.class);
                        totalCost += Float.parseFloat(expence.getCostName());

                    }
                    int persent = (int) calculatePercentage(totalCost, budget);
                    binding.textView5.setText(String.valueOf(persent) + "%");
                    binding.progressBar.setProgress(persent);
                    float remain = budget - totalCost;
                    binding.textView3.setText(String.valueOf(remain));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), "skfdkdsk", Toast.LENGTH_SHORT).show();
        }
    }


   public double calculatePercentage(double obtained, double total){
        return obtained * 100 / total;
   }
    private void firebaseEventget(){
        dialog.setMessage("Loading..");
      //  dialog.show();
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

                binding.mainuiLayout.setVisibility(View.VISIBLE);
               // dialog.dismiss();
                if(eventList !=null) {
                    try {
                        setspinner();
                    }catch (Exception e){}

                   // Toast.makeText(getActivity(), eventList.toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

              //  dialog.dismiss();
            }
        });

    }
    private void setspinner(){
        List<String> eventName =new ArrayList<>();
        eventName.add("Select Event");
        for(Event n:eventList){
            eventName.add(n.getDetails().getEventName());
        }
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,eventName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.eventSp.setAdapter(spinnerAdapter);
        if(eventName.size()>1){
            binding.eventSp.setSelection(1);
        }
    }
    private void getCurrentWeather(final String cityName){
        String baseUrl ="https://api.openweathermap.org/data/2.5/";
        CurrentWeatherService service = CurrentWeatherClient.getClient(
                baseUrl
        ).create(CurrentWeatherService.class);
        LatLng latLng = getLocationFromAddress(cityName);
        if(latLng !=null) {
            Log.i("LatLon", String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
            String endUrl = String.format("weather?lat=%f&lon=%f&units=metric&appid=%s", latLng.latitude, latLng.longitude, getString(R.string.currentWeatherApi));

            service.getCurrentWeather(endUrl)
                    .enqueue(new Callback<CurrentWeather>() {
                        @Override
                        public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                            if (response.isSuccessful()) {
                                currentWeatherUIupdate(response.body(), cityName);
                                binding.currentWeather.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CurrentWeather> call, Throwable t) {

                        }
                    });
        }else {
            Toast.makeText(getActivity(), "Address not found", Toast.LENGTH_SHORT).show();
        }
    }
    private void currentWeatherUIupdate(CurrentWeather currentWeather,String cityname){
        binding.currentWeather.setVisibility(View.VISIBLE);
        binding.textView13.setText(currentWeather.getSys().getCountry()+","+cityname);
        binding.progressBar15.setProgress(currentWeather.getMain().getTemp().intValue());
        binding.textView9.setText(String.valueOf(
                currentWeather.getMain().getTemp().intValue()
        )+" °C");
        binding.textView11.setText(String.valueOf(
                currentWeather.getMain().getHumidity().intValue()
        )+" %");
        binding.progressBar14.setProgress(currentWeather.getMain().getHumidity().intValue());
        Picasso.get().load("https://openweathermap.org/img/w/"+currentWeather.getWeather().get(0).getIcon()+".png")
                .into(binding.imageView);

    }
    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng latLng ;

        try {
            address = coder.getFromLocationName(strAddress,3);
            if (address==null) {
                return null;
            }
          //  Toast.makeText(getActivity(), String.valueOf(address.size()), Toast.LENGTH_SHORT).show();
            if(address.size()>0) {
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latLng = new LatLng(lat, lon);
                return latLng;
            }else {
                return new LatLng(23.810331,90.412521);
            }


        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return  new LatLng(23.810331,90.412521);
        }

    }
    private void getForcastWeather(String eventAddress) {
        ForecastService service = ForcastClient
                .getClient(baseUrl)
                .create(ForecastService.class);
        LatLng latLng = getLocationFromAddress(eventAddress);
        if(latLng !=null) {
            String endUrl = String
                    .format("forecast/daily?lat=%f&lon=%f&units=metric&cnt=16&appid=%s",
                            latLng.latitude,latLng.longitude,getString(R.string.weatherApi_get_from_sir));
            //  String s = "forecast/daily?lat=35&lon=139&cnt=10&appid=380199723cebdb85ef2e16cc30cee5b6";
            service.getForcaseWeather(endUrl)
                    .enqueue(new Callback<ForecasetWeather>() {
                        @Override
                        public void onResponse(Call<ForecasetWeather> call, Response<ForecasetWeather> response) {
                            if (response.isSuccessful()){
                                // Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT).show();
                                forcastweatherUIupdate(response.body());
                            }else {
                                Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForecasetWeather> call, Throwable t) {

                        }
                    });
        }else {
            Toast.makeText(getActivity(), "Address not found try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void forcastweatherUIupdate(ForecasetWeather forecasetWeather) {
        binding.forcastRecycler.setVisibility(View.VISIBLE);
        adapter = new ForcastAdapter(forecasetWeather);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.forcastRecycler.setAdapter(adapter);
        binding.forcastRecycler.setLayoutManager(linearLayoutManager);

    }


}
