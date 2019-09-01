package pw.sadbd.tourpatron;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import pw.sadbd.tourpatron.Interface.EventHandelActivityToFragmentListiner;
import pw.sadbd.tourpatron.R;

public class EventLocationFragment extends Fragment implements EventHandelActivityToFragmentListiner {



    public EventLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_location, container, false);
    }

    @Override
    public void getActivityStatus(String status) {

    }
}
