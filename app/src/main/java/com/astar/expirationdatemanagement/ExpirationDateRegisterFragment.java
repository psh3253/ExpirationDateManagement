package com.astar.expirationdatemanagement;

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

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.FragmentExpirationDateRegisterBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpirationDateRegisterFragment extends Fragment {

    MainActivity mainActivity;
    FragmentExpirationDateRegisterBinding binding;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String dateStr = year + "/" + (month + 1) + "/" + dayOfMonth + " 23:59:59";
                Date date = null;
                try {
                    date = sdf.parse(dateStr);
                } catch (ParseException e) {

                }
                if(date != null)
                    view.setDate(date.getTime());
            }
        });
        binding.btExpirationRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productBarcode = binding.etExpirationRegisterProductBarcode.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date(binding.calendarView.getDate());
                Date currentDate = new Date(System.currentTimeMillis());
                String dateStr = sdf1.format(date) + " 23:59:59";
                try {
                    date = sdf2.parse(dateStr);
                } catch (ParseException e) {

                }
                if (productBarcode.isEmpty()) {
                    Toast.makeText(getContext(), "바코드 번호를 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (date.before(currentDate)) {
                    Toast.makeText(getContext(), "유통기한은 지난 날짜로 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                } else if(productDao.getProductByBarcode(productBarcode) == null) {
                    Toast.makeText(getContext(), "등록되지 않은 상품의 바코드 번호입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (expirationDateDao.getExpiration(productBarcode, sdf.format(date)) != null) {
                    Toast.makeText(getContext(), "이미 등록된 유통기한입니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                expirationDateDao.insert(new ExpirationDate(productBarcode, sdf.format(date)));
                mainActivity.removeFragment();
                mainActivity.refreshExpirationDateList();
            }
        });
        return binding.getRoot();
    }
}