package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class RegisterKetuaRumah extends AppCompatActivity {
    private static final String TAG = "TAG";
    EditText mEmailPelajar, mNamaPelajar, mTahunPelajar, mKataLaluanPelajar;
    Spinner mRumah;
    Button mDaftarPelajar;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ketua_rumah);

        mEmailPelajar = findViewById(R.id.Email_Pelajar);
        mNamaPelajar = findViewById(R.id.Nama_Pelajar);
        mTahunPelajar = findViewById(R.id.Tahun_Pelajar);
        mKataLaluanPelajar = findViewById(R.id.Kata_Laluan_Pelajar);
        mDaftarPelajar = findViewById(R.id.Daftar_Ketua_Rumah);
        mRumah = findViewById(R.id.RumahID);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(RegisterKetuaRumah.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Rumah));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRumah.setAdapter(myAdapter3);

        mDaftarPelajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(mEmailPelajar);
                checkField(mNamaPelajar);
                checkField(mTahunPelajar);
                checkField(mKataLaluanPelajar);
                String house = String.valueOf(mRumah.getSelectedItem());

                if (valid) {
                    String emailpelajar = mEmailPelajar.getText().toString().trim();
                    String katalaluanpelajar = mKataLaluanPelajar.getText().toString().trim();
                    String tahunpelajar = mTahunPelajar.getText().toString();
                    String namapelajar = mNamaPelajar.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(emailpelajar, katalaluanpelajar)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterKetuaRumah.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("HeadHouse").document(userID);
                                Map<String, Object> KetuaRumah = new HashMap<>();
                                KetuaRumah.put("HeadHouseName", namapelajar);
                                KetuaRumah.put("ICNumber", tahunpelajar);
                                KetuaRumah.put("HeadHouseEmail", emailpelajar);
                                KetuaRumah.put("isHH", "1");
                                KetuaRumah.put("HH",house);
                                documentReference.set(KetuaRumah).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: User Profile For "
                                                + userID);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), MenuSuperAdmin.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterKetuaRumah.this, "Error !"
                                        + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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