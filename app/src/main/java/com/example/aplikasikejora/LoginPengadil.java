package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPengadil extends AppCompatActivity {

    EditText mEmailPengadil,mKataLaluanPengadil;
    TextView forgotTextLink;
    Button mLogMasuk,mHome;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pengadil);

        mEmailPengadil       =   findViewById(R.id.Email_Pengadil);
        mKataLaluanPengadil  =   findViewById(R.id.Kata_Laluan_Pengadil);
        fAuth               =   FirebaseAuth.getInstance();
        fStore              =   FirebaseFirestore.getInstance();
        progressBar         =   findViewById(R.id.progressBar);
        mLogMasuk           =   findViewById(R.id.LoginBtn);
        forgotTextLink      =   findViewById(R.id.ForgotPassword);
        mHome               =   findViewById(R.id.Home);

        mLogMasuk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String emailpengadil   =   mEmailPengadil.getText().toString().trim();
                String katalaluanpengadil    =   mKataLaluanPengadil.getText().toString().trim();

                if(TextUtils.isEmpty(emailpengadil)){
                    mEmailPengadil.setError("Please Enter Email");
                    return;
                }

                if(TextUtils.isEmpty(katalaluanpengadil)){
                    mKataLaluanPengadil.setError("Please Enter Password");
                    return;
                }

                if(katalaluanpengadil.length()   <   6){
                    mKataLaluanPengadil.setError("Password Must Be More Than 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(emailpengadil,katalaluanpengadil)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPengadil.this, "Login Failed !", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail  =   new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog =   new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Tetapkan Semula Kata Laluan ?");
                passwordResetDialog.setMessage("Sila Masukkan Email Untuk Menerima Pautan Tetapkan Semula.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail =   resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginPengadil.this, "Pautan Tetapkan Semula Telah Dihantar.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginPengadil.this, "Pauntan Tetapkan Semula Tidak Berjaya Dihantar"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Referee").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if(documentSnapshot.getString("isPengadil") != null){
                    Toast.makeText(LoginPengadil.this, "Login Success !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MenuPengadil.class));
                    finish();
                }else{
                    Toast.makeText(LoginPengadil.this, "Login Failed !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}