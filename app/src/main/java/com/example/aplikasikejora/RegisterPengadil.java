package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPengadil extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText mEmailPengadil, mNamaPengadil, mNoTelPengadil, mKataLaluanPengadil;
    Button mDaftarPengadil;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pengadil);

        mEmailPengadil = findViewById(R.id.Email_Pengadil);
        mNamaPengadil = findViewById(R.id.Nama_Pengadil);
        mNoTelPengadil = findViewById(R.id.No_Tel_Pengadil);
        mKataLaluanPengadil = findViewById(R.id.Kata_Laluan_Pengadil);
        mDaftarPengadil = findViewById(R.id.Daftar_Pengadil);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mDaftarPengadil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(mEmailPengadil);
                checkField(mNamaPengadil);
                checkField(mNoTelPengadil);
                checkField(mKataLaluanPengadil);

                if (valid) {
                    String emailpengadil = mEmailPengadil.getText().toString().trim();
                    String katalaluanpengadil = mKataLaluanPengadil.getText().toString().trim();
                    String notelpengadil = mNoTelPengadil.getText().toString();
                    String namapengadil = mNamaPengadil.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(emailpengadil, katalaluanpengadil).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterPengadil.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Referee").document(userID);
                                Map<String, Object> Pengadil = new HashMap<>();
                                Pengadil.put("RefereeName", namapengadil);
                                Pengadil.put("RefereeTelNo", notelpengadil);
                                Pengadil.put("RefereeEmail", emailpengadil);
                                Pengadil.put("isReferee", "1");
                                documentReference.set(Pengadil).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: User Profile For " + userID);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), MenuSuperAdmin.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterPengadil.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
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

    public void Home(View view) {
        startActivity(new Intent(getApplicationContext(), MenuSuperAdmin.class));
    }
}