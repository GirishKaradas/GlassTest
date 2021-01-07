package com.example.android.glass.glasstest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.glass.ui.GlassGestureDetector;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;
import androidx.annotation.NonNull;
import android.Manifest;
import android.widget.FrameLayout;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.opengl.GLSurfaceView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
    private DatabaseReference reference, ref1, ref2;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        requestPermissions();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference= firebaseDatabase.getReference();
        ref1= reference.child("calls");
        ref2 = reference.child("session");
        String time=new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date());
        String key=ref1.push().getKey();
        ref1.child(key).setValue(time);
        ref2.setValue(key);
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

            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);


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
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
        /*    mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());

         */
        }    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            /*
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();

             */

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

        if (gesture == GlassGestureDetector.Gesture.SWIPE_DOWN){
            ref2.setValue("");
            mSession.disconnect();
        }
        return super.onGesture(gesture);
    }
}