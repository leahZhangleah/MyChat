package com.example.android.mychat.chats;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.mychat.R;
import com.example.android.mychat.contacts.ContactsActivity;
import com.example.android.mychat.login.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    public static final String PUSH_NOTIFICATION_ID = "PUSH NOTIFICATION ID";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG,"The new token is: "+token);
        //todo
    }

    //handle msgs when app is in the foreground
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"FROM: "+remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"Message data payload: "+remoteMessage.getData());
            Log.d(TAG,"Message data payload: "+remoteMessage.getData().values());
            Log.d(TAG,"Message data payload: "+remoteMessage.getData().values());
            try{
                JSONObject data = (JSONObject) remoteMessage.getData();
                String jsonMessage = data.getString("extra_infomation");
                Log.d(TAG,"OnReceivedMessage: Extra Information: "+jsonMessage);
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        Log.d(TAG,"Message type: "+remoteMessage.getMessageType());
        if(remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
            Log.d(TAG,"title: "+title+" body: "+body+" click action: "+click_action);
            sendNotification(title,body,click_action);
        }
    }

    public void sendNotification(String title,String body,String clickAction){
        Intent intent;
        if(clickAction.equals("CONTACTACTIVITY")){
           intent = new Intent(this, ContactsActivity.class);
        }else{
            intent = new Intent(this, LoginActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,PUSH_NOTIFICATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
























