package com.example.icecream_renual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_main;
    private Button btn_add;
    private Button btn_remove;
    private Button btn_sort;
    private Button btn_notification;
    private Button btn_setting;

    private GridView gridView_cold;
    private GridViewAdapter adapter;
    //파일 경로
    private String path = "/data/data/com.example.icecream_renual/files/";
    //파일 이름 저장
    File file = new File(path);
    int count = 0;

    int sort_state = 0; // 0 : default , 1 : name, 2 : 날짜기준

    Func func = new Func();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onResume(){
        super.onResume();
        //버튼 생성 및 OnclickListner 선언
        btn_main = (Button) findViewById(R.id.btn_main);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_sort = (Button) findViewById(R.id.btn_sort);
        btn_notification = (Button) findViewById(R.id.btn_notification);
        btn_setting = (Button) findViewById(R.id.btn_setting);

        btn_main.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_sort.setOnClickListener(this);

        //GridView에 아이콘 생성
        adapter = new GridViewAdapter();
        gridView_cold = (GridView) findViewById(R.id.field_cold);

        //파일 읽기
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(".txt");
            }
        };
        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = 0; i < (fileNames.length); i++) {
                String rFile = func.readFile(path + fileNames[i]);
                //읽어온 파일 나누기
                String[] txt_split = rFile.split("\\|");
                String name = txt_split[0];
                int year = Integer.parseInt(txt_split[1]);
                int month = Integer.parseInt(txt_split[2]);
                int day = Integer.parseInt(txt_split[3]);
                int quantity = Integer.parseInt(txt_split[4]);
                String category = txt_split[5];
                count++;

                adapter.addItem(new ItemData(name, category, year, month, day, R.mipmap.ic_launcher));
                gridView_cold.setAdapter(adapter);
            }
        }
    }

    public void onClick(View v){ //implements View.OnClickListner 추가 필요
        //main 버튼
        if(v.getId() == R.id.btn_main){
            if(btn_add.getVisibility() == View.INVISIBLE){
                btn_add.setVisibility(View.VISIBLE);
                btn_remove.setVisibility(View.VISIBLE);
                btn_sort.setVisibility(View.VISIBLE);
                Animation animation_alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
                btn_add.startAnimation(animation_alpha);
                btn_remove.startAnimation(animation_alpha);
                btn_sort.startAnimation(animation_alpha);

            }
            else {
                btn_add.setVisibility(View.INVISIBLE);
                btn_remove.setVisibility(View.INVISIBLE);
                btn_sort.setVisibility(View.INVISIBLE);
                Animation animation_realpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.re_alpha);
                btn_add.startAnimation(animation_realpha);
                btn_remove.startAnimation(animation_realpha);
                btn_sort.startAnimation(animation_realpha);
            }
        }

        if(v.getId() == R.id.btn_add){
            startActivity(new Intent(this, AddActivity.class));
        }

        if(v.getId() == R.id.btn_remove){

        }

        if(v.getId() == R.id.btn_sort){
            if(sort_state == 0){
                sort_state = 1;
                adapter = new GridViewAdapter();

                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String name) {
                        return name.contains(".txt");
                    }
                };
                String[] fileName = file.list(filter);
                Arrays.sort(fileName);
                if (fileName.length > 0) {
                    for (int i = 0; i < (fileName.length); i++) {
                        String rFile = func.readFile(path + fileName[i]);
                        //읽어온 파일 나누기
                        String[] txt_split = rFile.split("\\|");
                        String name = txt_split[0];
                        int year = Integer.parseInt(txt_split[1]);
                        int month = Integer.parseInt(txt_split[2]);
                        int day = Integer.parseInt(txt_split[3]);
                        int quantity = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];
                        count++;

                        adapter.addItem(new ItemData(name, category, year, month, day, R.mipmap.ic_launcher));
                        gridView_cold.setAdapter(adapter);
                    }
                }
            }
            else if(sort_state == 1){
                sort_state = 0;
                gridView_cold.setAdapter(null);
                adapter = new GridViewAdapter();

                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String name) {
                        return name.contains(".txt");
                    }
                };
                String[] fileName = file.list(filter);
                if (fileName.length > 0) {
                    for (int i = 0; i < (fileName.length); i++) {
                        String rFile = func.readFile(path + fileName[i]);
                        //읽어온 파일 나누기
                        String[] txt_split = rFile.split("\\|");
                        String name = txt_split[0];
                        int year = Integer.parseInt(txt_split[1]);
                        int month = Integer.parseInt(txt_split[2]);
                        int day = Integer.parseInt(txt_split[3]);
                        int quantity = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];
                        count++;

                        adapter.addItem(new ItemData(name, category, year, month, day, R.mipmap.ic_launcher));
                        gridView_cold.setAdapter(adapter);
                    }
                }
            }

        }

        if(v.getId() == R.id.btn_notification){

        }

        if(v.getId() == R.id.btn_setting){

        }
    }

    //어댑터
    public class GridViewAdapter extends BaseAdapter {
        String TAG = MainActivity.class.getSimpleName();
        ArrayList<ItemData> item = new ArrayList<ItemData>();

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

        public void addItem(ItemData add_item) {
            item.add(add_item);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final ItemData itemData = item.get(i);

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_item_icon, viewGroup, false);

                ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

                iv_icon.setImageResource(itemData.getResId());
                tv_name.setText(itemData.getName());

                Log.d(TAG, "getView() - [ " + i + " ] " + itemData.getName());
            } else {
                View view_123 = new View(context);
                view_123 = (View) view;
            }

            //아이콘 클릭시 Info 팝업 생성
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_info i_info = new item_info();
                    i_info.setNameText(itemData.getName());
                    startActivity(new Intent(view.getContext(), item_info.class));
                    //intent 애니메이션 효과
                    overridePendingTransition(R.anim.translate_up, R.anim.re_alpha);
                }
            });

            return view;
        }
    }

}