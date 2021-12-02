package com.astar.expirationdatemanagement.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astar.expirationdatemanagement.DBInfo;
import com.astar.expirationdatemanagement.MainActivity;
import com.astar.expirationdatemanagement.dao.ExpirationDateDao;
import com.astar.expirationdatemanagement.dao.ProductDao;
import com.astar.expirationdatemanagement.databinding.NotificationRecyclerBinding;
import com.astar.expirationdatemanagement.model.ExpirationDate;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder>{

    MainActivity mainActivity;
    ArrayList<ExpirationDate> expirationDateList = new ArrayList<>();

    public NotificationAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationRecyclerBinding binding = NotificationRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationHolder(binding.getRoot(), binding, mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        ExpirationDate expirationDate = expirationDateList.get(position);
        holder.setNotification(expirationDate);
    }

    @Override
    public int getItemCount() {
        return expirationDateList.size();
    }
}

class NotificationHolder extends RecyclerView.ViewHolder {

    MainActivity mainActivity;
    ProductDao productDao = DBInfo.appDatabase.productDao();
    ExpirationDateDao expirationDateDao = DBInfo.appDatabase.expirationDateDao();
    private final NotificationRecyclerBinding binding;

    public NotificationHolder(@NonNull View itemView, NotificationRecyclerBinding binding, MainActivity mainActivity) {
        super(itemView);
        this.binding = binding;
        this.mainActivity = mainActivity;
    }

    public void setNotification(ExpirationDate expirationDate) {
        String productName = productDao.getProductByBarcode(expirationDate.getProductBarcode()).getProductName();
        binding.tvNotificationProductName.setText(productName);
        binding.deleteButton.setOnClickListener(v -> {
            expirationDateDao.delete(expirationDate);
            mainActivity.refreshNotification();
        });
    }
}
