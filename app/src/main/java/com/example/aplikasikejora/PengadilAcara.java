package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PengadilAcara extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengadil_acara);
    }

    public void kembali(View view) {
        finish();
    }

    public void SenaraiAcara(View view) {
        startActivity(new Intent(getApplicationContext(), DisplayJadualAcaraPengadil.class));
    }

    public void KeputusanAcara(View view) {
        startActivity(new Intent(getApplicationContext(), KeputusanAcara1.class));
    }
}