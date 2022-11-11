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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class KeputusanKeseluruhan1 extends AppCompatActivity {

    FirebaseFirestore fStore;
    CollectionReference fCollect;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keputusan_keseluruhan1);
        recyclerView = findViewById(R.id.KeputusanKeseluruhan1);
        fStore = FirebaseFirestore.getInstance();
        fCollect = fStore.collection("Keputusan");
        Query query=fCollect;
        FirestoreRecyclerOptions<RetrieveKeputusan> options = new FirestoreRecyclerOptions.Builder<RetrieveKeputusan>()
                .setQuery(query, RetrieveKeputusan.class)
                .build();
        Adapter = new FirestoreRecyclerAdapter<RetrieveKeputusan,RetrieveKeputusanHolder>(options) {
            @NonNull
            @Override
            public RetrieveKeputusanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_keputusan,parent,false);
                return new RetrieveKeputusanHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull RetrieveKeputusanHolder holder, int position, @NonNull RetrieveKeputusan model) {
                holder.NamaAcara.setText(model.getNamaAcara());
                holder.No1.setText(model.getNo1());
                holder.No2.setText(model.getNo2());
                holder.No3.setText(model.getNo3());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }

    private class RetrieveKeputusanHolder extends RecyclerView.ViewHolder{

        TextView NamaAcara,No1,No2,No3;

        public RetrieveKeputusanHolder(@NonNull View itemView) {
            super(itemView);

            NamaAcara=itemView.findViewById(R.id.IDAcara);
            No1 = itemView.findViewById(R.id.No1);
            No2 = itemView.findViewById(R.id.No2);
            No3 = itemView.findViewById(R.id.No3);

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