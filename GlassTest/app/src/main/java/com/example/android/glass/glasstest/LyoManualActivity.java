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
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView textView;
    public static Boolean FLAG5 = false;
    public static Boolean FLAG8 = false;

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
        textView = findViewById(R.id.activity_lyo_tvPage);
        arrayList = new ArrayList<>();
        indicator.setupWithViewPager(viewPager, true);

        indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                textView.setText((indicator.getSelectedTabPosition()+1) + " of "+ indicator.getTabCount());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
                if(viewPager.getCurrentItem()==5 || viewPager.getCurrentItem()==8){
                    Intent intent = new Intent(LyoManualActivity.this, MenuActivity.class);
                    intent.putExtra(MENU_KEY, R.menu.menu_call_cam);
                    startActivityForResult(intent, REQUEST_CODE);
                }else if (viewPager.getCurrentItem() == 0){
                    startActivity(new Intent(LyoManualActivity.this, TutorialActivity.class));
                } else{
                    Intent intent = new Intent(LyoManualActivity.this, MenuActivity.class);
                    intent.putExtra(MENU_KEY, R.menu.menu_call);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                return true;

            case SWIPE_FORWARD:
                if (viewPager.getCurrentItem()==5 && !FLAG5){
                        Toast.makeText(this, getString(R.string.text_error), Toast.LENGTH_SHORT).show();
                        return true;
                }else if (viewPager.getCurrentItem()==8 && !FLAG8){
                        Toast.makeText(this, getString(R.string.text_error), Toast.LENGTH_SHORT).show();
                        return true;
                }else {
                    return super.onGesture(gesture);
                }
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
                case R.id.bCamera2:
                    Intent intent = new Intent(this.getApplicationContext(), CameraActivity.class);
                    intent.putExtra("step", viewPager.getCurrentItem());
                    startActivity(intent);
                    break;
                case R.id.bAgora2:
                    startActivity(new Intent(this.getApplicationContext(), AgoraActivity.class));
                    break;
                case R.id.bVideoCall2:
                    startActivity(new Intent(this.getApplicationContext(), VideoCallActivity.class));
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