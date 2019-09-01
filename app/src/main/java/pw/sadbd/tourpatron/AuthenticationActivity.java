package pw.sadbd.tourpatron;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import pw.sadbd.tourpatron.Interface.AuthenticationListiner_;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.ActivityAuthenticateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationListiner_ {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private ActivityAuthenticateBinding binding;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private SignupFragment signupFragment;
    private ProfileEditFragment profileEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_authenticate);

        loginFragment = new LoginFragment();
        profileEditFragment = new ProfileEditFragment();
        signupFragment = new SignupFragment();
        auth = FirebaseAuth.getInstance();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.authContiners,loginFragment)
                .commit();
        user = auth.getCurrentUser();
        if(user !=null){
            goToPtofile();
        }else {

          goToLogin();
        }


    }
    @Override
    public void goToPtofile() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.authContiners,profileEditFragment)
                .commit();
    }

    @Override
    public void goToLogin() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.authContiners,loginFragment)
                .commit();
    }

    @Override
    public void goToSignUp() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.authContiners,signupFragment)
                .addToBackStack(null)
                .commit();
    }
}
