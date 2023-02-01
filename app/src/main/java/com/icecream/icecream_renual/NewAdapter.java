package com.icecream.icecream_renual;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyViewHolder> {

//    public interface onItemClickEventListener{
//        void onItemClick(View view,int position);
//    }
    Context mContext = null;
//    onItemClickEventListener itemClickEventListener;
    LayoutInflater mLayoutInflater = null;
    ArrayList<FoodData> foodData;

    public NewAdapter(Context context, ArrayList<FoodData> data){
        mContext = context;
        foodData = data;
        mLayoutInflater = LayoutInflater.from(mContext);

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dday,foodname,emoji,expirydate;
        ImageView delete_icon;

        public RelativeLayout foreground,background;






        public MyViewHolder(@NonNull View itemView
//                            final onItemClickEventListener deletelistener
        ) {

            super(itemView);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
            this.dday = itemView.findViewById(R.id.dday);
            this.foodname = itemView.findViewById(R.id.foodname);
            this.emoji = itemView.findViewById(R.id.listicon);
//            this.expirydate = itemView.findViewById(R.id.expirydate);

            delete_icon = itemView.findViewById(R.id.delete_icon);
//            delete_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final int position = getAdapterPosition();
//                    deleteData(position);
//
//                }
//            });
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
        holder.emoji.setText(foodData.get(position).getEmoji());
        holder.dday.setText(foodData.get(position).getDday());
        holder.foodname.setText(foodData.get(position).getFoodName());
//        holder.expirydate.setText("유통기한 : "+sample.get(position).getExpirydate());

        String[] dday_split = holder.dday.getText().toString().split("-");
        if (holder.dday.getText().toString().contains("-")){
            if (Integer.parseInt(dday_split[1])>10){
                holder.foreground.setVisibility(View.GONE);
                holder.background.setVisibility(View.GONE);
            }
            else if (Integer.parseInt(dday_split[1])>7){holder.foreground.setBackgroundResource(R.drawable.blue_ddaylist);}
            else if (Integer.parseInt(dday_split[1])>=5){holder.foreground.setBackgroundResource(R.drawable.green_ddaylist);}
            else {holder.foreground.setBackgroundResource(R.drawable.yellow_ddaylist);
            }
        }
        else if (holder.dday.getText().toString().contains("+")){
            holder.foreground.setBackgroundResource(R.drawable.red_ddaylist);
        }
        else {
            holder.foreground.setBackgroundResource(R.drawable.yellow_ddaylist);
        }

    }



    @Override
    public int getItemCount() {
        return foodData.size();
    }

    public void deleteItem(int position){
//        final int position = foodData.getAdapterPosition();
        FoodData deletedfood = foodData.get(position);
        foodData.remove(position);
        notifyItemRemoved(position);

//        Snackbar.make(deletedfood.getFoodName()+" 먹었어요!",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                foodData.add(position,deletedfood);
//                notifyItemInserted(position);
//            }
//        }).show();


        File file = new File("/data/data/com.icecream.icecream_renual/files/"+deletedfood.getFoodName()+".txt");
        file.delete();
    }


}
