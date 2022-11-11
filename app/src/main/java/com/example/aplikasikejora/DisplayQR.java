package com.example.aplikasikejora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DisplayQR extends AppCompatActivity {

    ImageView mQRImage;
    Bitmap qrBits;
    TextView mNamaQR,mKategoriQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_q_r);

        mQRImage = findViewById(R.id.displayQR);
        mNamaQR = findViewById(R.id.NamaQR);
        mKategoriQR = findViewById(R.id.KategoriQR);

        Intent data = getIntent();
        String  idDoc = data.getStringExtra("idDoc");
        String namaAcara = data.getStringExtra("NamaAcara");
        String kategori = data.getStringExtra("Kategori");
        String namapeserta = data.getStringExtra("NamaPeserta");
        String Jantina = data.getStringExtra("Jantina");

        QRGEncoder qrgEncoder = new QRGEncoder(idDoc,null, QRGContents.Type.TEXT,500);
        qrBits = qrgEncoder.getBitmap();
        mQRImage.setImageBitmap(qrBits);
        mNamaQR.setText(namapeserta);
        mKategoriQR.setText(kategori +" " +namaAcara + " " + Jantina);


    }

    public void Home1(View view) {
        finish();
    }

}