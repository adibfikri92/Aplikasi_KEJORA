package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MenuSuperAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_super_admin);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }

    public void PengadilReg(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterPengadil.class));
    }

    public void KetuaRumahReg(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterKetuaRumah.class));
    }

    public void PengurusanReg(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterPengurusan.class));
    }
}