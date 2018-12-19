package id.ac.uii.a16523169students.kumis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab_last_over extends Fragment {

    private TextView tKet, tPenyebab, tObat, tPantangan, tVitamin, tMakanan, tIdeal, tDehi;

    public tab_last_over() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeActivity activity = (HomeActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_last_over, container, false);
        tKet = (TextView) view.findViewById(R.id.teksKeteranganDiaree);
        tPenyebab = (TextView) view.findViewById(R.id.teksPenyebabDiaree);
        tObat = (TextView) view.findViewById(R.id.teksObatDiaree);
        tPantangan = (TextView) view.findViewById(R.id.teksPantanganDiaree);
        tVitamin = (TextView) view.findViewById(R.id.teksVitaminn);
        tMakanan = (TextView) view.findViewById(R.id.teksRekomendasii);
        tIdeal = (TextView) view.findViewById(R.id.teksBBTBB);
        tDehi = (TextView) view.findViewById(R.id.teksDehii);

        tKet.setText(activity.sendKet());
        tPenyebab.setText(activity.sendPenyebab());
        tObat.setText(activity.sendObat());
        tPantangan.setText(activity.sendPantangan());
        tVitamin.setText(activity.sendVit());
        tMakanan.setText(activity.sendRek());
        tIdeal.setText(activity.sendIde());
        tDehi.setText(activity.sendDehi());

        return view;
    }

}
