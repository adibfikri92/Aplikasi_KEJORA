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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UpdateAcara1 extends AppCompatActivity {

    Spinner mAcaraUpdate;
    Button mSeterusnya;
    String idAcara,Acara,strDate,strTime;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_acara1);

        mAcaraUpdate = findViewById(R.id.AcaraUpdate);
        mSeterusnya= findViewById(R.id.AcaraUpdateBtn);

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

        mSeterusnya.setOnClickListener(new View.OnClickListener() {
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
                            String Temp5 = documentSnapshot.getString("Pengadil");
                            Date masaTarikh = documentSnapshot.getDate("Tarikh");
                            Date masaTarikh2 = documentSnapshot.getDate("Tarikh");
                            String Temp4 = Temp3 + " " + Temp1 + " " + Temp2;
                            if(Objects.equals(Acara, Temp4)) {
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:aa");
                                strDate = dateFormat.format(masaTarikh);
                                strTime = dateFormat2.format(masaTarikh2);
                                Integer Bil = documentSnapshot.getLong("BilPeserta").intValue();
                                idAcara = documentSnapshot.getId();
                                Intent i = new Intent(v.getContext(),UpdateAcara2.class);
                                i.putExtra("IdAcara",idAcara);
                                i.putExtra("Jantina",Temp2);
                                i.putExtra("Kategori",Temp3);
                                i.putExtra("Pengadil",Temp5);
                                i.putExtra("NamaAcara",Temp1);
                                i.putExtra("Bil",Bil);
                                i.putExtra("Date",strDate);
                                i.putExtra("Time",strTime);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }
}