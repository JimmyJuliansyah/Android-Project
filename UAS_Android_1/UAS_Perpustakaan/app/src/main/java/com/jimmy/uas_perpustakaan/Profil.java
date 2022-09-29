package com.jimmy.uas_perpustakaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

public class Profil extends AppCompatActivity {
    public String email_profil, id;
    TextView Email;
    EditText  Password;
    String profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Intent intent = getIntent();
        email_profil = intent.getStringExtra("name");
        profil = email_profil;
        Email = findViewById(R.id.email_login);
        Email.setText(profil);
        Password = findViewById(R.id.password_login);
    }

    public void Change(View view) {
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();
        new Profil.AsyncUpdate().execute(email,password);
    }

    public void LogOut(View view) {
        Intent intent = new Intent(Profil.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_beranda:
                Intent intent1 = new Intent(getBaseContext(),SecondActivity.class);
                intent1.putExtra("name",email_profil);
                startActivity(intent1);
                return true;
            case R.id.action_profil:
                Intent intent2 = new Intent(getBaseContext(),Profil.class);
                intent2.putExtra("name",email_profil);
                startActivity(intent2);
                return true;
            case R.id.action_buku:
                Intent intent3 = new Intent(getBaseContext(),Buku.class);
                intent3.putExtra("name",email_profil);
                startActivity(intent3);
                return true;
            case R.id.action_history:
                Intent intent4 = new Intent(getBaseContext(),History.class);
                intent4.putExtra("name",email_profil);
                startActivity(intent4);
                return true;
            case R.id.action_credits:
                Intent intent5 = new Intent(getBaseContext(),Credits.class);
                intent5.putExtra("name",email_profil);
                startActivity(intent5);
                return true;
        }
        return false;
    }

    private class AsyncUpdate extends AsyncTask<String, String, String> {
        URL url;
        MyConfig ipadd;
        HttpURLConnection conn;

        @Override
        protected String doInBackground(String... params){
            try{
                url = new URL(ipadd.HOST+"/UAS_Android/update.php");
            } catch(MalformedURLException e){
                e.printStackTrace();
                Log.d("Note",e.toString());
                return "exception";
            }try{
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("password", params[1]);
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
                Log.d("Note",e1.toString());
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
            Log.d("Note",result);
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(getBaseContext(),"Proses Update berhasil!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),Profil.class);
                intent.putExtra("name",email_profil);
                startActivity(intent);
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