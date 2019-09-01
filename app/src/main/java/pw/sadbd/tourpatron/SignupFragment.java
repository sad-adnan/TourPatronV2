package pw.sadbd.tourpatron;


import android.app.ProgressDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pw.sadbd.tourpatron.Interface.AuthenticationListiner_;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.FragmentSignipBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignupFragment extends Fragment {

    private FragmentSignipBinding binding;
    private FirebaseAuth auth;
    private AuthenticationListiner_ authenticationListiner;
    private ProgressDialog dialog;
    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_signip, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        authenticationListiner = (AuthenticationListiner_) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Please Wait");
                dialog.show();
                String email = binding.emailET.getText().toString();
                String pass = binding.passwordET.getText().toString();
                String comPass = binding.confirmpasswordET.getText().toString();

                if(email.isEmpty()){
                    binding.emailET.setError(getString(R.string.required_field));
                }
                if(pass.isEmpty()){
                    binding.passwordET.setError(getString(R.string.required_field));
                }
                if(comPass.isEmpty()){
                    binding.confirmpasswordET.setError(getString(R.string.required_field));
                }
                if(!email.isEmpty() && !pass.isEmpty() && !comPass.isEmpty()){
                    if(pass.equals(comPass)){
                        auth.createUserWithEmailAndPassword(email,pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            authenticationListiner.goToPtofile();
                                            dialog.dismiss();
                                        }else {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else {
                        binding.confirmpasswordET.setError("Password don't match");
                    }
                }
            }
        });
    }
}
