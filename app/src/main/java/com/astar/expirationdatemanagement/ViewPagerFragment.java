package com.astar.expirationdatemanagement;

import android.os.Bundle;

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

    FragmentViewPagerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false);

        ArrayList<Fragment> fragmentList = new ArrayList(Arrays.asList(new ProductListFragment(), new ExpirationDateListFragment(), new ExpirationDateListFragment()));
        FragmentAdapter adapter = new FragmentAdapter(getActivity());
        adapter.fragmentList = fragmentList;
        binding.viewPager.setAdapter(adapter);

        ArrayList<String> tabTitles = new ArrayList<>(Arrays.asList("상품목록", "유통기한", "알림"));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (TabLayout.Tab tab, int position) -> tab.setText(tabTitles.get(position)));
        tabLayoutMediator.attach();

        return binding.getRoot();
    }
}