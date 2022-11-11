package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class KRPeserta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_r_peserta);
    }

    public void kembali(View view) {
        startActivity(new Intent(getApplicationContext(), MenuKetuaRumah.class));
    }

    public void PendaftaranPesertaBaru(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterPeserta2.class));
    }

    public void KemasKiniPeserta(View view) {
        startActivity(new Intent(getApplicationContext(), UpdatePeserta1.class));
    }

    public void SenaraiPeserta(View view) {
        startActivity(new Intent(getApplicationContext(), DisplayPesertaList.class));
    }
}