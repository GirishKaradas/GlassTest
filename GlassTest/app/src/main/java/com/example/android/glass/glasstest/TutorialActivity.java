package com.example.android.glass.glasstest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glass.ui.GlassGestureDetector;

import java.util.ArrayList;

public class TutorialActivity extends BaseActivity {

    private ArrayList<String> arrayList= new ArrayList();
    private TextView textView;
    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        prepareArraylist();

        textView = findViewById(R.id.activity_tutorial_textview);
        textView.setText(arrayList.get(count));

    }

    private void prepareArraylist() {
        arrayList.add(arrayList.size(), getString(R.string.tap));
        arrayList.add(arrayList.size(), getString(R.string.double_tap));
        arrayList.add(arrayList.size(), getString(R.string.swipe_f));
        arrayList.add(arrayList.size(), getString(R.string.swipe_b));
        arrayList.add(arrayList.size(), getString(R.string.swipe_d));
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case TAP:
                if (count == 0) {
                    count++;
                    Toast.makeText(this, "Step " + count + "Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                }
                return true;

            case TWO_FINGER_TAP:
                if (count == 1){
                    count++;
                    Toast.makeText(this, "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                }
                return true;

            case SWIPE_FORWARD:
                if (count == 2){
                    count++;
                    Toast.makeText(this, "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                }
                return true;

            case SWIPE_BACKWARD:
                if (count == 3){
                    count++;
                    Toast.makeText(this, "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    textView.setText(arrayList.get(count));
                }
                return true;

            case SWIPE_DOWN:
                if (count==4){
                    count++;
                    Toast.makeText(this, "Step " + count + " Completed", Toast.LENGTH_SHORT).show();
                    return super.onGesture(gesture);
                }else {
                    return  true;
                }

            default:
                return super.onGesture(gesture);

        }

    }
}