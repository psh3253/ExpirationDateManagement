package com.astar.expirationdatemanagement.view;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.astar.expirationdatemanagement.DBInfo;
import com.astar.expirationdatemanagement.MainActivity;
import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentExpirationDateRegisterBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpirationDateRegisterFragment extends Fragment {

    MainActivity mainActivity;
    FragmentExpirationDateRegisterBinding binding;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpirationDateRegisterBinding.inflate(inflater, container, false);

        binding.btExpirationRegisterCaptureBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeLauncher.launch(new ScanOptions());
            }

            private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                    result -> {
                        if (result.getContents() != null) {
                            binding.etExpirationRegisterProductBarcode.setText(result.getContents());
                        }
                    });
        });
        Calendar calendar = Calendar.getInstance();
        binding.calendarView.setMinDate(calendar.getTimeInMillis());
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String dateStr = year + "/" + (month + 1) + "/" + dayOfMonth + " 23:59:59";
            Date date = null;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException ignored) {

            }
            if (date != null)
                view.setDate(date.getTime());
        });
        binding.btExpirationRegisterSubmit.setOnClickListener(v -> {
            String productBarcode = binding.etExpirationRegisterProductBarcode.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy??? MM??? dd???");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date(binding.calendarView.getDate());
            String dateStr = sdf1.format(date) + " 23:59:59";
            try {
                date = sdf2.parse(dateStr);
            } catch (ParseException ignored) {

            }
            if (productBarcode.isEmpty()) {
                Toast.makeText(getContext(), "????????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                return;
            } else if (productDao.getProductByBarcode(productBarcode) == null) {
                Toast.makeText(getContext(), "???????????? ?????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                return;
            } else if (expirationDateDao.getExpiration(productBarcode, sdf.format(date)) != null) {
                Toast.makeText(getContext(), "?????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            expirationDateDao.insert(new ExpirationDate(productBarcode, sdf.format(date)));
            mainActivity.removeFragment();
            mainActivity.refreshExpirationDateList();
        });
        return binding.getRoot();
    }
}