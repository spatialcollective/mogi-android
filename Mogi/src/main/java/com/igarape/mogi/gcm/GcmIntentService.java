package com.igarape.mogi.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.igarape.mogi.recording.RecordingService;
import com.igarape.mogi.recording.StreamingService;
import com.igarape.mogi.recording.ToggleStreamingService;
import com.igarape.mogi.server.UpdateLocationService;

public class GcmIntentService extends IntentService {

    public static String TAG = "GcmIntentService";

    private static final String KEY_LOCATION = "sendLocation";

    private static final String KEY_STREAMING_START = "startStreaming";

    private static final String KEY_STREAMING_STOP = "stopStreaming";

    public GcmIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d(TAG, extras.toString());

                String key = extras.getString("collapse_key");

                if ( key.equals(KEY_LOCATION) ) {
                    startService(new Intent(this, UpdateLocationService.class));
                } else if ( key.equals(KEY_STREAMING_START) && !StreamingService.IsStreaming ) {
                    startService(new Intent(this, ToggleStreamingService.class));
                } else if ( key.equals(KEY_STREAMING_STOP) && StreamingService.IsStreaming ) {
                    stopService(new Intent(this, ToggleStreamingService.class));
                }
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
