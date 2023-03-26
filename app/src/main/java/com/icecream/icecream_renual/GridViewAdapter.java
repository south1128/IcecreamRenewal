package com.icecream.icecream_renual;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

//어댑터
public class GridViewAdapter extends BaseAdapter {
    Context context;
    public GridViewAdapter(Context c){
        context = c;
    }
    String TAG = MainActivity.class.getSimpleName();
    ArrayList<FoodData> item = new ArrayList<FoodData>();

    Func func = new Func();
    MainActivity mainActivity = new MainActivity();
    Boolean delete_mode = MainActivity.delete_mode;

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(FoodData add_item) {
        item.add(add_item);
    }



    public void deleteItem(int position){
//        final int position = foodData.getAdapterPosition();
        FoodData deletedfood = item.get(position);
        item.remove(position);
        notifyDataSetChanged();



        File file = new File("/data/data/com.icecream.icecream_renual/files/"+deletedfood.getFoodName()+".txt");
        file.delete();
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final FoodData foodData = item.get(i);



        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_item_icon, viewGroup, false);

            TextView tv_icon = (TextView) view.findViewById(R.id.foodicon);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_foodname);
            TextView tv_quantity = (TextView) view.findViewById(R.id.tv_foodname);
            ImageView cancel_icon = (ImageView) view.findViewById(R.id.cancel_icon);

            if (delete_mode == true)
                cancel_icon.setVisibility(View.VISIBLE);
            else cancel_icon.setVisibility(View.GONE);

            tv_icon.setText(foodData.getEmoji());
            tv_name.setText(foodData.getFoodName());
//                tv_quantity.setText(itemData.get);
            String dday = foodData.getDday();
            String[] dday_split = dday.split("-");
            if (dday.contains("-")){
                if (Integer.parseInt(dday_split[1])>7){view.setBackgroundResource(R.drawable.item_blue);}
                else if (Integer.parseInt(dday_split[1])>=5){view.setBackgroundResource(R.drawable.item_green);}
                else {view.setBackgroundResource(R.drawable.item_yellow);
                }
            }
            else if (dday.contains("+")){
                view.setBackgroundResource(R.drawable.item_red);
            }
            else {
                view.setBackgroundResource(R.drawable.item_yellow);
            }
//                view.setBackgroundResource(R.drawable.red_ddaylist);

            Log.d(TAG, "getView() - [ " + i + " ] " + foodData.getFoodName());
        } else {
//                View view = new View(context);
////                view_123 = (View) view;
            if (delete_mode == true) {
                view.findViewById(R.id.cancel_icon).setVisibility(View.VISIBLE);
            }
            else view.findViewById(R.id.cancel_icon).setVisibility(View.GONE);
        }
        view.findViewById(R.id.cancel_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodData deletedfood = item.get(i);
                notifyDataSetChanged();
                File file = new File("/data/data/com.icecream.icecream_renual/files/"+deletedfood.getFoodName()+".txt");
                file.delete();
                mainActivity.onResume();
            }
        });
        //아이콘 클릭시 Info 팝업 생성
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    item_info i_info = new item_info();
//                    i_info.setNameText(foodData.getFoodName());
//                    startActivity(new Intent(view.getContext(), item_info.class));

                InfoDialog infoDialog = new InfoDialog(context);
                infoDialog.setNameText(foodData.getFoodName());
                infoDialog.show();
                //intent 애니메이션 효과

                infoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        boolean isUpdated = infoDialog.getButtonStates();
                        if (isUpdated){
                            String[] elements = infoDialog.getelements();
                            String emoji = elements[0];
                            String name = elements[1];
                            String date = elements[2];
                            String[] date_split = date.split("\\."); //YYYY.MM.DD 형식을 YYYY MM DD 로 나누기
                            int year = Integer.parseInt(date_split[0]);
                            int month = Integer.parseInt(date_split[1]);
                            int day = Integer.parseInt(date_split[2]);
                            String category = elements[3];
                            String memo = elements[4];

                            File existingfile = new File(MainActivity.path+foodData.getFoodName()+".txt");
                            existingfile.delete();

                            mainActivity.writeFile(name + ".txt", emoji + "|" + name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo + "|");
                        }
                        mainActivity.onResume();
                    }
                });
                //overridePendingTransition(R.anim.translate_up, R.anim.re_alpha);
            }
        });
        return view;
    }
}