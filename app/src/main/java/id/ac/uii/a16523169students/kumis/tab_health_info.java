package id.ac.uii.a16523169students.kumis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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
        View newView = inflater.inflate(R.layout.fragment_health_infor, container, false);

        pager = (ViewPager) newView.findViewById(R.id.pagerSlide);
        PagerAdapterSS viewPagerAdapter = new PagerAdapterSS(newView.getContext());
        pager.setAdapter(viewPagerAdapter);

        ImageButton img1 = (ImageButton) newView.findViewById(R.id.img1);
        ImageButton img2 = (ImageButton) newView.findViewById(R.id.img2);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl( "https://hellosehat.com/penyakit/diare/");
            }
            private void goToUrl (String url) {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl( "https://hellosehat.com/parenting/kesehatan-anak/pertolongan-pertama-anak-diare/");
            }
            private void goToUrl (String url) {
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        return newView;
    }
}