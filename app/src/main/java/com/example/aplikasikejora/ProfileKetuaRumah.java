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

public class ProfileKetuaRumah extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView EmailPelajar, NamaPelajar, TahunPelajar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Button forgotTextLink,UpdateProfile;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ketua_rumah);

        EmailPelajar    = findViewById(R.id.Profile_Email_Pelajar);
        NamaPelajar     = findViewById(R.id.Profile_Nama_Pelajar);
        TahunPelajar    = findViewById(R.id.Profile_Tahun_Pelajar);
        forgotTextLink  = findViewById(R.id.Reset_Password);
        UpdateProfile   = findViewById(R.id.KetuaRumahUpdateBtn);

        fAuth   =   FirebaseAuth.getInstance();
        fStore  =   FirebaseFirestore.getInstance();

        userID  =   fAuth.getCurrentUser().getUid();
        user    =   fAuth.getCurrentUser();

        DocumentReference documentReference =   fStore.collection("KetuaRumah").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    EmailPelajar.setText(documentSnapshot.getString("EmailKetuaRumah"));
                    NamaPelajar.setText(documentSnapshot.getString("NamaKetuaRumah"));
                    TahunPelajar.setText(documentSnapshot.getString("ICNumber"));
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
                                Toast.makeText(ProfileKetuaRumah.this, "Tukaran Kata Laluan Berjaya ! ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileKetuaRumah.this, "Tukaran Kata Laluan Tidak Berjaya ! ", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(v.getContext(),UpdateKetuaRumah.class);
                i.putExtra("namapelajar",NamaPelajar.getText().toString());
                i.putExtra("emailpelajar",EmailPelajar.getText().toString());
                i.putExtra("ICNumber",TahunPelajar.getText().toString());
                startActivity(i);
            }
        });
    }



    public void Home(View view) {
        finish();
    }
}