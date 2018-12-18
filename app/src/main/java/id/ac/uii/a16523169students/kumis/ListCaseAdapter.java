package id.ac.uii.a16523169students.kumis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

public class ListCaseAdapter extends ArrayAdapter<Cases> {

    // declaring our ArrayList of items
    private ArrayList<Cases> objects;

    /* here we must override the constructor for ArrayAdapter
     * the only variable we care about now is ArrayList<Item> objects,
     * because it is the list of objects we want to display.
     */
    public ListCaseAdapter(Context context, int textViewResourceId, ArrayList<Cases> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.caselist, null);
        }

        /*
         * Recall that the variable position is sent in as an argument to this method.
         * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
         * iterates through the list we sent it)
         *
         * Therefore, i refers to the current Item object.
         */
        Cases i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.
            TextView mId = (TextView) v.findViewById(R.id.idKasus);
            TextView mGejala = (TextView) v.findViewById(R.id.banyakGejala);
            TextView mKet = (TextView) v.findViewById(R.id.ket);
            TextView mPenyebab = (TextView) v.findViewById(R.id.penyebab);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (mGejala != null){
                mGejala.setText("Banyak Gejala : "+i.getBanyakGejala());
            }
            if (mId != null){
                mId.setText("ID Kasus : K"+i.getIdKasus());
            }
            if (mKet != null){
                if(i.getKet().length() > 70){
                    mKet.setText("\nKeterangan : \n\n"+i.getKet().substring(0,70)+"...\n");
                }
                else {
                    mKet.setText("\nKeterangan : \n\n"+i.getKet()+"\n");
                }
            }
            if (mPenyebab != null){
                if(i.getPenyebab().length() > 70){
                    mPenyebab.setText("Penyebab : \n\n"+i.getPenyebab().substring(0,70)+"...\n");
                }
                else {
                    mPenyebab.setText("Penyebab : \n\n"+i.getPenyebab()+"\n");
                }
            }
        }

        // the view must be returned to our activity
        return v;

    }

}

