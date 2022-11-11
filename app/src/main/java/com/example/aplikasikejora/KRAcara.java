package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class KRAcara extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_r_acara);
    }

    public void kembali(View view) {
        finish();
    }

    public void SenaraiAcara(View view) {
        startActivity(new Intent(getApplicationContext(), DisplayAcaraList.class));
    }

    public void JadualAcara(View view) {
        startActivity(new Intent(getApplicationContext(), DisplayJadualAcara.class));
    }
}