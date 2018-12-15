package id.ac.uii.a16523169students.kumis;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ANDRI on 14/12/2018.
 */
public class PagerAdapterSS extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images ={R.drawable.diab1, R.drawable.diare2, R.drawable.diare3};

    public PagerAdapterSS(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, null);
        ImageView imageSlide = (ImageView) view.findViewById(R.id.imageSlide);
        imageSlide.setImageResource(images [position]);
        System.out.println("GAMBAR : " + images[position].toString());
        ViewPager vp =(ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position, Object object){
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}