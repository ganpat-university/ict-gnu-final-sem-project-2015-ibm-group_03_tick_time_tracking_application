package com.example.abhi.tick;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends AppCompatActivity {

    String ServerURL = "http://tickapplication.000webhostapp.com/api/userdetail.php?id=";
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    TextView timer;
    Button btn_checkout;
    String location, id, work, time;
    String tempLocation, tempWork, tempTime;
    String FULL_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        work = intent.getStringExtra("work");
        id = intent.getStringExtra("id");
        Log.d("location", location+work+id);

        FULL_URL = ServerURL+id;

        timer = (TextView) findViewById(R.id.tvTimer);
        btn_checkout = (Button) findViewById(R.id.btn_check_out);

        handler = new Handler();

        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("seconds : ", String.valueOf(Seconds));
                //Log.d("Minutes : ", String.valueOf(Minutes));

                if (!validate()) {
                    onSignupFailed();
                    return;
                } else {
                    GetData();
                    InsertData(tempLocation, tempWork, tempTime);
                    onSignupSuccess();
                }

            }
        });
    }

    public void GetData() {

        tempLocation = location;
        tempWork = work;
        tempTime = time;
    }

    public void InsertData(final String location, final String work, final String time) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String LocationHolder = location;
                String WorkHolder = work;
                String TimeHolder = time;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("location", LocationHolder));
                nameValuePairs.add(new BasicNameValuePair("work", WorkHolder));
                nameValuePairs.add(new BasicNameValuePair("time", TimeHolder));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(FULL_URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(location, work, time);
    }

    @Override
    public void onBackPressed() {

    }

    public void onSignupSuccess() {
        btn_checkout.setEnabled(true);
        GetData();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error in sign in", Toast.LENGTH_LONG).show();
        btn_checkout.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        return valid;
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);

            time = String.valueOf(Minutes) + " Minutes : " + String.valueOf(Seconds) + " Seconds";

        }
    };
}
