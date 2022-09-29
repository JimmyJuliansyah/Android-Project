package com.jimmy.uas_perpustakaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Credits extends AppCompatActivity {
    public String email_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        Intent intent = getIntent();
        email_profil = intent.getStringExtra("name");
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
}