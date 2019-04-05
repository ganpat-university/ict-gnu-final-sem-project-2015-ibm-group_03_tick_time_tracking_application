package com.example.abhi.tick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class AdminActivity extends AppCompatActivity {

    Button btn_login;
    ExtendedEditText username, password;
    String tempEmail, tempError, tempPass, text, tempId;
    int responseCode, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        username = (ExtendedEditText) findViewById(R.id.edit_email);
        password = (ExtendedEditText) findViewById(R.id.edit_password);

        btn_login = (Button) findViewById(R.id.button_login_main);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onSignupFailed();
                    return;
                } else {
                    new SendPostRequest().execute();
                }
            }
        });
    }

    public void GetData() {
        tempEmail = username.getText().toString();
        tempPass = password.getText().toString();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                /*SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putBoolean("activity_executed", true);
                edt.commit();*/

                URL url = new URL("http://tickapplication.000webhostapp.com/api/adminsignin.php");

                GetData();
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", tempEmail);
                postDataParams.put("password", tempPass);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                Log.d("writer", writer.toString());
                writer.flush();
                writer.close();
                os.close();

                responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject demo = new JSONObject(result);
                tempError = demo.getString("error");
                Log.d("temError" ,tempError);
                if (tempError == "false") {
                    Log.d("Email", tempEmail + ":" + tempPass);
                    onSignupSuccess();
                } else {
                    onSignupFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    public void onSignupSuccess() {
        btn_login.setEnabled(true);
        //GetData();
        Intent intent_login = new Intent(getApplicationContext(), EmployeeListActivity.class);
        startActivity(intent_login);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error in Login", Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        GetData();

        if (tempEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
            username.setError("enter a valid email address");
            valid = false;
        } else {
            username.setError(null);
        }

        if (tempPass.isEmpty()) {
            password.setError("Enter Password");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}

