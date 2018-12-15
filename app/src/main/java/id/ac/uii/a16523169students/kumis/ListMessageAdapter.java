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

public class ListMessageAdapter extends ArrayAdapter<ChatMessage> {

    // declaring our ArrayList of items
    private ArrayList<ChatMessage> objects;
    private List<ChatMessage> objectsList = new ArrayList<>();

    /* here we must override the constructor for ArrayAdapter
     * the only variable we care about now is ArrayList<Item> objects,
     * because it is the list of objects we want to display.
     */
    public ListMessageAdapter(Context context, int textViewResourceId, ArrayList<ChatMessage> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.objectsList = objects;
        Collections.sort(this.objectsList, DATE_ORDER);
    }

    private static Comparator<ChatMessage> DATE_ORDER = new Comparator<ChatMessage>() {
        @Override
        public int compare(ChatMessage o1, ChatMessage o2) {
            String d1 = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    o1.getMessageTime()).toString();
            String d2 = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    o2.getMessageTime()).toString();
            return d1.compareTo(d2) == -1 ? 1 : (d1.compareTo(d2) == 1 ? -1 : 0);
        }
    };

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
            v = inflater.inflate(R.layout.messagelist, null);
        }

        /*
         * Recall that the variable position is sent in as an argument to this method.
         * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
         * iterates through the list we sent it)
         *
         * Therefore, i refers to the current Item object.
         */
        ChatMessage i = objectsList.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView mText = (TextView) v.findViewById(R.id.mText);
            TextView mUser = (TextView) v.findViewById(R.id.mUser);
            TextView mTime = (TextView) v.findViewById(R.id.mTime);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (mText != null){
                if(i.getMessageText().length() > 70){
                    mText.setText(i.getMessageText().substring(0,70)+"...");
                }
                else {
                    mText.setText(i.getMessageText());
                }
            }
            if (mTime != null){
                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        i.getMessageTime()));
            }
            if (mUser != null){
                mUser.setText(i.getMessageUser());

                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(String.valueOf(i.getMessageUser().toString().charAt(0)), color);

                ImageView image = (ImageView) v.findViewById(R.id.image_view);
                image.setImageDrawable(drawable);
            }
        }

        // the view must be returned to our activity
        return v;

    }

}
