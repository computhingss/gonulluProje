package com.example.gonulluproje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.squareup.picasso.Picasso;

import java.util.List;



public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Foto> Fotos;


    private FirebaseUser firebaseUser;

    public FotoAdapter(Context context, List<Foto> fotos){
        mContext = context;
        Fotos = fotos;
    }

    @NonNull
    @Override
    public FotoAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lwitem, parent, false);
        return new FotoAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FotoAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Foto foto = Fotos.get(position);



        holder.satir_konum.setText(foto.getKonum());
        holder.satir_yukleyen.setText(foto.getKullanici());
        Picasso.get().load(foto.getResim()).into(holder.satir_resim);




    }


    @Override
    public int getItemCount() {
        return Fotos.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView satir_konum;
        public TextView satir_yukleyen;
        public ImageView satir_resim;

        public ImageViewHolder(View itemView) {
            super(itemView);

            satir_konum = itemView.findViewById(R.id.satir_konum);
            satir_yukleyen = itemView.findViewById(R.id.satir_yukleyen);
            satir_resim = itemView.findViewById(R.id.satir_resim);
        }
    }

}