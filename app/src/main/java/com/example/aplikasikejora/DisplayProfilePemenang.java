package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DisplayProfilePemenang extends AppCompatActivity {

    TextView mRumahSukan, mNamaPeserta;
    String idTest,iDdoc,No,namapemenang,houseid;
    String Temp1,Temp2,Temp3,Temp4;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;

    int valid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile_pemenang);

        mNamaPeserta = findViewById(R.id.namapeserta);
        mRumahSukan = findViewById(R.id.wakilrumah);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        Intent data = getIntent();
        idTest = data.getStringExtra("IdAcara");
        iDdoc = data.getStringExtra("IdPeserta");
        No = data.getStringExtra("Number");

        fStore.collection("Sukan").document(idTest)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Temp1 = documentSnapshot.getString("Kategori");
                Temp2 = documentSnapshot.getString("NamaAcara");
                Temp3 = documentSnapshot.getString("Jantina");
                Temp4= Temp1 + " " + Temp2+ " "+ Temp3;
            }
        });

        fCollect.document(idTest).collection("Senarai Peserta")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String idPeserta = documentSnapshot.getId();
                    if(iDdoc.equals(idPeserta)){
                        Toast.makeText(DisplayProfilePemenang.this, "Peserta Yang Sah", Toast.LENGTH_SHORT).show();
                        fCollect.document(idTest).collection("Senarai Peserta")
                                .document(iDdoc).get();
                        mNamaPeserta.setText(documentSnapshot.getString("NamaPeserta"));
                        mRumahSukan.setText(documentSnapshot.getString("HouseID"));

                        namapemenang=documentSnapshot.getString("NamaPeserta");
                        houseid=documentSnapshot.getString("HouseID");

                        valid=1;

                    }
                }
                if(valid==0){
                    Toast.makeText(DisplayProfilePemenang.this, "Tiada Rekod", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Home(View view) {
        Toast.makeText(DisplayProfilePemenang.this, "Pengesahan Gagal", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void Home2(View v) {

        if(namapemenang != null){
            Map<String, Object> Keputusan = new HashMap<>();
            Keputusan.put("Number", No);
            Keputusan.put("NamaPemenang", namapemenang);
            Keputusan.put("HouseID", houseid);

            Map<String, Object> Keputusan1 = new HashMap<>();
            Keputusan1.put("NamaAcara",Temp4);
            Keputusan1.put("No"+No,namapemenang + " " + houseid);

            Map<String, Object> Keputusan2 = new HashMap<>();
            Keputusan2.put("No"+No,namapemenang + " " + houseid);

            fStore.collection("Sukan").document(idTest)
                    .collection("Keputusan")
                    .document(No).set(Keputusan).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    fStore.collection("Keputusan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if(documentSnapshot.getString("NamaAcara")==null){
                                    fStore.collection("Keputusan").document(idTest).set(Keputusan1);
                                }else{
                                    fStore.collection("Keputusan").document(idTest).update(Keputusan2);
                                }
                            }
                        }
                    });
                    Toast.makeText(DisplayProfilePemenang.this, "Berjaya Disahkan", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DisplayProfilePemenang.this, "Tidak Berjaya Disahkan", Toast.LENGTH_SHORT).show();
                        }
                    });


        }else{
            Toast.makeText(DisplayProfilePemenang.this, "Tiada Maklumat", Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(v.getContext(),KeputusanAcara2.class);
        i.putExtra("IdAcara",idTest);
        startActivity(i);
        finish();

    }
}