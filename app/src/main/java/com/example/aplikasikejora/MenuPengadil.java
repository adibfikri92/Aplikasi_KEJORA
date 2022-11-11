package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MenuPengadil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pengadil);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }

    public void PengesahanPeserta(View view) {
        startActivity(new Intent(getApplicationContext(), PengadilPeserta.class));


    }

    public void KeputusanAcara(View view) {
        startActivity(new Intent(getApplicationContext(), PengadilAcara.class));

    }

    public void PengadilnProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfilePengadil.class));
    }
}