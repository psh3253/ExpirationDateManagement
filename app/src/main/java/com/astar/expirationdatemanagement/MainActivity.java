package com.astar.expirationdatemanagement;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.astar.expirationdatemanagement.dao.AppDatabase;
import com.astar.expirationdatemanagement.databinding.ActivityMainBinding;
import com.astar.expirationdatemanagement.receiver.AlarmReceiver;
import com.astar.expirationdatemanagement.view.ExpirationDateRegisterFragment;
import com.astar.expirationdatemanagement.view.ProductRegisterFragment;
import com.astar.expirationdatemanagement.view.ProductSearchFragment;
import com.astar.expirationdatemanagement.view.ViewPagerFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ViewPagerFragment viewPagerFragment;
    ProductSearchFragment productSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBInfo.appDatabase = Room.databaseBuilder(this, AppDatabase.class, "app")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        viewPagerFragment = new ViewPagerFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, viewPagerFragment);
        transaction.commit();

        setAlarm();
        createNotificationChannel();
    }

    public void changeFragment(int fragmentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentId == 1) {
            transaction.replace(R.id.fragment_container, new ProductRegisterFragment())
                    .addToBackStack(null);
        } else if (fragmentId == 2) {
            productSearchFragment = new ProductSearchFragment();
            transaction.replace(R.id.fragment_container, productSearchFragment)
                    .addToBackStack(null);
        } else if (fragmentId == 3) {
            transaction.replace(R.id.fragment_container, new ExpirationDateRegisterFragment())
                    .addToBackStack(null);
        }
        transaction.commit();
    }

    public void removeFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public String saveImageFile(Uri productImagePath, String barcodeNumber) {

        String ext = getFileExtension(productImagePath);
        String newFileName = getFilesDir().getAbsolutePath() + File.separatorChar + barcodeNumber + "." + ext;

        try (BufferedInputStream bis = new BufferedInputStream(getContentResolver().openInputStream(productImagePath)); BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFileName, false))) {
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
            return newFileName;
        } catch (IOException e) {
            Toast.makeText(this, "사진 파일을 저장할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void refreshProductList() {
        viewPagerFragment.getProductListFragment().refreshProductList();
    }

    public void refreshSearch() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            productSearchFragment.refreshSearch();
        }
    }

    public void refreshExpirationDateList() {
        viewPagerFragment.getExpirationDateListFragment().refreshExpirationDateList();
    }

    public void refreshNotification() {
        viewPagerFragment.getNotificationFragment().refreshNotification();
    }

    private void createNotificationChannel() {
        CharSequence name = "알림";
        String description = "notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notification", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
    }
}
