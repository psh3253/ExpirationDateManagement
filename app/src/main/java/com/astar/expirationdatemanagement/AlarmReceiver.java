package com.astar.expirationdatemanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.model.ExpirationDate;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TestTest", "발동");
        Toast.makeText(context, "Yeah", Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("유통기한 경과 알림")
                .setContentText("유통기한이 지난 상품이 존재합니다. 확인하여 주세요.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        ArrayList<ExpirationDate> expirationDates = (ArrayList<ExpirationDate>) expirationDateDao.getPreviousExpirationList();
        if(expirationDates.size() > 0) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK  |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "My:Tag");
            wakeLock.acquire(5000);

            notificationManager.notify((int)(Math.random() * 10000), builder.build());
        }
    }

}
