package sample.com.medicalrecordtrackingsystem;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ayush on 1/4/17
 */
public class AppointmentActivity extends AppCompatActivity {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private AppointmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_layout);
        ButterKnife.bind(this);

        mAdapter = new AppointmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(pager);
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setTitle(getString(R.string.appointments));
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class AppointmentPagerAdapter extends FragmentPagerAdapter{
        public AppointmentPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new PendingAppointmentFragment();
                    break;
                case 1:
                    fragment = new CompletedAppointmentFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.pending).toUpperCase(l);
                case 1:
                    return getString(R.string.completed).toUpperCase(l);
            }
            return null;
        }
    }
}
