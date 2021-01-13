package com.example.android.glass.glasstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.example.android.glass.glasstest.fragments.BaseFragment;
import com.example.android.glass.glasstest.fragments.LyoLayoutFragment;
import com.example.android.glass.glasstest.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LyoManualActivity extends BaseActivity {

    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout indicator;
    private ArrayList<DataLyo> arrayList = new ArrayList<>();

    private final String ACCESS_TOKEN="tbYF8iyCGtF0CNgErPrRdU3LelybCwSXGWl5jA7nRoQ";
    private final String SPACE_ID= "kjy57u6y7jdo";

    private static final int REQUEST_CODE = 301;
    private String MENU_KEY="menu_key";

    private final CDAClient client = CDAClient
            .builder()
            .setToken(ACCESS_TOKEN)
            .setSpace(SPACE_ID)
            .setEnvironment("master")
            .build();

    final ScreenSlidePagerAdapter screenSliderPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyo_manual);

        viewPager = findViewById(R.id.activity_lyo_viewpager);
        indicator = findViewById(R.id.activity_lyo_indicator);
        arrayList = new ArrayList<>();
        indicator.setupWithViewPager(viewPager, true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (arrayList.isEmpty()){

            client
                    .observe(CDAEntry.class)
                    .withContentType("lyo")
                    .all()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<CDAArray>() {
                        @Override
                        public void accept(CDAArray entries) {

                            arrayList = new ArrayList<>();
                            for (final CDAResource cdaResource: entries.items()){

                                final CDAEntry entry = (CDAEntry) cdaResource;

                                double id = entry.getField("id");
                                String step = entry.getField("step");
                                String type = entry.getField("type");
                                String title = entry.getField("title");
                                CDAAsset asset = entry.getField("image");
                                String url = "";
                                if (asset!= null){
                                    url = asset.url();
                                }
                                String desc = entry.getField("desc");

                                Log.e("This", step);
                                arrayList.add(arrayList.size(), new DataLyo(id, step, type, title, url, desc));
                            }

                            fragments.clear();

                            Collections.sort(arrayList, new Comparator<DataLyo>() {
                                @Override
                                public int compare(DataLyo lyo, DataLyo lyo1) {
                                    return Double.valueOf(lyo.getId()).compareTo(Double.valueOf(lyo1.getId()));
                                }
                            });
                            for (int i=0; i<arrayList.size(); i++){
                                DataLyo dataLyo = arrayList.get(i);
                                Log.e("This 2", dataLyo.getStep());
                                fragments.add(LyoLayoutFragment.newInstance(dataLyo.getId(), dataLyo.getStep(), dataLyo.getType(), dataLyo.getTitle(), dataLyo.getUrl(), dataLyo.getDesc()));
                            }
                            screenSliderPagerAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(screenSliderPagerAdapter);
                        }
                    });
        }
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
                //    fragments.get(viewPager.getCurrentItem()).onSingleTapUp();
                Intent intent = new Intent(LyoManualActivity.this, MenuActivity.class);
                intent.putExtra(MENU_KEY, R.menu.menu_call);
                startActivityForResult(intent, REQUEST_CODE );
                return true;
            default:
                return super.onGesture(gesture);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final int id = data.getIntExtra(MenuActivity.EXTRA_MENU_ITEM_ID_KEY,
                    MenuActivity.EXTRA_MENU_ITEM_DEFAULT_VALUE);
            switch (id) {
                case R.id.bWebrtc:
                //    startActivity(new Intent(this.getApplicationContext(), VideoCallActivity.class));
                    break;
                case R.id.bAgora:
                  //  startActivity(new Intent(this.getApplicationContext(), AgoraActivity.class));
                    break;
            }
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}