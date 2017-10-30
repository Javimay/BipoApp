package com.bipo.javier.bipo.report.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.fragments.EventsFragment;
import com.bipo.javier.bipo.report.utilities.TabRecovered;
import com.bipo.javier.bipo.report.utilities.TabStolen;
import com.bipo.javier.bipo.report.utilities.TabViews;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportBikesFragment extends Fragment  {

    private ReportBikesFragment.SectionsPagerAdapter adapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    public ReportBikesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_bikes, container, false);
        mViewPager = (ViewPager)view.findViewById(R.id.pager);
        adapter = new SectionsPagerAdapter(getFragmentManager());
        setupViewPager(mViewPager, adapter);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setHasOptionsMenu(true);
        return view;
    }

    public void setupViewPager(ViewPager viewPager, ReportBikesFragment.SectionsPagerAdapter adapter){

        adapter.addFragment(new TabStolen(), "ROBADAS");
        adapter.addFragment(new TabViews(), "VISTAS");
        adapter.addFragment(new TabRecovered(), "RECUPERADAS");
        adapter.addFragment(new EventsFragment(), "Ultimas");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> listFragments = new ArrayList<>();
        private final List<String> listTitles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public void addFragment(Fragment fragment, String title){

            listFragments.add(fragment);
            listTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {

            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return listTitles.get(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
 }
