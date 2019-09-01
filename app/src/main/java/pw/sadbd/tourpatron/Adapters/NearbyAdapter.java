package pw.sadbd.tourpatron.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pw.sadbd.tourpatron.Interface.NearbyLiatiner;
import pw.sadbd.tourpatron.LocationAndMap.NearbySearch.NearbyPojo.NearbyResponse;
import pw.sadbd.tourpatron.LocationAndMap.NearbySearch.NearbyPojo.Result;
import pw.sadbd.tourpatron.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyHolder> {
    private Context context;
    private NearbyResponse response;
    private NearbyLiatiner nearbyLiatiner;

    public NearbyAdapter(Context context, NearbyResponse response) {
        this.context = context;
        this.response = response;
        this.nearbyLiatiner = (NearbyLiatiner) context;
    }

    @NonNull
    @Override
    public NearbyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NearbyHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_nearby_place,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final NearbyHolder holder, int position) {
        final Result result = response.getResults().get(position);
        if(result.getPhotos() !=null ) {
            String photolink =
                    String.format("https://maps.googleapis.com/maps/api/place/" +
                                    "photo?maxwidth=400&photoreference=%s&key=%s",
                            result.getPhotos().get(0).getPhotoReference()
                            ,
                            context.getString(R.string.nearby_place_api_key));
            Picasso.get().load(photolink).into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    holder.imageView.setImageResource(R.drawable.no_image);
                }
            });
        }
        holder.nameTV.setText(result.getName());
        if(result.getOpeningHours() !=null) {
            boolean isopen = result.getOpeningHours().getOpenNow();
            if (isopen) {
                holder.statusTv.setText("Open");
            } else {
                holder.statusTv.setText("Close");
            }
        }else {
            holder.statusTv.setText("") ;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng());
                nearbyLiatiner.getLatlon(latLng);
            }
        });


    }

    @Override
    public int getItemCount() {
        return response.getResults().size();
    }
}
class NearbyHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameTV;
    TextView statusTv;
    CardView cardView;

    public NearbyHolder(View itemView) {
        super(itemView);
        nameTV = itemView.findViewById(R.id.nearby_name);
        imageView = itemView.findViewById(R.id.nearby_img);
        statusTv = itemView.findViewById(R.id.nearby_status);
        cardView = itemView.findViewById(R.id.nearby_card);
    }
}
