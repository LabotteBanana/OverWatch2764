package com.example.codingmaster.overwatch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends Activity implements ImageButton.OnClickListener, Recorder.OnVoicePlaybackStateChangedListener {

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String VOICE_FILE_NAME = "audiorecord.pcm";
    private MediaPlayer mMediaPlayer;
    private AppState mState = AppState.READY;
    private Recorder mSoundRecorder;

    @Override
    public void onPlaybackStopped() {
        mState = AppState.READY;
    }


    enum AppState {
        READY, PLAYING_VOICE, PLAYING_MUSIC, RECORDING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, " StopRecord ");

        ImageButton Rec_btn = (ImageButton) findViewById(R.id.Record_Btn);
        ImageButton Play_btn = (ImageButton) findViewById(R.id.Play_Btn);
        Button Find_btn = (Button) findViewById(R.id.Find_Btn);
        TextView File_Name = (TextView) findViewById(R.id.File_Name);

        Rec_btn.setOnClickListener(this);
        Play_btn.setOnClickListener(this);
        Find_btn.setOnClickListener(this);

    }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Record_Btn:
                    if(mState == AppState.RECORDING) {
                        mState = AppState.READY;
                        Log.d(TAG, " StopRecord ");
                        mSoundRecorder.stopRecording();
                    }
                    else if(mState == AppState.READY) {
                        mState = AppState.RECORDING;
                        Log.d(TAG, " StartRecord ");
                        mSoundRecorder.startRecording();
                    }
                    break;
                case R.id.Play_Btn:
                    if(mState == AppState.PLAYING_VOICE) {
                        mState = AppState.READY;
                        Log.d(TAG, " StopPlay ");
                        mSoundRecorder.stopPlaying();
                    }
                    else if(mState == AppState.READY){
                        mState = AppState.PLAYING_VOICE;
                        Log.d(TAG, " Play ");
                        mSoundRecorder.startPlay();
                    }
                    break;

                case R.id.Find_Btn:
                    Log.d(TAG, " Find ");
                    break;
            }

        }

    @Override
    protected void onStart() {
        super.onStart();
        if (speakerIsSupported()) {
            mSoundRecorder = new Recorder(this, File_name(), this);
        } else {
            findViewById(R.id.container2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "no_speaker_supported",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private String File_name() {
        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(calendar.YEAR);
        int MONTH = calendar.get(calendar.MONTH);
        int DAY = calendar.get(calendar.DAY_OF_MONTH);
        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);

        String FileName = String.format("%%%_%%", YEAR,MONTH,DAY,HOUR,MINUTE);
        return FileName;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mSoundRecorder != null) {
            mSoundRecorder.cleanup();
            mSoundRecorder = null;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onStop();
    }


    /**
     * Determines if the wear device has a built-in speaker and if it is supported. Speaker, even if
     * physically present, is only supported in Android M+ on a wear device..
     */
    public final boolean speakerIsSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager packageManager = getPackageManager();
            // The results from AudioManager.getDevices can't be trusted unless the device
            // advertises FEATURE_AUDIO_OUTPUT.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false;
            }
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true;
                }
            }
        }
        return false;
    }






}
