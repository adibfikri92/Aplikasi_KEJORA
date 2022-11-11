package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class KRPaparKeputusan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_r_papar_keputusan);
    }

    public void kembali(View view) {
        finish();
    }

    public void KeputusanMengikutAcara(View view) {
        startActivity(new Intent(getApplicationContext(), KeputusanMengikutAcara.class));
    }

    public void KeputusanKeseluruhan(View view) {
        startActivity(new Intent(getApplicationContext(), KeputusanKeseluruhan1.class));
    }
}