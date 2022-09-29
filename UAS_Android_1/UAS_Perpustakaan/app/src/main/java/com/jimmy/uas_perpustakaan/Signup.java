package com.jimmy.uas_perpustakaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Signup extends AppCompatActivity {
    EditText txtName,txtEmail,txtPassword;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        b1=(Button)findViewById(R.id.btnBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void doRegister(View view) {
        final String name = txtName.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        new Signup.AsyncSignup().execute(name,email,password);
    }

    private class AsyncSignup extends AsyncTask<String, String, String> {
        URL url;
        MyConfig ipadd;
        HttpURLConnection conn;

        @Override
        protected String doInBackground(String... params){
            try{
                url = new URL(ipadd.HOST+"/UAS_Android/register.php");
            } catch(MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }try{
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("email", params[1])
                        .appendQueryParameter("password", params[2]);
                String qry = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new
                        OutputStreamWriter(os, "UTF-8"));
                writer.write(qry);
                writer.flush();
                writer.close();
                os.close();
            } catch (IOException e1){
                e1.printStackTrace();
                return "exception";
            }try{
                int response_code=conn.getResponseCode();
                if(response_code==HttpURLConnection.HTTP_OK){
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line=reader.readLine()) != null)
                        result.append(line);
                    return(result.toString());
                } else {
                    return("unsuccessful");
                }
            } catch(IOException e){
                e.printStackTrace();
                Log.d("Note",e.toString());
                return "exception";
            }
        }
        protected void onPostExecute(String result){
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(getBaseContext(),"Proses inserting berhasil!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                Signup.this.finish();
            } else if(result.equalsIgnoreCase("false")){
                Toast.makeText(getBaseContext(), "Your message here…",
                        Toast.LENGTH_LONG).show();
            } else if(result.equalsIgnoreCase("exception")) {
                Toast.makeText(getBaseContext(),"Your message here…",
                        Toast.LENGTH_LONG).show();
            } else if(result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(getBaseContext(),"Your message here…",
                        Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getBaseContext(),"Other problem!",
                        Toast.LENGTH_LONG).show();
        }
    }
}