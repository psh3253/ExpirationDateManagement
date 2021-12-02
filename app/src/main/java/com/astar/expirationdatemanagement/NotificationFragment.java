package com.astar.expirationdatemanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.databinding.FragmentNotificationBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    MainActivity mainActivity;
    FragmentNotificationBinding binding;
    ArrayList<ExpirationDate> expirationDates;
    NotificationAdapter notificationAdapter;
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        expirationDates = (ArrayList<ExpirationDate>) expirationDateDao.getPreviousExpirationList();
        notificationAdapter = new NotificationAdapter(mainActivity);
        notificationAdapter.expirationDateList = expirationDates;
        binding.rvNotificationList.setAdapter(notificationAdapter);
        binding.rvNotificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    public void refreshNotification() {
        expirationDates = (ArrayList<ExpirationDate>) expirationDateDao.getPreviousExpirationList();
        notificationAdapter.expirationDateList = expirationDates;
        binding.rvNotificationList.setAdapter(notificationAdapter);
    }
}