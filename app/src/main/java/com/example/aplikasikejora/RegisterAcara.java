package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAcara extends AppCompatActivity{

    private static final String TAG="MainActivity";
    Spinner mJantina,mKategori,mPengadil;
    EditText mNamaAcara,mBilPeserta;
    Button mDaftarAcara;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    TextView mTarikh,mMasa;
    String date,time,datetime,TempID;
    DatePickerDialog.OnDateSetListener mPilihTarikh;
    int hour,min;
    boolean valid=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acara);

        mJantina = findViewById(R.id.Jantina);
        mKategori = findViewById(R.id.Kategori);
        mPengadil = findViewById(R.id.Pengadil);
        mNamaAcara = findViewById(R.id.NamaAcara);
        mDaftarAcara = findViewById(R.id.Daftar_Acara);
        mTarikh = findViewById(R.id.Tarikh);
        mMasa = findViewById(R.id.masa);
        mBilPeserta = findViewById(R.id.bilPeserta);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Pengadil");

        mMasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        RegisterAcara.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour=hourOfDay;
                        min=minute;

                        time = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                       try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm:aa");
                            mMasa.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },12,0,true
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

        mTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterAcara.this, android.R.style.Theme_Holo_Dialog_MinWidth,mPilihTarikh,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mPilihTarikh = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG,"onDate : "+ year +" "+month+" "+dayOfMonth);
                date = month + "-"+dayOfMonth+"-"+year;
                mTarikh.setText(date);
            }
        };

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(RegisterAcara.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Jantina));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mJantina.setAdapter(myAdapter1);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(RegisterAcara.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Kategori));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mKategori.setAdapter(myAdapter2);

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPengadil.setAdapter(dataAdapter);
        fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    list.add(documentSnapshot.getString("NamaPengadil"));
                }
                dataAdapter.notifyDataSetChanged();
            }
        });



        mDaftarAcara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(mNamaAcara);
                checkField(mBilPeserta);
                String Jantina = String.valueOf(mJantina.getSelectedItem());
                String Kategori = String.valueOf(mKategori.getSelectedItem());
                String Pengadil = String.valueOf(mPengadil.getSelectedItem());
                datetime=date+"T"+time+":00Z";
                if(valid && getDateFromString(datetime)!=null){
                    String namaacara = mNamaAcara.getText().toString();
                    String idDoc=Kategori+namaacara+Jantina;
                        int BilPeserta=Integer.parseInt(mBilPeserta.getText().toString());
                        Map<String, Object> Acara = new HashMap<>();
                        Acara.put("EventName", namaacara);
                        Acara.put("Gender", Jantina);
                        Acara.put("Category", Kategori);
                        Acara.put("Referee",Pengadil);
                        Acara.put("Date",getDateFromString(datetime));
                        Acara.put("NumParticipants",BilPeserta);
                        fStore.collection("Sport").document(idDoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "Document exists!");
                                        Toast.makeText(RegisterAcara.this, "Event Already Registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        fStore.collection("Sukan").document(idDoc).set(Acara).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterAcara.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MenuPengurusan.class));
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterAcara.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Failed with: ", task.getException());
                                }
                            }
                        });
                }else{
                    Toast.makeText(RegisterAcara.this, "Sila Pilih Tarikh Dan Masa", Toast.LENGTH_SHORT).show();
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

    static final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss'Z'");
    public Date getDateFromString(String datetoSaved){

        try {
            Date dateformat = format.parse(datetoSaved);
            return dateformat ;
        } catch (ParseException e){
            return null ;
        }

    }

}