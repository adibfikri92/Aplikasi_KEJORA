package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegisterPesertaBaru extends AppCompatActivity{

    private static final String TAG="MainActivity";
    EditText mNamaPeserta, mICNumberPeserta;
    Button mDaftarPeserta;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    String strDate,J,namaAcara,kategori;
    String userID;
    String houseID;
    TextView mDisplayAcara,mDisplayMasa;
    boolean valid=true;
    Integer count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_peserta_baru);

        Intent data = getIntent();
        String idTest = data.getStringExtra("IdAcara");
        Integer Bil = data.getIntExtra("BilPeserta",0);


        mDaftarPeserta = findViewById(R.id.Daftar_Peserta);
        mNamaPeserta = findViewById(R.id.Nama_Peserta);
        mICNumberPeserta = findViewById(R.id.Tahun_Pelajar_Peserta);
        mDisplayAcara = findViewById(R.id.DisplayAcara1);
        mDisplayMasa = findViewById(R.id.DisplayMasa);

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

        fCollect.document(idTest).collection("Senarai Peserta").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                count=1;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String TempHouseID = documentSnapshot.getString("HouseID");
                    if(houseID.equals(TempHouseID)){
                        count=count+1;
                    }
                }
            }
        });

       mDaftarPeserta.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               checkField(mNamaPeserta);
               checkField(mICNumberPeserta);
               if(valid){
                   if(count <= Bil){
                       String namapeserta=mNamaPeserta.getText().toString();
                       String icNumber = mICNumberPeserta.getText().toString();
                       Map<String, Object> Peserta = new HashMap<>();
                       Peserta.put("ParticipantsName", namapeserta);
                       Peserta.put("ICNumber", icNumber);
                       Peserta.put("HouseID",houseID);
                       Peserta.put("Attendance",null);
                       String idDoc=icNumber;
                       fStore.collection("Sport").document(idTest)
                               .collection("Participants List")
                               .document(idDoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()) {
                                   DocumentSnapshot document = task.getResult();
                                   if (document.exists()) {
                                       Log.d(TAG, "Document exists!");
                                       Toast.makeText(RegisterPesertaBaru.this, "Participants are already registered", Toast.LENGTH_SHORT).show();
                                   } else {
                                       savePeserta(userID,idDoc,namapeserta,icNumber,idTest);
                                       fStore.collection("Sport").document(idTest)
                                               .collection("Participants List")
                                               .document(idDoc).set(Peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Toast.makeText(RegisterPesertaBaru.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                               Intent i = new Intent(v.getContext(),DisplayQR.class);
                                               i.putExtra("idDoc",idDoc);
                                               i.putExtra("ParticipantsName", namapeserta);
                                               i.putExtra("EventName",namaAcara);
                                               i.putExtra("Category",kategori);
                                               i.putExtra("Gender",J);
                                               startActivity(i);
                                               finish();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(RegisterPesertaBaru.this, "Tidak Berjaya Didaftarkan", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                               } else {
                                   Log.d(TAG, "Failed with: ", task.getException());
                               }
                           }
                       });
                   }else {
                       if(count>2){
                           Toast.makeText(RegisterPesertaBaru.this, "Pendaftaran Telah Melebihi Kuota " + "(" + Bil + ")", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(RegisterPesertaBaru.this, "Semak Semula Kategori", Toast.LENGTH_SHORT).show();
                       }
                   }
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

    public void savePeserta(String userID, String idDoc, String namapeserta, String icNumber,String idacara){
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
                                .document(idacara).set(Acara);
                    } else {
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(idDoc).set(Peserta);
                        fStore.collection("KetuaRumah").document(userID)
                                .collection("Senarai Peserta")
                                .document(idDoc).collection("Senarai Acara")
                                .document(idacara).set(Acara);
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }


}