package com.astar.expirationdatemanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astar.expirationdatemanagement.databinding.FragmentViewPagerBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPagerFragment extends Fragment {

    MainActivity mainActivity;
    FragmentViewPagerBinding binding;
    ProductListFragment productListFragment;
    ExpirationDateListFragment expirationDateListFragment;
    NotificationFragment notificationFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity)
            mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false);
        if (productListFragment == null)
            productListFragment = new ProductListFragment();
        if (expirationDateListFragment == null)
            expirationDateListFragment = new ExpirationDateListFragment();
        if (notificationFragment == null)
            notificationFragment = new NotificationFragment();

        ArrayList<Fragment> fragmentList = new ArrayList(Arrays.asList(productListFragment, expirationDateListFragment, notificationFragment));
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        adapter.fragmentList = fragmentList;
        binding.viewPager.setAdapter(adapter);

        ArrayList<String> tabTitles = new ArrayList<>(Arrays.asList("상품목록", "유통기한", "알림"));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (TabLayout.Tab tab, int position) -> tab.setText(tabTitles.get(position)));
        tabLayoutMediator.attach();

        return binding.getRoot();
    }

    public ProductListFragment getProductListFragment() {
        return productListFragment;
    }

    public ExpirationDateListFragment getExpirationDateListFragment() {
        return expirationDateListFragment;
    }

    public NotificationFragment getNotificationFragment() {
        return notificationFragment;
    }
}