package pw.sadbd.tourpatron;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import pw.sadbd.tourpatron.PojoClass.EventDetails;
import pw.sadbd.tourpatron.PojoClass.StaticData;
import pw.sadbd.tourpatron.databinding.ActivityEventBinding;
import pw.sadbd.tourpatron.trytodeleteevent.EventDelete;
import pw.sadbd.tourpatron.R;


public class EventActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ActivityEventBinding binding;

    private EventDetailsFragment detailsFragment;
    private ExpenseFragment expenseFragment;
    private MembersFragment membersFragment;
    private MomentFragment momentFragment;
    private EventLocationFragment locationFragment;
    private EventDetails event;
    private String address;



    public static int indexCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        detailsFragment = new EventDetailsFragment();
        expenseFragment = new ExpenseFragment();
        membersFragment = new MembersFragment();
        momentFragment = new MomentFragment();
        /*locationFragment = new EventLocationFragment();*/

        mViewPager = (ViewPager) findViewById(R.id.container);


        int indexnumber = getIntent().getIntExtra("frag", 0);

        if (indexnumber == 1){
            indexCheck = 1;
        }else {
            indexCheck = 0;
        }

        if(getIntent()!=null){
           event = (EventDetails) getIntent().getSerializableExtra("event");


          if(event !=null){
              address = event.getEventAddress();
              Bundle bundle = new Bundle();
              bundle.putSerializable("event",event);
              detailsFragment.setArguments(bundle);
              expenseFragment.setArguments(bundle);
              membersFragment.setArguments(bundle);
              momentFragment.setArguments(bundle);
              /*locationFragment.setArguments(bundle);*/
          }
        }

        paggerAndTabinit();
    }

    private void paggerAndTabinit(){
        mViewPager.setAdapter(
                new selectpageAdapter(getSupportFragmentManager())
        );

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (indexCheck == 1){
            mViewPager.setCurrentItem(1);
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position==0){
                    binding.fabadd.setImageResource(R.drawable.ic_save_black_24dp);
                }else {
                    binding.fabadd.setImageResource(R.drawable.ic_add_black_24dp);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        binding.fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int index =  mViewPager.getCurrentItem();
               switch (index){
                   case 0:{
                       detailsFragment.getActivityStatus("");
                       break;
                   }
                   case 1:{
                       expenseFragment.getActivityStatus("");
                       break;
                   }
                   case 2:{
                       membersFragment.getActivityStatus("");
                       break;
                   }
                   case 3:{
                       momentFragment.getActivityStatus("");
                       break;
                   }
                   /*case 4:{
                       locationFragment.getActivityStatus("");
                       break;
                   }*/
               }

            }
        });

    }

    class selectpageAdapter extends FragmentPagerAdapter {
        public selectpageAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:{
                    return detailsFragment;
                }
                case 1:{
                    return  expenseFragment;
                }
                case 2:{
                    return membersFragment;
                }
                case 3:{
                    return momentFragment;
                }
                /*case 4:{
                    return locationFragment;
                }*/
                default:{
                    return new MembersFragment();
                }
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!StaticData.eventID.equals("NA")) {
            getMenuInflater().inflate(R.menu.menu_event, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mapOPen){
            if(address !=null){
                Intent intent = new Intent(EventActivity.this,MapsActivity.class);
                intent.putExtra("addressevent",address);
                startActivity(intent);
            }else {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }
        } else if(item.getItemId() == R.id.deleteevent){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.create();
            builder.setTitle("Delete");
            builder.setMessage("Do you want delete this event?");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new EventDelete().getEventdeletelistiner().onDelete(StaticData.eventID);
                    dialog.dismiss();
                    finish();

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
