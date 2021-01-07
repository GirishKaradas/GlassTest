/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.glass.glasstest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.android.glass.glasstest.fragments.BaseFragment;
import com.example.android.glass.glasstest.fragments.ColumnLayoutFragment;
import com.example.android.glass.glasstest.fragments.MainLayoutFragment;
import com.example.android.glass.glasstest.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector.Gesture;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity of the application. It provides viewPager to move between fragments.
 */
public class MainActivity extends BaseActivity {

    private String MENU_KEY="menu_key";
    protected static final int REQUEST_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        switch (gesture) {
            case TAP:

                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(MENU_KEY, R.menu.main_menu);
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
            String selectedOption = "";
            switch (id) {
                case R.id.bManual:
                    selectedOption = getString(R.string.manual);
                    startActivity(new Intent(this.getApplicationContext(), ManualActivity.class));
                    break;
                case R.id.bMaintenance:
                    selectedOption = getString(R.string.maintenance);
               //     startActivity(new Intent(this.getApplicationContext(), ManualActivity.class));
                    break;
                case R.id.bMonitor:
                    selectedOption = getString(R.string.monitor);
                    startActivity(new Intent(this.getApplicationContext(), MonitorActivity.class));
                    break;
                case R.id.bVideoCall:
                    selectedOption = getString(R.string.video_call);
                 //   startActivity(new Intent(this.getApplicationContext(), MonitorActivity.class));
                    break;
            }
            Toast.makeText(this.getApplicationContext(), selectedOption + " Activity Launching", Toast.LENGTH_SHORT)
                    .show();
        }
    }



}
