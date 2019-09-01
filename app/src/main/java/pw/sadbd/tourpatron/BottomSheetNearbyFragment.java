package pw.sadbd.tourpatron;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pw.sadbd.tourpatron.Interface.NearbyBottomSheetListiner;
import pw.sadbd.tourpatron.LocationAndMap.NearbySearch.NearbyPojo.Result;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.BottomSheetFagmentNearbyBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomSheetNearbyFragment  extends BottomSheetDialogFragment implements NearbyBottomSheetListiner {

    private BottomSheetFagmentNearbyBinding binding;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.bottom_sheet_fagment_nearby,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void getNearbyObject(List<Result> resultList) {
        Toast.makeText(getActivity(), "ok work", Toast.LENGTH_SHORT).show();
    }
}
