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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdatePeserta2 extends AppCompatActivity {

    Spinner mPesertaUpdate;
    Button mUpdatePeserta1;
    String Peserta,userID,houseID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    int validID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_peserta2);

        Intent data = getIntent();
        String idTest = data.getStringExtra("IdAcara");
        Integer Bil = data.getIntExtra("BilPeserta",0);

        mPesertaUpdate = findViewById(R.id.SenaraiPesertaUpdate);
        mUpdatePeserta1= findViewById(R.id.UpdatePesertaBtn2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan").document(idTest).collection("Senarai Peserta");

        userID  =    fAuth.getCurrentUser().getUid();

        fStore.collection("KetuaRumah").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                houseID = documentSnapshot.getString("KR");
            }
        });

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPesertaUpdate.setAdapter(dataAdapter);
        fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                validID=0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String TempID = documentSnapshot.getString("HouseID");
                    if(houseID.equals(TempID)){
                        String NamaPeserta = documentSnapshot.getString("NamaPeserta");
                        String NoIc = documentSnapshot.getString("ICNumber");
                        list.add(NamaPeserta+ " " + NoIc);
                        validID=1;
                    }
                }
                if(validID==0){
                    list.add("Tiada Data");
                }
                dataAdapter.notifyDataSetChanged();
            }
        });

        mUpdatePeserta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Peserta=String.valueOf(mPesertaUpdate.getSelectedItem());
                if(Peserta.equals("Tiada Data")){
                    Toast.makeText(UpdatePeserta2.this, "Tiada Data", Toast.LENGTH_SHORT).show();
                }else{
                    fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String Temp1 = documentSnapshot.getString("NamaPeserta");
                                String Temp2 = documentSnapshot.getString("ICNumber");
                                String Temp4 =Temp1 + " " + Temp2;
                                if(Objects.equals(Peserta, Temp4)) {
                                    Intent i = new Intent(v.getContext(),UpdatePeserta3.class);
                                    i.putExtra("IdPeserta",Temp2);
                                    i.putExtra("IdAcara",idTest);
                                    i.putExtra("BilPeserta",Bil);
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