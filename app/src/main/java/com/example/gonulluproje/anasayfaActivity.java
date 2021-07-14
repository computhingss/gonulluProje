package com.example.gonulluproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class anasayfaActivity extends AppCompatActivity {
    Button btnEkle;

    private RecyclerView recyclerView;
    private FotoAdapter fotoAdapter;
    private List<Foto> fotoList;

    protected ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        tanimla();
        ekle();
        readFotos();
    }

    public void ekle(){
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(anasayfaActivity.this, EkleActivity.class);
                startActivity(i);
            }
        });
    }

    public void tanimla(){
        btnEkle =findViewById(R.id.btnEkle);
        fotoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle);
        progress_circular = findViewById(R.id.progress_circular);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManagers);
        fotoList = new ArrayList<>();
        fotoAdapter = new FotoAdapter(this, fotoList);
        recyclerView.setAdapter(fotoAdapter);
    }

    private void readFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("fotolar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fotoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Foto foto = snapshot.getValue(Foto.class);
                    fotoList.add(foto);


                }

                fotoAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}