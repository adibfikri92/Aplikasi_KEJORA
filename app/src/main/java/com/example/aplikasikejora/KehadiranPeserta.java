package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KehadiranPeserta extends AppCompatActivity {

    Spinner mAcaraUpdate;
    Button mUpdatePeserta;
    String idAcara,Acara;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran_peserta);

        mAcaraUpdate = findViewById(R.id.KehadiranAcara);
        mUpdatePeserta= findViewById(R.id.KehadiranPesertaBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAcaraUpdate.setAdapter(dataAdapter);
        fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String NamaAcara = documentSnapshot.getString("NamaAcara");
                    String Jantina = documentSnapshot.getString("Jantina");
                    String Kategori = documentSnapshot.getString("Kategori");
                    list.add(Kategori + " " +  NamaAcara + " " + Jantina);
                }
                dataAdapter.notifyDataSetChanged();
            }
        });

        mUpdatePeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acara=String.valueOf(mAcaraUpdate.getSelectedItem());

                fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String Temp1 = documentSnapshot.getString("NamaAcara");
                            String Temp2 = documentSnapshot.getString("Jantina");
                            String Temp3 = documentSnapshot.getString("Kategori");
                            String Temp4 = Temp3 + " " + Temp1 + " " + Temp2;
                            if(Objects.equals(Acara, Temp4)) {
                                idAcara = documentSnapshot.getId();
                                Intent i = new Intent(v.getContext(),DisplayKehadiranPeserta.class);
                                i.putExtra("IdAcara",idAcara);
                                startActivity(i);
                            }
                        }
                    }
                });
            }
        });

    }
}