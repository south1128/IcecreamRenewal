package com.example.icecream_renual;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecream_renual.databinding.ActivityNotificationBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements SwipetoDelete.SwipetoDeleteListener {

    ActivityNotificationBinding b;
    private String path = "/data/data/com.example.icecream_renual/files/";
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        notification_list = new ArrayList<FoodData>();
        Intent tomain = new Intent(this,MainActivity.class);
        tomain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        b.nNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(tomain);
            }
        });
        b.nSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tomain);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        notification_list = new ArrayList<FoodData>();
        newAdapter = new NewAdapter(this,notification_list);
//        newAdapter.setClickListener(this);
        this.InitializeFoodData();

        b.ddayList.setLayoutManager(new LinearLayoutManager(this));
        b.ddayList.setItemAnimator(new DefaultItemAnimator());
        b.ddayList.setAdapter(newAdapter);

        ItemTouchHelper.SimpleCallback Swipetodelete = new SwipetoDelete(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(Swipetodelete).attachToRecyclerView(b.ddayList);




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
    }
    public void InitializeFoodData() {

        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = count; i < (fileNames.length); i++) {
                String rFile = func.readFile(path + fileNames[i]);
                //읽어온 파일 나누기
                String[] txt_split = rFile.split("\\|");
                String name = txt_split[0];
                String category = txt_split[5];
                int year = Integer.parseInt(txt_split[1]);
                int month = Integer.parseInt(txt_split[2]);
                int day = Integer.parseInt(txt_split[3]);
                //int quantity = Integer.parseInt(txt_split[1]);
                count++;

                notification_list.add(new FoodData(name,category, year, month, day));
                b.ddayList.setAdapter(newAdapter);
            }
            count = 0;
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
}
