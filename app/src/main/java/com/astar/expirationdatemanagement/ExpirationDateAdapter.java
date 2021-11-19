package com.astar.expirationdatemanagement;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.ExpirationRecyclerBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpirationDateAdapter extends RecyclerView.Adapter<ExpirationDateHolder> {

    MainActivity mainActivity;
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();
    ArrayList<ExpirationDate> expirationDateList = new ArrayList<>();

    public ExpirationDateAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ExpirationDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpirationRecyclerBinding binding = ExpirationRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExpirationDateHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpirationDateHolder holder, int position) {
        ExpirationDate expirationDate = expirationDateList.get(position);
        holder.setExpirationDate(expirationDate);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("삭제").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        expirationDateDao.delete(expirationDate);
                        mainActivity.refreshExpirationDateList();
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return expirationDateList.size();
    }
}

class ExpirationDateHolder extends RecyclerView.ViewHolder {

    private final ExpirationRecyclerBinding binding;
    ProductDao productDao = DBInfo.appDatabase.productDao();

    public ExpirationDateHolder(@NonNull View itemView, ExpirationRecyclerBinding binding) {
        super(itemView);
        this.binding = binding;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        binding.tvExpirationProductName.setText(productDao.getProductByBarcode(expirationDate.getProductBarcode()).getProductName());
        binding.tvExpiration.setText(expirationDate.getExpirationDate());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(expirationDate.getExpirationDate() + " 23:59:59");
        } catch (ParseException e) {

        }
        if (date != null) {
            Log.d("TestTest", new Date(date.getTime()).toString());
            Log.d("TestTest", new Date(System.currentTimeMillis()).toString());
            int deadLine = (int) Math.abs((date.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000));
            String deadLineStr = deadLine + "일";
            binding.tvDeadline.setText(deadLineStr);
        }
    }
}
