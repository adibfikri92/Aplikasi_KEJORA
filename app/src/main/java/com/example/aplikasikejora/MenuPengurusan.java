package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MenuPengurusan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pengurusan);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }

    public void PengurusanProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfilePengurusan.class));
    }

    public void AcaraReg(View view) {
        startActivity(new Intent(getApplicationContext(), PengurusanAcara.class));
    }


    public void AcaraUpdate(View view) {
        startActivity(new Intent(getApplicationContext(), KehadiranPeserta.class));
    }

    public void KeputusanAcara(View view) {
        startActivity(new Intent(getApplicationContext(), KeputusanKeseluruhan1.class));
    }
}