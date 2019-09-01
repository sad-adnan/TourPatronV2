package pw.sadbd.tourpatron;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pw.sadbd.tourpatron.PojoClass.Moment;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.R;

import pw.sadbd.tourpatron.databinding.ActivityMomentImageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MomentImageActivity extends AppCompatActivity {

    private ActivityMomentImageBinding binding;
    private DatabaseReference momentRef;
    private  Moment moment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_moment_image);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_moment_image);
        setSupportActionBar(binding.toolbar2moment);


         moment = (Moment) getIntent().getSerializableExtra("moment");

        Picasso.get().load(moment.getMomentImg()).into(binding.imageMoment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense_delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.deleteExpense){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.create();
            builder.setTitle("Delete");
            builder.setMessage("Do you want delete this moment?");
            momentRef = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Event")
                    .child(StaticData.eventID)
                    .child("Moments")
                    .child(moment.getMomentID());

            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    momentRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MomentImageActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(MomentImageActivity.this, "Failed to remove", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return true;
    }
}
