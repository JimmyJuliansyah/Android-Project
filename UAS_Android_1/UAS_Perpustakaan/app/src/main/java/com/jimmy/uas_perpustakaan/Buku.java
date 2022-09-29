package com.jimmy.uas_perpustakaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Buku extends AppCompatActivity {
    public String email_profil, kd_buku, judul_buku, text_singkat, penulis, penerbit;
    public List<String> list;
    ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);

        Intent intent = getIntent();
        email_profil = intent.getStringExtra("name");

        lstView = findViewById(R.id.list1);
        lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstView.setTextFilterEnabled(true);

        new Asyncgetdata().execute();
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

    private class Asyncgetdata extends AsyncTask<String, String, String> {
        MyConfig ipadd;
        URL url;
        HttpURLConnection connect = null;

        @Override
        protected String doInBackground(String... strings) {
            try{
                url = new URL(ipadd.HOST+"/UAS_Android/buku.php");
                connect = (HttpURLConnection) url.openConnection();
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.d("Note", result.toString());
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            finally {
                connect.disconnect();
            }
        }

        protected void onPostExecute(String s){
            try{
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("buku");
                list = new ArrayList<String>();
                String[] kode = new String[array.length()];
                for(int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    kd_buku = object2.getString("Kd_buku");
                    judul_buku = object2.getString("Judul_buku");
                    text_singkat = object2.getString("Text_singkat");
                    penulis = object2.getString("Penulis");
                    penerbit = object2.getString("Penerbit");
                    kode[i] = kd_buku;
                    list.add("\nJudul Buku : " + judul_buku + "\n\n" + text_singkat + "\n\n Penulis : " + penulis + "\n\n Penerbit : " + penerbit + "\n");
                }
                lstView.setAdapter(new ArrayAdapter<String>(Buku.this,android.R.layout.simple_list_item_1, list));
                lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new AsyncHistory().execute(kode[position], email_profil);
                        Intent intent = new Intent(getBaseContext(),Info.class);
                        intent.putExtra("kode",kode[position]);
                        intent.putExtra("name",email_profil);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                Log.d("Note",e.toString());
                e.printStackTrace();
            }
        }
    }

    private class AsyncHistory extends AsyncTask<String, String, String> {
        URL url;
        MyConfig ipadd;
        HttpURLConnection conn;
        @Override
        protected String doInBackground(String... params){
            try{
                url = new URL(ipadd.HOST+"/UAS_Android/history.php");
            } catch(MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }
            try{
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("kd_buku", params[0])
                        .appendQueryParameter("email", params[1]);
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
        } //the end of doInBackground() method
        protected void onPostExecute(String result){
            if(result.equalsIgnoreCase("true")){

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