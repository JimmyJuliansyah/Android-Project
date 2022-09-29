package com.jimmy.uas_perpustakaan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    public String email_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
    }

    public void doLogin(View view) {
        // Get text from email and password field
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        email_profil = email;
        new AsyncLogin().execute(email,password);
    }

    public void doSignup(View view) {
        Intent intent = new Intent(getBaseContext(),Signup.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        MyConfig ipadd;

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(ipadd.HOST+"/UAS_Android/login.php");
            } catch (MalformedURLException e) {
                return "exception";
            }try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1000);
                conn.setConnectTimeout(1000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d("Note",e1.toString());
                return "exception";
            }try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        Log.d("Note",result.toString());
                    }
                    return(result.toString());
                }else{
                    Log.d("Note", conn.getResponseMessage());
                    return("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Note",e.toString());
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase("true")){
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name",email_profil);
                startActivity(intent);
                MainActivity.this.finish();
            } else if (result.equalsIgnoreCase("false")){
                Toast.makeText(MainActivity.this, "Invalid email or password",
                        Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("exception") ||
                    result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(MainActivity.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
        }
    }
}