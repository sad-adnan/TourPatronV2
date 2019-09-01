package pw.sadbd.tourpatron;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pw.sadbd.tourpatron.R;

public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 778;
    private static final int SELECT_IMAGE = 9599;
    private boolean isPermissionGranated =false;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkStoragePermission();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if( isPermissionGranated){
            startActivitywithCondition();
        }


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
                        !=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat
                        .checkSelfPermission(this,
                                Manifest.permission.CALL_PHONE)
                        !=PackageManager.PERMISSION_GRANTED
        )


        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.CALL_PHONE
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
                && grantResults[4] != PackageManager.PERMISSION_GRANTED
        ){
            isPermissionGranated = false;
            checkStoragePermission();
        }else {
            isPermissionGranated =true;
            startActivitywithCondition();

        }
    }
    private void startActivitywithCondition(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user !=null){
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent =
                            new Intent(SplashActivity.this,AuthenticationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },500);
    }
}
