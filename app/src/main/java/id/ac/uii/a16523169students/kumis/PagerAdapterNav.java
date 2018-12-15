package id.ac.uii.a16523169students.kumis;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ANDRI on 14/12/2018.
 */

public class PagerAdapterNav extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterNav(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new tab_health_info();
            case 1:
                return new tab_last_over();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}