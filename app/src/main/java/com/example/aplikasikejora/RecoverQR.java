package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecoverQR extends AppCompatActivity {

    Spinner mPesertaRecover;
    Button mRecoverQR;
    String Peserta,userID,houseID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_q_r);

        mPesertaRecover = findViewById(R.id.RecoverPeserta);
        mRecoverQR= findViewById(R.id.RecoverPesertaBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID  =    fAuth.getCurrentUser().getUid();
        fCollect = fStore.collection("KetuaRumah").document(userID).collection("Senarai Peserta");

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPesertaRecover.setAdapter(dataAdapter);
        fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    list.add("Tiada Data");
                }else{
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String NamaPeserta = documentSnapshot.getString("NamaPeserta");
                        String NoIc = documentSnapshot.getString("ICNumber");
                        list.add(NamaPeserta+ " " + NoIc);

                    }
                }
                dataAdapter.notifyDataSetChanged();
            }
        });

        mRecoverQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Peserta=String.valueOf(mPesertaRecover.getSelectedItem());
                if(Peserta.equals("Tiada Data")){
                    Toast.makeText(RecoverQR.this, "Tiada Data", Toast.LENGTH_SHORT).show();
                }else{
                    fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String Temp1 = documentSnapshot.getString("NamaPeserta");
                                String Temp2 = documentSnapshot.getString("ICNumber");
                                String Temp4 =Temp1 + " " + Temp2;
                                if(Objects.equals(Peserta, Temp4)) {
                                    Intent i = new Intent(v.getContext(),DisplayQR.class);
                                    i.putExtra("idDoc",Temp2);
                                    i.putExtra("NamaPeserta",Temp1);
                                    i.putExtra("NamaAcara"," ");
                                    i.putExtra("Kategori"," ");
                                    i.putExtra("Jantina"," ");
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    });
                }
            }
        });

    }
}