package com.example.aplikasikejora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayKehadiranPeserta extends AppCompatActivity {

    FirebaseFirestore fStore;
    CollectionReference fCollect;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter Adapter;
    FirebaseAuth fAuth;
    String idDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_kehadiran_peserta);
        recyclerView = findViewById(R.id.KehadiranPesertaDisplay);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        Intent data = getIntent();
        idDoc = data.getStringExtra("IdAcara");
        fCollect = fStore.collection("Sukan").document(idDoc).collection("Senarai Peserta");
        Query query=fCollect;
        FirestoreRecyclerOptions<RetrieveKehadiran> options = new FirestoreRecyclerOptions.Builder<RetrieveKehadiran>()
                .setQuery(query, RetrieveKehadiran.class)
                .build();
        Adapter = new FirestoreRecyclerAdapter<RetrieveKehadiran, RetrieveKehadiranHolder>(options) {
            @NonNull
            @Override
            public RetrieveKehadiranHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kehadiran,parent,false);
                return new RetrieveKehadiranHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull RetrieveKehadiranHolder holder, int position, @NonNull RetrieveKehadiran model) {
                holder.HouseID.setText(model.getHouseID());
                holder.NamaPeserta.setText(model.getNamaPeserta());
                holder.Kehadiran.setText(Semak_Kehadiran(model.getKehadiran()));
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }
    private class RetrieveKehadiranHolder extends RecyclerView.ViewHolder{

        TextView HouseID,NamaPeserta,Kehadiran;

        public RetrieveKehadiranHolder(@NonNull View itemView) {
            super(itemView);

            HouseID =itemView.findViewById(R.id.HouseIDDisplay);
            NamaPeserta = itemView.findViewById(R.id.NamaPesertaDisplay1);
            Kehadiran = itemView.findViewById(R.id.KehadiranDisplay);

        }
    }

    public String Semak_Kehadiran(String Kehadiran){
        if(Kehadiran != null){
            return "Hadir";
        }else{
            return "Tidak Hadir";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
    }
}