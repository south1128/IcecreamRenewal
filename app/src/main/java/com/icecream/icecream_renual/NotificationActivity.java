package com.icecream.icecream_renual;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icecream.icecream_renual.databinding.ActivityNotificationBinding;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity implements SwipetoDelete.SwipetoDeleteListener {

    ActivityNotificationBinding b;
    private String path = "/data/data/com.icecream.icecream_renual/files/";
    //파일 이름 저장
    File file = new File(path);
    int count = 0;

    Func func = new Func();
    FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(".txt");
        }
    };

    NewAdapter newAdapter;
    ArrayList<FoodData> notification_list;

    SharedPreferences sharedPreferences;
    String shared = "thememode";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        notification_list = new ArrayList<FoodData>();
        Intent tomain = new Intent(this,MainActivity.class);
        tomain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent tosetting = new Intent(this,SettingActivity.class);
        tosetting.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        b.nNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(tomain);
            }
        });
        b.nSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tosetting);
            }
        });
        Log.i(TAG,"n.onCreate()");
    }
    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        sharedPreferences = getSharedPreferences(shared,0);
        String value = sharedPreferences.getString("color","");
        if (Objects.equals(value, "red")){
            theme.applyStyle(R.style.Theme_App_Red,true);
        }
        else if (Objects.equals(value, "yellow")){
            theme.applyStyle(R.style.Theme_App_Yellow,true);
        }
        else if (Objects.equals(value, "green")){
            theme.applyStyle(R.style.Theme_App_green,true);
        }
        else {theme.applyStyle(R.style.Theme_Icecream_renual,true);}

        return theme;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "n.onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "n.onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "n.onDestroy()");
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "n.onRestart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        notification_list = new ArrayList<FoodData>();
        newAdapter = new NewAdapter(this,notification_list);
//        newAdapter.setClickListener(this);
        this.InitializeFoodData();

        b.rvDdayList.setLayoutManager(new LinearLayoutManager(this));
        b.rvDdayList.setItemAnimator(new DefaultItemAnimator());
        b.rvDdayList.setAdapter(newAdapter);

        ItemTouchHelper.SimpleCallback Swipetodelete = new SwipetoDelete(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(Swipetodelete).attachToRecyclerView(b.rvDdayList);


        Log.i(TAG, "n.onResume()");

    }

//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
////        @Override
////        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
////            if (viewHolder != null) {
////                final View foregroundView = ((NewAdapter.MyViewHolder) viewHolder).foreground;
////                getDefaultUIUtil().onSelected(foregroundView);
////            }
////        }
////        @Override
//        public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
//                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                                    int actionState, boolean isCurrentlyActive) {
//            final View foregroundView = ((NewAdapter.MyViewHolder) viewHolder).foreground;
//            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX/6, dY,
//                    actionState, isCurrentlyActive);
//        }
//        // swipe 또는 drag 된 view 가 drop 되었을때 (다른 실행 없이 다른 view 가 선택되었을때 이미 선택되어 있던 뷰 clear)
//        @Override
//        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//            final View foregroundView = ((NewAdapter.MyViewHolder) viewHolder).foreground;
//            getDefaultUIUtil().clearView(foregroundView);
//        }
//        // swipe 시 나타나는 view 의 변화 적용 ( 이 경우 background 나타남)
//        @Override
//        public void onChildDraw(Canvas c, RecyclerView recyclerView,
//                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                                int actionState, boolean isCurrentlyActive) {
//            final View foregroundView = ((NewAdapter.MyViewHolder) viewHolder).foreground;
//
//            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX/6, dY, actionState, isCurrentlyActive);
//
//        }
//        // swipe 가 일어날 때 동작
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            final int position = viewHolder.getAdapterPosition();
//            ImageView delete = viewHolder.itemView.findViewById(R.id.delete_icon);
////            delete.setVisibility(View.VISIBLE);
////            delete.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
//////                    delete.setVisibility(View.GONE);
////                    Toast.makeText(NotificationActivity.this,"deleted",Toast.LENGTH_LONG).show();
////                }
////            });
////            Toast.makeText(NotificationActivity.this,"deleted",Toast.LENGTH_LONG).show();
////            SampleData deletedfood = notification_list.get(viewHolder.getAdapterPosition());
////            newAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
////            File file = new File("/data/data/com.example.icecream_renual/files/"+deletedfood.getFoodname()+".txt");
////            Snackbar.make(b.ddayList,deletedfood.getFoodname()+" 먹었어요!",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    notification_list.add(position,deletedfood);
////                    newAdapter.notifyItemInserted(position);
////                }
////            }).show();
////            file.delete();
//        }
//    };

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        Log.i(TAG, "n.onPause()");
    }
    public void InitializeFoodData() {
        
        ArrayList<sort_by_dday> howmanydays_list = new ArrayList<sort_by_dday>();

        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = count; i < (fileNames.length); i++) {
                String rFile = func.readFile(path + fileNames[i]);
                //읽어온 파일 나누기
                String[] txt_split = rFile.split("\\|");
                String emoji = txt_split[0];
                String name = txt_split[1];
                String category = txt_split[5];
                int year = Integer.parseInt(txt_split[2]);
                int month = Integer.parseInt(txt_split[3]);
                int day = Integer.parseInt(txt_split[4]);
                //int quantity = Integer.parseInt(txt_split[1]);
                int order = year*10000 + month*100 + day;
                count++;

                howmanydays_list.add(new sort_by_dday(name, order));

                notification_list.add(new FoodData(emoji,name,category, year, month, day));
                b.rvDdayList.setAdapter(newAdapter);
            }
            count = 0;
            Collections.sort(howmanydays_list,Collections.reverseOrder());


        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof NewAdapter.MyViewHolder){
            newAdapter.deleteItem(viewHolder.getAdapterPosition());
        }

    }

    @Override
    public void onClick(RecyclerView.ViewHolder viewHolder, boolean isCurrentActive, int position) {
        if (isCurrentActive){
            newAdapter.deleteItem(viewHolder.getAdapterPosition());
        }
    }

    private class sort_by_dday implements Comparable<sort_by_dday> {
        private String name ;
        private int order;
        public sort_by_dday(String name, int order) {
            this.name = name;
            this.order = order;
        }

        @Override
        public int compareTo(sort_by_dday sort_by_dday) {
            if(sort_by_dday.order<order){
                return 1;
            }else if (sort_by_dday.order>order){
                return -1;
            }
            return 0;
        }
    }
}


//https://stackoverflow.com/questions/57865125/how-can-i-force-user-to-enter-emoji-only-in-edittext