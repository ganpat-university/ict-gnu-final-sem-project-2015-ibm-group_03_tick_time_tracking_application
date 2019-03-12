package com.example.abhi.tick;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class SignUpActivity extends AppCompatActivity {

    String ServerURL = "http://tickapplication.000webhostapp.com/api/index.php";
    String tempEmail, tempLastName, tempFirstName, tempPassword;
    ExtendedEditText email, firstName, lastName, password;
    Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = (ExtendedEditText) findViewById(R.id.edit_email);
        firstName = (ExtendedEditText) findViewById(R.id.edit_first_name);
        lastName = (ExtendedEditText) findViewById(R.id.edit_last_name);
        password = (ExtendedEditText) findViewById(R.id.edit_password);

        btn_sign_up = (Button) findViewById(R.id.button_sign_up_user);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onSignupFailed();
                    return;
                } else {
                    GetData();
                    InsertData(tempFirstName, tempLastName, tempEmail, tempPassword);
                    onSignupSuccess();
                }
            }
        });

    }

    public void GetData() {

        tempEmail = email.getText().toString();
        tempFirstName = firstName.getText().toString();
        tempLastName = lastName.getText().toString();
        tempPassword = password.getText().toString();
    }

    public void InsertData(final String fname, final String lname, final String email, final String password) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String firstNameHolder = fname;
                String lastNameHolder = lname;
                String emailHolder = email;
                String passwordHolder = password;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("first_name", firstNameHolder));
                nameValuePairs.add(new BasicNameValuePair("last_name", lastNameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", emailHolder));
                nameValuePairs.add(new BasicNameValuePair("password", passwordHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

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

        sendPostReqAsyncTask.execute(fname, lname, email, password);
    }

    public void onSignupSuccess() {
        btn_sign_up.setEnabled(true);
        GetData();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error in sign in", Toast.LENGTH_LONG).show();
        btn_sign_up.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        GetData();

        if (tempFirstName.isEmpty() || tempFirstName.length() < 3) {
            firstName.setError("at least 3 characters");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (tempLastName.isEmpty() || tempLastName.length() < 3) {
            lastName.setError("at least 3 characters");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (tempEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        return valid;
    }
}
