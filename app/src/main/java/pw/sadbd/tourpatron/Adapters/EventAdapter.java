package pw.sadbd.tourpatron.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pw.sadbd.tourpatron.EventActivity;
import pw.sadbd.tourpatron.LocationAndMap.SinglePlace.Pojo.SinglePlace;
import pw.sadbd.tourpatron.LocationAndMap.SinglePlace.RetrofitClient;
import pw.sadbd.tourpatron.LocationAndMap.SinglePlace.SinglePlaceService;
import pw.sadbd.tourpatron.PojoClass.Event;
import pw.sadbd.tourpatron.PojoClass.EventDetails;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_recycler_row,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final EventHolder holder, final int position) {
        final EventDetails details = eventList.get(position).getDetails();
        holder.eventName.setText(details.getEventName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String startdate = dateFormat.format(details.getEventStartDate().getTime());
        holder.startDate.setText(startdate);
        String enddate = dateFormat.format(details.getEventEndDate().getTime());
        holder.endDate.setText(enddate);
        holder.eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticData.eventID = details.getEventId();
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("event",eventList.get(position).getDetails());
                context.startActivity(intent);
            }
        });
        SinglePlaceService service = RetrofitClient.getClient().create(SinglePlaceService.class);
        String endurl = String.format("json?input=%s&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=%s",
                details.getEventAddress(),
                context.getString(R.string.nearby_place_api_key));
        service.getsinglePlaces(endurl).enqueue(new Callback<SinglePlace>() {
            @Override
            public void onResponse(Call<SinglePlace> call, Response<SinglePlace> response) {
                if (response.isSuccessful()){
                    SinglePlace singlePlace =   response.body();

                    if(singlePlace !=null){
                       if(singlePlace.getCandidates() !=null){
                           if(singlePlace.getCandidates().size()>0){
                               if(singlePlace.getCandidates().get(0) !=null){
                                   if(singlePlace.getCandidates().get(0).getPhotos() !=null){
                                       if(singlePlace.getCandidates().get(0).getPhotos().size()>0){
                                           if(singlePlace.getCandidates().get(0).getPhotos().get(0) !=null){
                                              if(singlePlace.getCandidates().get(0).getPhotos().get(0).getPhotoReference() !=null){
                                                  String photoRef = singlePlace.getCandidates()
                                                          .get(0)
                                                          .getPhotos()
                                                          .get(0)
                                                          .getPhotoReference();
                                                  String photoLink =
                                                          String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                                                                  photoRef,context.getString(R.string.nearby_place_api_key));
                                                  Picasso.get().load(photoLink).into(holder.imageView, new com.squareup.picasso.Callback() {
                                                      @Override
                                                      public void onSuccess() {

                                                      }

                                                      @Override
                                                      public void onError(Exception e) {
                                                          holder.imageView.setImageResource(R.drawable.no_image);
                                                      }
                                                  });

                                              }
                                           }
                                       }
                                   }
                               }
                           }
                       }
                    }




                }else {

                }
            }

            @Override
            public void onFailure(Call<SinglePlace> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
class EventHolder extends RecyclerView.ViewHolder {

    CardView eventCard;
    TextView eventName;
    TextView startDate;
    TextView endDate;
    TextView dayleft;
    ImageView imageView;
    public EventHolder(View itemView) {
        super(itemView);

        eventCard = itemView.findViewById(R.id.eventCard);
        eventName = itemView.findViewById(R.id.row_event_name);
        startDate = itemView.findViewById(R.id.row_envet_start_date);
        endDate = itemView.findViewById(R.id.row_event_end_date);
        dayleft = itemView.findViewById(R.id.row_event_days);
        imageView = itemView.findViewById(R.id.eventrowImg);

    }
}
