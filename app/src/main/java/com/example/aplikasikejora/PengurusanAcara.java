package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PengurusanAcara extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengurusan_acara);
    }

    public void kembali(View view) {
        finish();
    }

    public void daftarAcara(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterAcara.class));
    }

    public void KemasKiniAcara(View view) {
        startActivity(new Intent(getApplicationContext(), UpdateAcara1.class));
    }

    public void SenaraiAcara(View view) {
        startActivity(new Intent(getApplicationContext(), DisplayJadualAcaraPengadil.class));
    }
}