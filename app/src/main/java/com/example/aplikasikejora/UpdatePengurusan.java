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

public class UpdatePengurusan extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileNama,profileEmail,profileNoTel;
    Button SaveProfile;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pengurusan);

        Intent data = getIntent();
        String namapekerja  = data.getStringExtra("namapekerja");
        String emailpekerja = data.getStringExtra("emailpekerja");
        String notelpekerja = data.getStringExtra("notelpekerja");

        fAuth   =   FirebaseAuth.getInstance();
        fStore  =   FirebaseFirestore.getInstance();
        user    =   fAuth.getCurrentUser();

        profileNama     = findViewById(R.id.NamaPengurusanUpdate);
        profileEmail    = findViewById(R.id.EmailPengurusanUpdate);
        profileNoTel    = findViewById(R.id.NoTelPengurusanUpdate);
        SaveProfile     = findViewById(R.id.SaveProfilInfo);

        profileNama.setText(namapekerja);
        profileEmail.setText(emailpekerja);
        profileNoTel.setText(notelpekerja);

        Log.d(TAG, "onCreate: " + namapekerja + " " + emailpekerja + " " + notelpekerja);

        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileNama.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()||profileNoTel.getText().toString().isEmpty()){
                    Toast.makeText(UpdatePengurusan.this, "Jangan Tinggalkan Ruang Kosong ! ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String emailpekerja = profileEmail.getText().toString();
                user.updateEmail(emailpekerja).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentReference = fStore.collection("Pengurusan").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("EmailPengurusan",emailpekerja);
                        edited.put("NamaPengurusan",profileNama.getText().toString());
                        edited.put("NoTelPengurusan",profileNoTel.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdatePengurusan.this, "Profil Berjaya Dikemaskini", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ProfilePengurusan.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdatePengurusan.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void Home(View view) {
        startActivity(new Intent(getApplicationContext(), MenuPengurusan.class));
    }
}