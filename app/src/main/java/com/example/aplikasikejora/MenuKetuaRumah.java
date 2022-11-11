package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MenuKetuaRumah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ketua_rumah);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }

    public void KetuaRumahProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileKetuaRumah.class));
    }

    public void PaparanMarkahSemasa(View view) {
        startActivity(new Intent(getApplicationContext(), KRPaparKeputusan.class));
    }

    public void Acara(View view) {
        startActivity(new Intent(getApplicationContext(), KRAcara.class));
    }

    public void Peserta(View view) {
        startActivity(new Intent(getApplicationContext(), KRPeserta.class));
    }
}