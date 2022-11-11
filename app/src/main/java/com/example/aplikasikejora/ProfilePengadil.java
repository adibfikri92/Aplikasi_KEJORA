package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfilePengadil extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView EmailPengadil, NamaPengadil, NoTelPengadil;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button forgotTextLink,UpdateProfile;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pengadil);

        EmailPengadil    = findViewById(R.id.Profile_Email_Pengadil);
        NamaPengadil     = findViewById(R.id.Profile_Nama_Pengadil);
        NoTelPengadil    = findViewById(R.id.Profile_No_Tel_Pengadil);
        forgotTextLink  = findViewById(R.id.Reset_Password_Pengadil);
        UpdateProfile   = findViewById(R.id.PengadilUpdateBtn);

        fAuth   =   FirebaseAuth.getInstance();
        fStore  =   FirebaseFirestore.getInstance();

        userID  =   fAuth.getCurrentUser().getUid();
        user    =   fAuth.getCurrentUser();

        DocumentReference documentReference =   fStore.collection("Pengadil").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    EmailPengadil.setText(documentSnapshot.getString("EmailPengadil"));
                    NamaPengadil.setText(documentSnapshot.getString("NamaPengadil"));
                    NoTelPengadil.setText(documentSnapshot.getString("NoTelPengadil"));
                }else{
                    Log.d(TAG, "onEvent: Tiada Maklumat ");
                }
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPassword  =   new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog =   new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Tukar Kata Laluan ?");
                passwordResetDialog.setMessage("Sila Masukkan Kata Laluan Perlu Lebih 6 Aksara.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword =   resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfilePengadil.this, "Tukaran Kata Laluan Berjaya ! ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfilePengadil.this, "Tukaran Kata Laluan Tidak Berjaya ! ", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.show();
            }
        });

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),UpdatePengadil.class);
                i.putExtra("namapekerja",NamaPengadil.getText().toString());
                i.putExtra("emailpekerja",EmailPengadil.getText().toString());
                i.putExtra("notelpekerja",NoTelPengadil.getText().toString());
                startActivity(i);
            }
        });

    }

    public void Home(View view) {
        finish();
    }
}