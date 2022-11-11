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

public class DisplayPesertaList extends AppCompatActivity {

    FirebaseFirestore fStore;
    CollectionReference fCollect;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter Adapter;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_peserta_list);
        recyclerView = findViewById(R.id.PesertaListDisplay);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID  =    fAuth.getCurrentUser().getUid();
        fCollect = fStore.collection("KetuaRumah").document(userID).collection("Senarai Peserta");
        Query query=fCollect;
        FirestoreRecyclerOptions<RetrievePeserta> options = new FirestoreRecyclerOptions.Builder<RetrievePeserta>()
                .setQuery(query, RetrievePeserta.class)
                .build();
        Adapter = new FirestoreRecyclerAdapter<RetrievePeserta, RetrievePesertaHolder>(options) {
            @NonNull
            @Override
            public RetrievePesertaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_peserta,parent,false);
                return new RetrievePesertaHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull RetrievePesertaHolder holder, int position, @NonNull RetrievePeserta model) {
                holder.ICNumber.setText(model.getICNumber());
                holder.NamaPeserta.setText(model.getNamaPeserta());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }
    private class RetrievePesertaHolder extends RecyclerView.ViewHolder{

        TextView ICNumber,NamaPeserta;

        public RetrievePesertaHolder(@NonNull View itemView) {
            super(itemView);

            ICNumber =itemView.findViewById(R.id.ICNumberDisplay);
            NamaPeserta = itemView.findViewById(R.id.NamaPesertaDisplay);

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


    public void KodQR(View view) {
        startActivity(new Intent(getApplicationContext(), RecoverQR.class));
    }
}