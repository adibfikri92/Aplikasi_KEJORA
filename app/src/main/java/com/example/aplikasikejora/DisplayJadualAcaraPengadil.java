package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayJadualAcaraPengadil extends AppCompatActivity {

    FirebaseFirestore fStore;
    CollectionReference fCollect;
    FirebaseAuth fAuth;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter Adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_jadual_acara_pengadil);

        recyclerView = findViewById(R.id.JadualAcaraPengadilDisplay);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fCollect = fStore.collection("Sukan");

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss' '");

        Query query=fCollect.orderBy("Tarikh", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<RetrieveAcara> options = new FirestoreRecyclerOptions.Builder<RetrieveAcara>()
                .setQuery(query, RetrieveAcara.class)
                .build();

        Adapter1 = new FirestoreRecyclerAdapter<RetrieveAcara, RetrieveJadualHolder>(options) {
            @NonNull
            @Override
            public RetrieveJadualHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jadual_pengadil,parent,false);
                return new RetrieveJadualHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RetrieveJadualHolder holder1, int position, @NonNull RetrieveAcara model1) {
                    holder1.Pengadil.setText(model1.getPengadil());
                    holder1.NamaAcara.setText(model1.getKategori()+" "+ model1.getNamaAcara()+" "+model1.getJantina());
                    holder1.Tarikh.setText(dateFormat.format(model1.getTarikh()));

            }
        };


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter1);
    }

    private class RetrieveJadualHolder extends RecyclerView.ViewHolder{

        TextView Pengadil,NamaAcara,Tarikh;

        public RetrieveJadualHolder(@NonNull View itemView) {
            super(itemView);

            Pengadil=itemView.findViewById(R.id.PengadilDisplay);
            NamaAcara = itemView.findViewById(R.id.JadualNamaAcaraDisplay1);
            Tarikh = itemView.findViewById(R.id.JadualDisplay1);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter1.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter1.startListening();
    }
}