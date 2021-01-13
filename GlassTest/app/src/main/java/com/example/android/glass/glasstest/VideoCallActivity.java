package com.example.android.glass.glasstest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.example.glass.ui.GlassGestureDetector;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.opengl.GLSurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class VideoCallActivity extends BaseActivity implements  Session.SessionListener,
        PublisherKit.PublisherListener{

    private static String API_KEY = "47070554";
    private static String SESSION_ID = "1_MX40NzA3MDU1NH5-MTYwOTk2NDE1OTE1NH40U0dFMmlzMVZYcTRtSktVdWNteERUN1h-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NzA3MDU1NCZzaWc9ZTQ0NDYwZThkMWNhMWZkODUzNDk5NGJmZTYxNzk0YTdlOGUxYTYwYjpzZXNzaW9uX2lkPTFfTVg0ME56QTNNRFUxTkg1LU1UWXdPVGsyTkRFMU9URTFOSDQwVTBkRk1tbHpNVlpZY1RSdFNrdFZkV050ZUVSVU4xaC1mZyZjcmVhdGVfdGltZT0xNjA5OTY0MTgwJm5vbmNlPTAuNDIwOTMxODk0NTI4NTU5NCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjEyNTU2MTgzJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference, ref1, ref_session, ref2;
    private StorageReference storageReference, storageRef1;
    private RecyclerView rcView;
    private ArrayList<DataUploads> arrayList =  new ArrayList<>();
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        rcView = findViewById(R.id.activity_call_rcView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcView.setLayoutManager(layoutManager);
        rcView.setHasFixedSize(true);
        adapter = new ImageAdapter(this, arrayList);
        rcView.setAdapter(adapter);

        requestPermissions();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference= firebaseDatabase.getReference();
        ref1= reference.child("calls");
        ref_session = reference.child("session");

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String key=ref1.push().getKey();
        ref1.child(key).child("time").setValue(now.toString());
        ref_session.setValue(key);
        ref2 = reference.child("uploads").child(key);
        storageReference = FirebaseStorage.getInstance().getReference().child("images").child(key);

        ref2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.add(new DataUploads(snapshot.getKey().toString(), snapshot.getValue().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(VideoCallActivity.this, "Image Received, TAP to check", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayList.add(new DataUploads(snapshot.getKey().toString(), snapshot.getValue().toString()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                arrayList.remove(new DataUploads(snapshot.getKey().toString(), snapshot.getValue().toString()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

        private Context context;
        private ArrayList<DataUploads> list=new ArrayList<>();

        public ImageAdapter(Context context, ArrayList<DataUploads> list) {
            this.context=context;
            this.list=list;
        }

        @NonNull
        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_call, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, final int position) {
            Picasso.get().load(list.get(position).getUrl()).into(holder.imageView);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDesc;
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.list_call_imageview);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {

            mPublisherViewContainer = findViewById(R.id.publisher_container);
            mSubscriberViewContainer = findViewById(R.id.subscriber_container);


            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    // SessionListener methods

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
        mSubscriber.destroy();
        mPublisher.destroy();
        ref_session.setValue("");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscribeToAudio(true);
            mSubscriber.setSubscribeToVideo(true);
            mSession.subscribe(mSubscriber);
            if (stream.hasAudio()){
                Log.e("CallActivity", "Audio is Enabled");
            }else {
                Log.e("CallActivity", "Audio is Disabled");
            }
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {

            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOG_TAG, "Error");

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case SWIPE_DOWN:
                if (rcView.getVisibility() == View.VISIBLE){
                    rcView.setVisibility(View.GONE);
                }else {
                    mSession.disconnect();
                    ref_session.setValue("");
                }
                break;

            case TAP:
                if (rcView.getVisibility() == View.VISIBLE){
                    rcView.setVisibility(View.GONE);
                }else {
                    rcView.setVisibility(View.VISIBLE);
                }
                break;
        }
        return true;
    }


}