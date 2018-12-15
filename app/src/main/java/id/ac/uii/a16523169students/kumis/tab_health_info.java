package id.ac.uii.a16523169students.kumis;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_health_info extends Fragment {


    ViewPager pager;
    public tab_health_info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View newView = inflater.inflate(R.layout.fragment_health_infor, container, false);

        pager = (ViewPager) newView.findViewById(R.id.pagerSlide);
        PagerAdapterSS viewPagerAdapter = new PagerAdapterSS(newView.getContext());
        pager.setAdapter(viewPagerAdapter);

        return newView;
    }

}
