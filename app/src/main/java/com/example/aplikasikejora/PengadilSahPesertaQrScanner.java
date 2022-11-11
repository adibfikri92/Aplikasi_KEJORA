package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

public class PengadilSahPesertaQrScanner extends AppCompatActivity {

    CodeScanner mCodeScanner;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    TextView mDisplayProfile;
    int valid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengadil_sah_peserta_qr_scanner);

        Intent data = getIntent();
        String idTest = data.getStringExtra("IdAcara");

        mDisplayProfile=findViewById(R.id.DisplayProfile1);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String iDdoc = result.getText();
                        fCollect.document(idTest).collection("Senarai Peserta")
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                   String idPeserta = documentSnapshot.getId();
                                   if(iDdoc.equals(idPeserta)){
                                       fCollect.document(idTest).collection("Senarai Peserta")
                                               .document(iDdoc).get();
                                       mDisplayProfile.setText(documentSnapshot.getString("NamaPeserta") + "\n"
                                               + documentSnapshot.getString("HouseID"));

                                       Map<String,Object> edited = new HashMap<>();
                                       edited.put("Kehadiran","1");
                                       fCollect.document(idTest).collection("Senarai Peserta")
                                               .document(iDdoc).update(edited);
                                       Toast.makeText(PengadilSahPesertaQrScanner.this, "Peserta Telah Disahkan", Toast.LENGTH_SHORT).show();

                                       valid=1;

                                   }
                                }
                                if(valid==0){
                                    Toast.makeText(PengadilSahPesertaQrScanner.this, "Tiada Rekod", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplayProfile.setText(" ");
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisplayProfile.setText(" ");
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}

