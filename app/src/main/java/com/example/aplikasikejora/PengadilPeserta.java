package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PengadilPeserta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengadil_peserta);
    }

    public void kembali(View view) {
        finish();
    }

    public void KehadiranPeserta(View view) {
        startActivity(new Intent(getApplicationContext(), KehadiranPeserta.class));
    }

    public void SahPeserta(View view) {
        startActivity(new Intent(getApplicationContext(), PengadilSahPeserta.class));
    }
}