package com.example.ceyda.friendlypaws;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList animal_id,  animal_address, animal_info, animal_phone, animal_user_id;  //animal_picture,

    private ArrayList<Bitmap>  animal_picture;

    CustomAdapter(Activity activity, Context context, ArrayList animal_id, ArrayList animal_picture, ArrayList animal_address,
                  ArrayList animal_info, ArrayList animal_phone, ArrayList animal_user_id){
        this.activity = activity;
        this.context = context;
        this.animal_id = animal_id;
        this.animal_picture = animal_picture;
        this.animal_address = animal_address;
        this.animal_info = animal_info;
        this.animal_phone = animal_phone;
        this.animal_user_id = animal_user_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.r_my_row, parent, false);
        return new MyViewHolder(view);
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {  //final
        if (position >= 0 && position < animal_id.size()) {
            // Ensure that the position is within the valid range of the ArrayList

            // Set the Bitmap to the ImageView
            Bitmap imageBitmap = animal_picture.get(position);
            holder.animal_picture_img.setImageBitmap(imageBitmap);

            holder.animal_address_txt.setText(String.valueOf(animal_address.get(position)));
            holder.animal_info_txt.setText(String.valueOf(animal_info.get(position)));

            //Phone

            // Now you can use these variables to bind data to your ViewHolder

            //Recyclerview onClickListener
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LostAnimalViewActivity.class);
                    intent.putExtra("id", String.valueOf(animal_id.get(position)));

                    //Transaction too large !!!

                    activity.startActivityForResult(intent, 1);
                }
            });

        } else {
            // Handle the case where the position is out of bounds
            Log.e("CustomAdapter", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return animal_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView animal_address_txt, animal_info_txt;  //animal_id_txt,
        ImageView animal_picture_img;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //animal_id_txt = itemView.findViewById(R.id.animal_id_txt);
            animal_picture_img = itemView.findViewById(R.id.animal_picture_img);
            animal_address_txt = itemView.findViewById(R.id.animal_address_txt);
            animal_info_txt = itemView.findViewById(R.id.animal_info_txt);

            mainLayout = itemView.findViewById(R.id.mainLayout);

            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }
}
