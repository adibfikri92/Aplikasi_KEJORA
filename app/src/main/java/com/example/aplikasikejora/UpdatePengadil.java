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

public class UpdatePengadil extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileNama,profileEmail,profileNoTel;
    Button SaveProfile;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pengadil);

        Intent data = getIntent();
        String namapekerja  = data.getStringExtra("namapekerja");
        String emailpekerja = data.getStringExtra("emailpekerja");
        String notelpekerja = data.getStringExtra("notelpekerja");

        fAuth   =   FirebaseAuth.getInstance();
        fStore  =   FirebaseFirestore.getInstance();
        user    =   fAuth.getCurrentUser();

        profileNama     = findViewById(R.id.Nama_PengadilUpdate);
        profileEmail    = findViewById(R.id.Email_PengadilUpdate);
        profileNoTel    = findViewById(R.id.NoTelPengadilUpdate);
        SaveProfile     = findViewById(R.id.SaveProfilPengadilInfo);

        profileNama.setText(namapekerja);
        profileEmail.setText(emailpekerja);
        profileNoTel.setText(notelpekerja);

        Log.d(TAG, "onCreate: " + namapekerja + " " + emailpekerja + " " + notelpekerja);

        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileNama.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()||profileNoTel.getText().toString().isEmpty()){
                    Toast.makeText(UpdatePengadil.this, "Jangan Tinggalkan Ruang Kosong ! ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String emailpekerja = profileEmail.getText().toString();
                user.updateEmail(emailpekerja).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = fStore.collection("Pengadil").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("EmailPengadil",emailpekerja);
                        edited.put("NamaPengadil",profileNama.getText().toString());
                        edited.put("NoTelPengadil",profileNoTel.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdatePengadil.this, "Profil Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ProfilePengadil.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdatePengadil.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}