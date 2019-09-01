package pw.sadbd.tourpatron;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import pw.sadbd.tourpatron.databinding.ActivityProfileDetailsBinding;

import pw.sadbd.tourpatron.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    private ActivityProfileDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        binding = DataBindingUtil
                .setContentView(ProfileDetailsActivity.this,
                        R.layout.activity_profile_details);


    }
}
