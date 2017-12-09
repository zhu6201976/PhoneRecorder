package com.example.administrator.test.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

import static android.telephony.TelephonyManager.CALL_STATE_RINGING;

/**
 * Created by My on 2017/12/8.
 */

public class PhoneListenService extends Service {

    private MyPhoneStateListener mPhoneStateListener;
    private TelephonyManager mTm;
    private MediaRecorder mRecorder;
    private static final String TAG = "PhoneListenService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTm != null && mPhoneStateListener != null) {
            mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_IDLE");
                    if (mRecorder != null) {
                        mRecorder.stop();
                        mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
                        mRecorder.release(); // Now the object cannot be reused
                    }
                    break;
                case CALL_STATE_RINGING:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_RINGING");
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/luyin.3gp");
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                    mRecorder.start();   // Recording is now started
                    break;
            }
        }
    }
}
