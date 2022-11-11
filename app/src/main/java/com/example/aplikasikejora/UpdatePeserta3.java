package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdatePeserta3 extends AppCompatActivity {

    private static final String TAG="MainActivity";
    EditText mNamaPeserta, mICNumberPeserta;
    Button mUpdatePeserta;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    String strDate,J,namaAcara,kategori;
    String userID,icNumber;
    String houseID,idTest,idPeserta;
    TextView mDisplayAcara,mDisplayMasa;
    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_peserta3);

        Intent data = getIntent();
        idTest = data.getStringExtra("IdAcara");
        idPeserta = data.getStringExtra("IdPeserta");

        mUpdatePeserta = findViewById(R.id.UpdatePesertaBtn3);
        mNamaPeserta = findViewById(R.id.Nama_PesertaUpdate);
        mICNumberPeserta = findViewById(R.id.Tahun_Pelajar_PesertaUpdate);
        mDisplayAcara = findViewById(R.id.DisplayAcaraPeserta);
        mDisplayMasa = findViewById(R.id.DisplayMasaPeserta);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        userID  =    fAuth.getCurrentUser().getUid();

        fCollect.document(idTest).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Date masaTarikh = documentSnapshot.getDate("Tarikh");
                J=documentSnapshot.getString("Jantina");
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss' '");
                strDate = dateFormat.format(masaTarikh);
                mDisplayMasa.setText(strDate);
                mDisplayAcara.setText(documentSnapshot.getString("Kategori")+ " " + documentSnapshot.getString("NamaAcara")
                        + " " + documentSnapshot.getString("Jantina")
                );

                namaAcara=documentSnapshot.getString("NamaAcara");
                kategori=documentSnapshot.getString("Kategori");
            }
        });

        fStore.collection("KetuaRumah").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                houseID = documentSnapshot.getString("KR");
            }
        });

        fCollect.document(idTest).collection("Senarai Peserta").document(idPeserta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mNamaPeserta.setText(documentSnapshot.getString("NamaPeserta"));
                mICNumberPeserta.setText(documentSnapshot.getString("ICNumber"));
            }
        });


        mUpdatePeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(mNamaPeserta);
                checkField(mICNumberPeserta);
                if(valid){
                    String namapeserta=mNamaPeserta.getText().toString();
                    icNumber = mICNumberPeserta.getText().toString();
                    if(!idPeserta.equals(icNumber)){
                        Map<String, Object> Peserta = new HashMap<>();
                        Peserta.put("NamaPeserta", namapeserta);
                        Peserta.put("ICNumber", icNumber);
                        Peserta.put("HouseID",houseID);
                        Peserta.put("Kehadiran",null);
                        fStore.collection("Sukan").document(idTest)
                                .collection("Senarai Peserta")
                                .document(icNumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        Toast.makeText(UpdatePeserta3.this, "Peserta Sudah Didaftarkan", Toast.LENGTH_SHORT).show();
                                    } else {
                                        savePeserta(userID,icNumber,namapeserta,icNumber);
                                        fStore.collection("Sukan").document(idTest)
                                                .collection("Senarai Peserta")
                                                .document(icNumber).set(Peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                fStore.collection("Sukan").document(idTest).collection("Senarai Peserta").document(idPeserta).delete();
                                                Toast.makeText(UpdatePeserta3.this, "Peserta Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(v.getContext(),DisplayQR.class);
                                                i.putExtra("idDoc",icNumber);
                                                i.putExtra("NamaPeserta", namapeserta);
                                                i.putExtra("NamaAcara",namaAcara);
                                                i.putExtra("Kategori",kategori);
                                                i.putExtra("Jantina",J);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UpdatePeserta3.this, "Peserta Tidak Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            }
                        });
                    }else{
                        Map<String, Object> Peserta = new HashMap<>();
                        Peserta.put("NamaPeserta", namapeserta);
                        Peserta.put("ICNumber", icNumber);
                        Peserta.put("HouseID",houseID);
                        Peserta.put("Kehadiran",null);
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(icNumber).update(Peserta);
                        fStore.collection("Sukan").document(idTest)
                                .collection("Senarai Peserta")
                                .document(icNumber).update(Peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                fStore.collection("Sukan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String id=documentSnapshot.getId();
                                            fStore.collection("Sukan").document(id)
                                                    .collection("Senarai Peserta").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                        String idPesertaTemp=documentSnapshot.getId();
                                                        if(icNumber.equals(idPesertaTemp)){
                                                            fStore.collection("Sukan").document(id)
                                                                    .collection("Senarai Peserta")
                                                                    .document(idPesertaTemp).update(Peserta);
                                                        }

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                Toast.makeText(UpdatePeserta3.this, "Peserta Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdatePeserta3.this, "Peserta Tidak Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                }
            }
        });

    }

    public void savePeserta(String userID, String idDoc, String namapeserta, String icNumber){
        Map<String, Object> Peserta = new HashMap<>();
        Peserta.put("NamaPeserta", namapeserta);
        Peserta.put("ICNumber", icNumber);
        Peserta.put("HouseID",houseID);
        Peserta.put("Kehadiran",null);

        Map<String, Object> Acara = new HashMap<>();
        Acara.put("NamaAcara",namaAcara);
        Acara.put("Kategori",kategori);
        Acara.put("Jantina",J);

        fStore.collection("KetuaRumah").document(userID)
                .collection("Senarai Peserta")
                .document(idPeserta).collection("Senarai Acara")
                .document(idTest).delete();

        fStore.collection("KetuaRumah").document(userID)
                .collection("Senarai Peserta")
                .document(idDoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(idDoc).collection("Senarai Acara")
                                .document(idTest).set(Acara);
                    } else {
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(idDoc).set(Peserta);
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(idDoc).collection("Senarai Acara")
                                .document(idTest).set(Acara);
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
        fStore.collection("KetuaRumah").document(userID)
                .collection("Senarai Peserta")
                .document(idPeserta).collection("Senarai Acara")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    fStore.collection("KetuaRumah").document(userID)
                            .collection("Senarai Peserta")
                            .document(idPeserta).delete();
                }
            }
        });
    }

    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Error");
            valid=false;
        }else{
            valid=true;
        }
        return valid;
    }

    public void deletePeserta(View view) {

        fStore.collection("Sukan").document(idTest).collection("Senarai Peserta")
                .document(idPeserta).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fStore.collection("KetuaRumah").document(userID)
                        .collection("Senarai Peserta")
                        .document(idPeserta).collection("Senarai Acara")
                        .document(idTest).delete();
                Toast.makeText(UpdatePeserta3.this, "Peserta Berjaya Dibuang", Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdatePeserta3.this, "Peserta Tidak Berjaya Dibuang", Toast.LENGTH_SHORT).show();
            }
        });

        fStore.collection("KetuaRumah").document(userID)
                .collection("Senarai Peserta")
                .document(idPeserta).collection("Senarai Acara")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    fStore.collection("KetuaRumah").document(userID)
                            .collection("Senarai Peserta")
                            .document(idPeserta).delete();
                }
            }
        });

    }


}