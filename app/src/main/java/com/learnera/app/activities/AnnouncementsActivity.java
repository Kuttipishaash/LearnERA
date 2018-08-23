package com.learnera.app.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.learnera.app.R;
import com.learnera.app.data.User;
import com.learnera.app.fragments.AnnouncementsKTUFragment;
import com.learnera.app.fragments.AnnouncementsRSETFragment;
import com.learnera.app.fragments.NetworkNotAvailableFragment;
import com.learnera.app.utils.Utils;

public class AnnouncementsActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager mViewPager;
    public static TabLayout tabLayout;
    public static FrameLayout network;
    public static Fragment ktuAnnouncementsFragment;
    public static Fragment rsetAnnouncementsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        rsetAnnouncementsFragment = new AnnouncementsRSETFragment();
        ktuAnnouncementsFragment = new AnnouncementsKTUFragment();
        network = findViewById(R.id.announcement_network);

        Fragment fragment = new NetworkNotAvailableFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.announcement_network, fragment);
        fragmentTransaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         Create the adapter that will return a fragment for each of the three
         primary sections of the activity.
        */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_about:
                Utils.showAbout(this);
                return true;
            case R.id.action_logout:
                User.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ktuAnnouncementsFragment;
                case 1:
                    return rsetAnnouncementsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "KTU";
                case 1:
                    return "RSET";
            }
            return null;
        }
    }


}
