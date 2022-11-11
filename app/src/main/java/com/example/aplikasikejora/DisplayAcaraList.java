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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayAcaraList extends AppCompatActivity {

    FirebaseFirestore fStore;
    CollectionReference fCollect;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_acara_list);
        recyclerView = findViewById(R.id.AcaraListDisplay);
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Sukan");
        Query query=fCollect;
        FirestoreRecyclerOptions<RetrieveAcara> options = new FirestoreRecyclerOptions.Builder<RetrieveAcara>()
                .setQuery(query, RetrieveAcara.class)
                .build();
        Adapter = new FirestoreRecyclerAdapter<RetrieveAcara, RetrieveAcaraHolder>(options) {
            @NonNull
            @Override
            public RetrieveAcaraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_acara,parent,false);
                return new RetrieveAcaraHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull RetrieveAcaraHolder holder, int position, @NonNull RetrieveAcara model) {
                holder.Kategori.setText(model.getKategori());
                holder.NamaAcara.setText(model.getNamaAcara() + " "+ model.getJantina());
                holder.Bil.setText(model.getBilPeserta()+"");
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }
    private class RetrieveAcaraHolder extends RecyclerView.ViewHolder{

        TextView Kategori,NamaAcara,Bil;

        public RetrieveAcaraHolder(@NonNull View itemView) {
            super(itemView);

            Kategori=itemView.findViewById(R.id.KategoriDisplay);
            NamaAcara = itemView.findViewById(R.id.NamaAcaraDisplay);
            Bil = itemView.findViewById(R.id.BilPesertaDisplay);

        }
    }

    public void Daftar(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterPeserta2.class));
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