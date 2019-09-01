package pw.sadbd.tourpatron;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pw.sadbd.tourpatron.PojoClass.Event;
import pw.sadbd.tourpatron.trytodeleteevent.EventDelete;
import pw.sadbd.tourpatron.trytodeleteevent.Eventdeletelistiner;
import pw.sadbd.tourpatron.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSION_CODE = 788;
    private static final int SELECT_IMAGE = 999;
    private boolean isPermissionGranated =false;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<Event> eventList_;


    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private DashboardFragment dashboardFragment;
    private EventFragment eventFragment;
    private ProfileFragment profileFragment;
    private GoogleMap mMap;

    private DatabaseReference eventRef;

    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        checkStoragePermission();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dashboardFragment = new DashboardFragment();
        eventFragment = new EventFragment();
        profileFragment = new ProfileFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container,dashboardFragment).commit();
        toolbar.setTitle("Dashboard");
      //  navigation.setSelectedItemId(R.id.navigation_event);



        new EventDelete().setEventdeletelistiner(new Eventdeletelistiner() {
            @Override
            public void onDelete(String eventid) {
                eventRef = FirebaseDatabase.getInstance().getReference().child("Event").child(eventid);
                eventRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    goDahboard();
                    toolbar.setTitle("Dashboard");
                    return true;
                case R.id.navigation_event:
                    goEvent();
                    toolbar.setTitle("Event");
                    return true;

                case R.id.navigation_profile:
                    goProfile();
                    toolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.logout_menu){
            auth.signOut();
            startActivity(new Intent(MainActivity.this,AuthenticationActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goDahboard(){
        fragmentManager.beginTransaction().replace(R.id.container,dashboardFragment).commit();
    }
    private void goEvent(){
        fragmentManager.beginTransaction().replace(R.id.container,eventFragment).commit();
      //  eventFragment.getEvent(eventList_);
    }
    private void goProfile(){
        fragmentManager.beginTransaction().replace(R.id.container,profileFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void checkStoragePermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat
                        .checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat
                        .checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat
                        .checkSelfPermission(this,
                                Manifest.permission.CAMERA)
                        !=PackageManager.PERMISSION_GRANTED
        )


        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA
                    },
                    PERMISSION_CODE);
        }else {
            isPermissionGranated =true;
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] !=PackageManager.PERMISSION_GRANTED
                && grantResults[1] != PackageManager.PERMISSION_GRANTED
                && grantResults[2] != PackageManager.PERMISSION_GRANTED
                && grantResults[3] != PackageManager.PERMISSION_GRANTED
        ){
            isPermissionGranated = false;
            checkStoragePermission();
        }else {
            isPermissionGranated =true;
        }
    }

}
