package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void KRLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginKetuaRumah.class));
    }

    public void PengadilLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginPengadil.class));
    }

    public void PengurusanLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginPengurusan.class));
    }
}