package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

public class KeputusanQrScanner extends AppCompatActivity {

    CodeScanner mCodeScanner;

    String iDdoc;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    TextView mDisplayProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keputusan_qr_scanner);

        Intent data = getIntent();
        String idTest = data.getStringExtra("IdAcara");
        String No = data.getStringExtra("Number");

        mDisplayProfile=findViewById(R.id.DisplayProfile2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");

        CodeScannerView scannerView = findViewById(R.id.scanner_view1);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iDdoc = result.getText();
                        mDisplayProfile.setText("Click to continue");

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(),DisplayProfilePemenang.class);
                i.putExtra("IdAcara",idTest);
                i.putExtra("IdPeserta",iDdoc);
                i.putExtra("Number",No);
                startActivity(i);
                finish();
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