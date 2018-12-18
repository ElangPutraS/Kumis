package id.ac.uii.a16523169students.kumis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListCaseActivity extends Activity  implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {
    private String apiPathCBR = "https://kumisproject.000webhostapp.com/RestController.php?view=tabelCBR";
    private JSONArray resultJsonArrayCBR;
    private boolean resultBoolCBR = false;
    private int successCBR = 0;
    private ProgressDialog processDialog;
    private Toast toast;
    private ArrayList<Cases> m_partsCBR = new ArrayList<Cases>();
    private JSONObject resultJsonObjectCBR;

    private Runnable viewPartsCBR;
    private ListCaseAdapter m_adapterCBR;
    private TextView emptlist;
    private ListView listOfCases;
    private SwipeRefreshLayout pullToRefreshLayoutCBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_case);

        //listview
        listOfCases = (ListView) findViewById(R.id.list_of_cases);

        //refresh layout
        pullToRefreshLayoutCBR = (SwipeRefreshLayout) findViewById(R.id.activity_list_case);
        pullToRefreshLayoutCBR.setOnRefreshListener(this);

        emptlist = (TextView) findViewById(R.id.emptylistt);
        emptlist.setVisibility(View.INVISIBLE);

        m_adapterCBR = new ListCaseAdapter(this, R.layout.caselist, m_partsCBR);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ServiceCBRAsyncTask(this, this).execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onRefresh() {
        listOfCases.setAdapter(null);
        new ServiceCBRAsyncTask(this, this).execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayoutCBR.setRefreshing(false);
            }
        }, 2000);
    }

    private Handler handlerCBR = new Handler()
    {
        public void handleMessage(Message msg)
        {
            try {
                System.out.println("IKILOHHHH : "+resultJsonArrayCBR.length() + " " + resultJsonArrayCBR.get(0));
                int bGejala = 0, idKasus = 0;
                String ket = "", penyebab = "";

                for (int i = 0 ; i < resultJsonArrayCBR.length(); i++){
                    JSONObject objTabel = resultJsonArrayCBR.getJSONObject(i);
                    bGejala = 0;
                    System.out.println("Data ke-"+i);
                    for(int j = 1; j <= 28; j++ ){
                        String tmp = "G"+j;
                        if(objTabel.getInt(tmp) > 0){
                            bGejala += 1;
                        }
                    }
                    ket = objTabel.getString("Keterangan");
                    penyebab = objTabel.getString("Penyebab");
                    idKasus = objTabel.getInt("id");

                    m_partsCBR.add(new Cases(bGejala, ket, penyebab, idKasus));
                    System.out.println("ININAHH ANJAYYYY "+bGejala+" "+ket+" "+penyebab);
                }
                m_adapterCBR = new ListCaseAdapter(ListCaseActivity.this, R.layout.caselist, m_partsCBR);
                if (m_adapterCBR == null)
                    emptlist.setVisibility(View.VISIBLE);
                else
                    listOfCases.setAdapter(m_adapterCBR);
                // display the list.
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listOfCases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(ListCaseActivity.this, DetailCaseActivity.class);
                    TextView idKasus = (TextView) view.findViewById(R.id.idKasus);
                    String idcase = idKasus.getText().toString();
                    System.out.println("IKIIII : "+idcase.substring(12,idcase.length()));
                    i.putExtra("ID_KASUS", Integer.parseInt(idcase.substring(12,idcase.length())));
                    startActivity(i);
                }
            });
        }
    };

    private class ServiceCBRAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams;

        public ServiceCBRAsyncTask(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");


            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(apiPathCBR, postDataParams);

            try {
                successCBR = 1;
                resultJsonObjectCBR = new JSONObject(response);
                resultJsonArrayCBR = resultJsonObjectCBR.getJSONArray("output");

                m_partsCBR.clear();
                // here we are defining our runnable thread.
                viewPartsCBR = new Runnable(){
                    public void run(){
                        handlerCBR.sendEmptyMessage(0);
                    }
                };

                // here we call the thread we just defined - it is sent to the handler below.
                Thread thread =  new Thread(null, viewPartsCBR, "MagentoBackground");
                thread.start();
            } catch (JSONException e) {
                successCBR = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

        }

    }//end of async task
}
