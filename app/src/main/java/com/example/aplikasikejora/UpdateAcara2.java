package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UpdateAcara2 extends AppCompatActivity {

    public static final String TAG = "TAG";
    Spinner mJantinaUpdate,mKategoriUpdate,mPengadilUpdate;
    EditText mNamaAcaraUpdate,mBilPesertaUpdate;
    Button mUpdateAcara;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CollectionReference fCollect;
    TextView mTarikhUpdate,mMasaUpdate;
    String date,time,datetime,strTime,strDate,idTest;
    DatePickerDialog.OnDateSetListener mPilihTarikhUpdate;
    int hour,min;
    boolean valid=true;

    String namaAcaraUpdate,kategori,pengadil,jantina;
    Integer BilUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_acara2);

        mJantinaUpdate = findViewById(R.id.JantinaUpdateAcara);
        mKategoriUpdate = findViewById(R.id.KategoriUpdateAcara);
        mPengadilUpdate = findViewById(R.id.PengadilUpdateAcara);
        mNamaAcaraUpdate = findViewById(R.id.NamaAcaraUpdateAcara);
        mUpdateAcara = findViewById(R.id.UpdateAcara);
        mTarikhUpdate = findViewById(R.id.TarikhUpdateAcara);
        mMasaUpdate = findViewById(R.id.MasaUpdateAcara);
        mBilPesertaUpdate = findViewById(R.id.bilPesertaUpdateAcara);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Pengadil");

        Intent data = getIntent();
        idTest = data.getStringExtra("IdAcara");
        namaAcaraUpdate=data.getStringExtra("NamaAcara");
        kategori=data.getStringExtra("Kategori");
        pengadil=data.getStringExtra("Pengadil");
        jantina=data.getStringExtra("Jantina");
        BilUpdate=data.getIntExtra("Bil",0);
        strDate=data.getStringExtra("Date");
        strTime=data.getStringExtra("Time");

        mTarikhUpdate.setText(strDate);
        mMasaUpdate.setText(strTime);

        mNamaAcaraUpdate.setText(namaAcaraUpdate);
        mBilPesertaUpdate.setText(BilUpdate.toString());

        List<String> list = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPengadilUpdate.setAdapter(dataAdapter);
        fCollect.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list.add(pengadil);
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if(!Objects.equals(pengadil, documentSnapshot.getString("NamaPengadil"))){
                        list.add(documentSnapshot.getString("NamaPengadil"));
                    }
                }
                dataAdapter.notifyDataSetChanged();
            }
        });


        List<String> list1 = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mJantinaUpdate.setAdapter(dataAdapter1);
        if(jantina.equals("Lelaki")){
            list1.add("Lelaki");
            list1.add("Perempuan");
        }else{
            list1.add("Perempuan");
            list1.add("Lelaki");
        }
        dataAdapter1.notifyDataSetChanged();

        List<String> list2 = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mKategoriUpdate.setAdapter(dataAdapter2);
        if(kategori.equals("U13")){
            list2.add("U13");
            list2.add("U15");
            list2.add("U18");
        }else if(kategori.equals("U15")){
            list2.add("U15");
            list2.add("U13");
            list2.add("U18");
        }else{
            list2.add("U18");
            list2.add("U13");
            list2.add("U15");
        }
        dataAdapter2.notifyDataSetChanged();

        mMasaUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateAcara2.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour=hourOfDay;
                        min=minute;

                        time = hour + ":" + min;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm:aa");
                            mMasaUpdate.setText(f12Hours.format(date));
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

        mTarikhUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateAcara2.this, android.R.style.Theme_Holo_Dialog_MinWidth,mPilihTarikhUpdate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mPilihTarikhUpdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG,"onDate : "+ year +" "+month+" "+dayOfMonth);
                date = month + "-"+dayOfMonth+"-"+year;
                mTarikhUpdate.setText(date);
            }
        };

        mUpdateAcara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(mNamaAcaraUpdate);
                checkField(mBilPesertaUpdate);

                String Jantina = String.valueOf(mJantinaUpdate.getSelectedItem());
                String Kategori = String.valueOf(mKategoriUpdate.getSelectedItem());
                String Pengadil = String.valueOf(mPengadilUpdate.getSelectedItem());
                datetime=date+"T"+time+":00Z";


                if(valid && getDateFromString(datetime)!=null){
                    String namaacara = mNamaAcaraUpdate.getText().toString();
                    int BilPeserta=Integer.parseInt(mBilPesertaUpdate.getText().toString());
                    Map<String, Object> Acara = new HashMap<>();
                    Acara.put("NamaAcara", namaacara);
                    Acara.put("Jantina", Jantina);
                    Acara.put("Kategori", Kategori);
                    Acara.put("Pengadil",Pengadil);
                    Acara.put("Tarikh",getDateFromString(datetime));
                    Acara.put("BilPeserta",BilPeserta);
                    String idDoc=Kategori+namaacara+Jantina;
                    fStore.collection("Sukan").document(idDoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "Document exists!");
                                    Toast.makeText(UpdateAcara2.this, "Acara Sudah Didaftarkan", Toast.LENGTH_SHORT).show();
                                } else {
                                    fStore.collection("Sukan").document(idDoc).set(Acara).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UpdateAcara2.this, "Berjaya Kemaskini", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                    fStore.collection("Sukan").document(idTest).collection("Senarai Peserta")
                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                fStore.collection("Sukan").document(idTest).collection("Senarai Peserta")
                                                        .document(documentSnapshot.getId()).delete();
                                            }
                                            fStore.collection("Sukan").document(idTest).delete();
                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "Failed with: ", task.getException());
                            }
                        }
                    });
                }else{
                    Toast.makeText(UpdateAcara2.this, "Sila Pilih Semula Tarikh Dan Masa", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void deleteAcara(View view) {
        fStore.collection("Sukan").document(idTest).collection("Senarai Peserta").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    fStore.collection("Sukan").document(idTest).collection("Senarai Peserta").document(documentSnapshot.getId()).delete();
                }
                fStore.collection("Sukan").document(idTest).delete();
                Toast.makeText(UpdateAcara2.this, "Acara Berjaya Dibuang ( " + idTest + " )", Toast.LENGTH_SHORT).show();
                finish();
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