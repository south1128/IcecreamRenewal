package com.example.icecream_renual;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyViewHolder> {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SampleData> sample;

    public NewAdapter(Context context, ArrayList<SampleData> data){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dday,foodname,expirydate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dday = itemView.findViewById(R.id.dday);
            this.foodname = itemView.findViewById(R.id.foodname);
            this.expirydate = itemView.findViewById(R.id.expirydate);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView;
        holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationlist,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e("type",String.valueOf(holder.getItemViewType()));
        holder.dday.setText(sample.get(position).getDday());
        holder.foodname.setText(sample.get(position).getFoodname());
        holder.expirydate.setText("유통기한 : "+sample.get(position).getExpirydate());

        String[] dday_split = holder.dday.getText().toString().split("-");
        if (holder.dday.getText().toString().contains("-")){
            if (Integer.parseInt(dday_split[1])>7){holder.itemView.setBackgroundColor(Color.parseColor("#326199"));}
            else if (Integer.parseInt(dday_split[1])>=5){holder.itemView.setBackgroundColor(Color.parseColor("#4FB1A1"));}
            else {holder.itemView.setBackgroundColor(Color.parseColor("#FCC055"));
//            holder.itemView.setBackgroundResource(R.drawable.red_ddaylist);
            }
        }
        else if (holder.dday.getText().toString().contains("+")){
            holder.itemView.setBackgroundColor(Color.parseColor("#DF6E5B"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FCC055"));
        }

    }

    @Override
    public int getItemCount() {
        return sample.size();
    }


}
