package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateKetuaRumah extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileNama,profileEmail,profileTahunPelajar;
    Button SaveProfile;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ketua_rumah);

        Intent data = getIntent();
        String namapelajar  = data.getStringExtra("namapelajar");
        String emailpelajar = data.getStringExtra("emailpelajar");
        String tahunpelajar = data.getStringExtra("ICNumber");

        fAuth   =   FirebaseAuth.getInstance();
        fStore  =   FirebaseFirestore.getInstance();
        user    =   fAuth.getCurrentUser();

        profileNama            = findViewById(R.id.NamaKetuaRumahUpdate);
        profileEmail           = findViewById(R.id.EmailKetuaRumahUpdate);
        profileTahunPelajar    = findViewById(R.id.TahunPelajarUpdate);
        SaveProfile            = findViewById(R.id.SaveProfilInfo);

        profileNama.setText(namapelajar);
        profileEmail.setText(emailpelajar);
        profileTahunPelajar.setText(tahunpelajar);

        Log.d(TAG, "onCreate: " + namapelajar + " " + emailpelajar + " " + tahunpelajar);

        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileNama.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()||profileTahunPelajar.getText().toString().isEmpty()){
                    Toast.makeText(UpdateKetuaRumah.this, "Jangan Tinggalkan Ruang Kosong ! ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String emailpelajar = profileEmail.getText().toString();
                user.updateEmail(emailpelajar).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = fStore.collection("KetuaRumah").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("EmailKetuaRumah",emailpelajar);
                        edited.put("NamaKetuaRumah",profileNama.getText().toString());
                        edited.put("ICNumber",profileTahunPelajar.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateKetuaRumah.this, "Profil Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ProfileKetuaRumah.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateKetuaRumah.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void Home(View view) {
        startActivity(new Intent(getApplicationContext(), MenuKetuaRumah.class));
    }
}