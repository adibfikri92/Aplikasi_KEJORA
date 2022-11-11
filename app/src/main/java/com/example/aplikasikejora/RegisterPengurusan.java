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

public class RegisterPengurusan extends AppCompatActivity {
    private static final String TAG = "TAG";
    EditText mEmailPekerja, mNamaPekerja, mNoTelPekerja, mKataLaluanPekerja;
    Button mDaftarPengurusan;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID,userIDTemp;
    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pengurusan);

        mEmailPekerja = findViewById(R.id.Email_Pekerja);
        mNamaPekerja = findViewById(R.id.Nama_Pekerja);
        mNoTelPekerja = findViewById(R.id.No_Tel_Pekerja);
        mKataLaluanPekerja = findViewById(R.id.Kata_Laluan_Pekerja);
        mDaftarPengurusan = findViewById(R.id.Daftar_Pengurusan);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mDaftarPengurusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(mEmailPekerja);
                checkField(mNamaPekerja);
                checkField(mNoTelPekerja);
                checkField(mKataLaluanPekerja);

                if (valid) {
                    String emailpekerja = mEmailPekerja.getText().toString().trim();
                    String katalaluanpekerja = mKataLaluanPekerja.getText().toString().trim();
                    String notelpekerja = mNoTelPekerja.getText().toString();
                    String namapekerja = mNamaPekerja.getText().toString();

                    userIDTemp = fAuth.getCurrentUser().getUid();
                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(emailpekerja, katalaluanpekerja).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterPengurusan.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("Management").document(userID);
                                Map<String, Object> Pengurusan = new HashMap<>();
                                Pengurusan.put("ManagementName", namapekerja);
                                Pengurusan.put("ManagementTelNo", notelpekerja);
                                Pengurusan.put("ManagementEmail", emailpekerja);
                                Pengurusan.put("isAdmin","1");
                                documentReference.set(Pengurusan).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: User Profile For " + userID);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), MenuSuperAdmin.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterPengurusan.this, "Error !" +
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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