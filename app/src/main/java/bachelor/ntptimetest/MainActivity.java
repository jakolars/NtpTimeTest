package bachelor.ntptimetest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {


    public static final String TIME_SERVER = "time-a.nist.gov";
    private long nTime=0;
    private long time=0;
    TextView view1;
    TextView view2;
    TextView view3;
    ListView nTimeList;
    ListView sTimeList;
    ListView diffList;
    //private Calendar calendarSystem= Calendar.getInstance();
    //private Calendar calendarNetwork=Calendar.getInstance();
    String timeStringS="";
    String timeStringN="";
    ArrayList<String> nTimes;
    ArrayAdapter<String> nTimesAdapter;
    ArrayList<String> sTimes;
    ArrayAdapter<String> sTimesAdapter;
    ArrayList<String> diff;
    ArrayAdapter<String> diffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nTimes= new ArrayList<String>();
        sTimes= new ArrayList<String>();
        diff= new ArrayList<String>();
        view1= (TextView)findViewById(R.id.textView1);
        view2= (TextView)findViewById(R.id.textView2);
        view3= (TextView)findViewById(R.id.textView3);
        nTimeList=(ListView)findViewById(R.id.listViewNTime);
        sTimeList=(ListView)findViewById(R.id.listViewSTime);
        diffList=(ListView)findViewById(R.id.listViewDiff);
        nTimesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nTimes);
        sTimesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sTimes);
        diffAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, diff);
        nTimeList.setAdapter(nTimesAdapter);
        sTimeList.setAdapter(sTimesAdapter);
        diffList.setAdapter(diffAdapter);


        new TimeTask(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static long getCurrentNetworkTime() throws Exception {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        //long returnTime = timeInfo.getReturnTime();   //local device time
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

        Date time = new Date(returnTime);
        //Log.d(TAG, "Time from " + TIME_SERVER + ": " + time);

        return returnTime;
    }


    private class TimeTask extends AsyncTask<String, Void, String> {
        private String mailResult="";
        private Context context;
        private Dialog mailDialog;
        private ProgressDialog dialog;
        private AlertDialog.Builder builder;




        public TimeTask(Context context){
            this.context=context;

        }


        @Override
        protected String doInBackground(String... params) {

            try {
                nTime=getCurrentNetworkTime();
                time= System.currentTimeMillis();

            } catch (Exception e) {
                e.printStackTrace();
            }
            /*if((calendarSystem.getTimeInMillis()-calendarNetwork.getTimeInMillis())>10000) {
                calendarSystem.setTimeInMillis(nTime);
            }
            calendarNetwork.setTimeInMillis(nTime);
            */

            timeStringN = "Network time: "+ nTime;
            timeStringS = "System time"+ time;




            return mailResult;
        }

        protected void onPreExecute() {


        }


        protected void onPostExecute(String result) {





            view1.setText(timeStringN);
            view2.setText(timeStringS);
            view3.setText("Diff: "+(nTime-time));
            nTimesAdapter.insert(timeStringN,0);
            sTimesAdapter.insert(timeStringS,0);
            diffAdapter.add("Diff: "+(nTime-time));
            try {
                RunTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }



    protected void RunTask() throws InterruptedException {

        Thread.sleep(3000);
        new TimeTask(this).execute();



    }








}
