package com.example.android.glass.glasstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.example.android.glass.glasstest.fragments.BaseFragment;
import com.example.android.glass.glasstest.fragments.ColumnLayoutFragment;
import com.example.android.glass.glasstest.fragments.MainLayoutFragment;
import com.example.android.glass.glasstest.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ManualActivity extends BaseActivity {

    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;

    private ArrayList<DataManual> arrayList=new ArrayList<>();

    private final String ACCESS_TOKEN="tbYF8iyCGtF0CNgErPrRdU3LelybCwSXGWl5jA7nRoQ";
    private final String SPACE_ID= "kjy57u6y7jdo";

    private static final int REQUEST_CODE = 201;
    private String MENU_KEY="menu_key";

    private final CDAClient client=CDAClient
            .builder()
            .setToken(ACCESS_TOKEN)
            .setSpace(SPACE_ID)
            .setEnvironment("master")
            .build();

    final ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(
            getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);

        viewPager = findViewById(R.id.viewPager);
        arrayList=new ArrayList<>();
        final TabLayout tabLayout = findViewById(R.id.page_indicator);
        tabLayout.setupWithViewPager(viewPager, true);

    }



    @Override
    protected void onResume() {
        super.onResume();

        if (arrayList.isEmpty()) {

            client
                    .observe(CDAEntry.class)
                    .all()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<CDAArray>() {
                        @Override
                        public void accept(CDAArray entries) {

                            for (final CDAResource resource : entries.items()) {

                                final CDAEntry entry = (CDAEntry) resource;

                                double id_double = entry.getField("id");
                                String title = entry.getField("title");
                                String step = entry.getField("step");
                                String desc = entry.getField("desc");

                                CDAAsset asset = entry.getField("image");
                                String url = asset.url();

                                arrayList.add(arrayList.size(), new DataManual(id_double, title, step, desc, url));
                            }
                            fragments.clear();
                            for (int i = 0; i < arrayList.size(); i++) {
                                DataManual dataManual = arrayList.get(i);
                                fragments.add(ColumnLayoutFragment.newInstance(dataManual.getUrl(), dataManual.getStep(), dataManual.getDesc(), ""));
                            }
                            screenSlidePagerAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(screenSlidePagerAdapter);

                        }
                    });
        }

    }




    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
            //    fragments.get(viewPager.getCurrentItem()).onSingleTapUp();
                Intent intent = new Intent(ManualActivity.this, MenuActivity.class);
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
                    startActivity(new Intent(this.getApplicationContext(), VideoCallActivity.class));
                    break;
                case R.id.bAgora:
                    startActivity(new Intent(this.getApplicationContext(), AgoraActivity.class));
                    break;
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

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
