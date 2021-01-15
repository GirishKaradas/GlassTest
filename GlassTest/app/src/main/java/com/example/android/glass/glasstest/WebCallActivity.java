package com.example.android.glass.glasstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.glass.ui.GlassGestureDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WebCallActivity extends BaseActivity {

    private TextView textView;
    private DatabaseReference reference;
    private int count;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_call);

        textView = findViewById(R.id.activity_web_tvCount);

        reference = FirebaseDatabase.getInstance().getReference().child("tapcount");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = Integer.parseInt(snapshot.getValue().toString());
                textView.setText(getString(R.string.tap_count) + " : " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case TAP:
                count++;
                reference.setValue(count);
                break;
        }
        return super.onGesture(gesture);
    }
}