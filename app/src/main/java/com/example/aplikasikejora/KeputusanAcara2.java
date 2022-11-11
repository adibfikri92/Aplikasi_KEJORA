package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class KeputusanAcara2 extends AppCompatActivity {

    TextView mNama1,mNama2,mNama3;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    TextView mDisplayAcara;

    String idTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keputusan_acara2);

        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        mDisplayAcara = findViewById(R.id.DisplayAcara1);
        mNama1 = findViewById(R.id.Nama1);
        mNama2 = findViewById(R.id.Nama2);
        mNama3 = findViewById(R.id.Nama3);

        Intent data = getIntent();
        idTest = data.getStringExtra("IdAcara");

        fCollect.document(idTest).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String namaAcara = documentSnapshot.getString("NamaAcara");
                String Jantina = documentSnapshot.getString("Jantina");
                String Kategori = documentSnapshot.getString("Kategori");
                mDisplayAcara.setText(Kategori + " " +namaAcara + " " + Jantina);

            }
        });

        fCollect.document(idTest).collection("Keputusan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    if(documentSnapshot.getString("Number").equals("1")){
                        mNama1.setText(documentSnapshot.getString("NamaPemenang")+ " "+documentSnapshot.getString("HouseID") );
                    }else if(documentSnapshot.getString("Number").equals("2")){
                        mNama2.setText(documentSnapshot.getString("NamaPemenang") +" "+documentSnapshot.getString("HouseID"));
                    }else{
                        mNama3.setText(documentSnapshot.getString("NamaPemenang")+ " "+documentSnapshot.getString("HouseID"));
                    }
                }
            }
        });


    }

    public void Up2(View v) {
        Intent i = new Intent(v.getContext(), KeputusanQrScanner.class);
        i.putExtra("IdAcara",idTest);
        i.putExtra("Number","2");
        startActivity(i);
        finish();
    }

    public void Up3(View v) {
        Intent i = new Intent(v.getContext(), KeputusanQrScanner.class);
        i.putExtra("IdAcara",idTest);
        i.putExtra("Number","3");
        startActivity(i);
        finish();
    }

    public void Up1(View v){
        Intent i = new Intent(v.getContext(), KeputusanQrScanner.class);
        i.putExtra("IdAcara",idTest);
        i.putExtra("Number","1");
        startActivity(i);
        finish();
    }


    public void Home4(View view) {
        finish();
    }
}