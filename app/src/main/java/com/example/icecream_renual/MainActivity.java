package com.example.icecream_renual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import androidx.databinding.DataBindingUtil;
import com.example.icecream_renual.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityMainBinding b;

    private GridView gridView_cold;
    private GridView gridView_warm;
    private GridView gridView_freeze;
    private GridViewAdapter adapter_cold;
    private GridViewAdapter adapter_warm;
    private GridViewAdapter adapter_freeze;
    private GridViewAdapter adapter_search;
    //파일 경로
    private String path = "/data/data/com.example.icecream_renual/files/";
    //파일 이름 저장
    File file = new File(path);

    int sort_state = 0; // 0 : default , 1 : name, 2 : date

    Func func = new Func();

    Boolean isAllFabVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_main);
//        setContentView(R.layout.activity_main1);

    }

    @Override
    public void onResume(){
        super.onResume();
        //버튼 생성 및 OnclickListner 선언

        b.fabAdd.setVisibility(View.GONE);
        b.fabCancel.setVisibility(View.GONE);
        b.fabSort.setVisibility(View.GONE);

        isAllFabVisible = false;

        b.fabMain.setOnClickListener(this);
        b.fabAdd.setOnClickListener(this);
        b.fabCancel.setOnClickListener(this);
        b.fabSort.setOnClickListener(this);
        b.btnNotification.setOnClickListener(this);
        b.btnSetting.setOnClickListener(this);

        //GridView에 아이콘 생성
        adapter_cold = new GridViewAdapter();
        adapter_warm = new GridViewAdapter();
        adapter_freeze = new GridViewAdapter();
        gridView_cold = (GridView) findViewById(R.id.field_cold);
        gridView_warm = (GridView) findViewById(R.id.field_warm);
        gridView_freeze = (GridView) findViewById(R.id.field_freeze);

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

                if(category.equals("냉장")){
                    adapter_cold.addItem(new FoodData(name, category, year, month, day));
                    gridView_cold.setAdapter(adapter_cold);
                }
                else if(category.equals("상온")){
                    adapter_warm.addItem(new FoodData(name, category, year, month, day));
                    gridView_warm.setAdapter(adapter_warm);
                }
                else if(category.equals("냉동")){
                    adapter_freeze.addItem(new FoodData(name, category, year, month, day));
                    gridView_freeze.setAdapter(adapter_freeze);
                }
            }
        }
        //검색 기능
        EditText et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //입력값
                String search = et_search.getText().toString();
                //초기화
                adapter_cold = new GridViewAdapter();
                adapter_warm = new GridViewAdapter();
                adapter_freeze = new GridViewAdapter();
                gridView_cold.setAdapter(adapter_cold);
                gridView_warm.setAdapter(adapter_warm);
                gridView_freeze.setAdapter(adapter_freeze);

                for (int i = 0; i < (fileNames.length); i++) {
                    if(fileNames[i].toLowerCase(Locale.ROOT).contains(search)){
                        String rFile = func.readFile(path + fileNames[i]);
                        //읽어온 파일 나누기
                        String[] txt_split = rFile.split("\\|");
                        String name = txt_split[0];
                        int year = Integer.parseInt(txt_split[1]);
                        int month = Integer.parseInt(txt_split[2]);
                        int day = Integer.parseInt(txt_split[3]);
                        int quantity = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }
                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        b.fabAdd.setVisibility(View.GONE);
        b.fabCancel.setVisibility(View.GONE);
        b.fabSort.setVisibility(View.GONE);
//        overridePendingTransition(0,0);
    }


    public void onClick(View v){ //implements View.OnClickListner 추가 필요

        if(!isAllFabVisible){
            b.fabAdd.show();
            b.fabCancel.show();
            b.fabSort.show();
            isAllFabVisible = true;

        }
        else{
            b.fabAdd.hide();
            b.fabCancel.hide();
            b.fabSort.hide();
            isAllFabVisible = false;
        }

        if(v.getId() == R.id.fab_add){
            startActivity(new Intent(this, AddActivity.class));
        }

        if(v.getId() == R.id.fab_cancel){

        }

        if(v.getId() == R.id.fab_sort){
            if(sort_state == 0){
                sort_state = 1;
                adapter_cold = new GridViewAdapter();
                adapter_warm = new GridViewAdapter();
                adapter_freeze = new GridViewAdapter();

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

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }
                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);
                        }
                    }
                }
            }
            else if(sort_state == 1){
                sort_state = 0;
                gridView_cold.setAdapter(null);
                adapter_cold = new GridViewAdapter();
                adapter_warm = new GridViewAdapter();
                adapter_freeze = new GridViewAdapter();

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

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }

                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);

                        }
                    }
                }
            }
        }

        if(v.getId() == R.id.btn_notification){
            startActivity(new Intent(this, NotificationActivity.class));
            overridePendingTransition(0,0);
        }

        if(v.getId() == R.id.btn_setting){
            startActivity(new Intent(this, SettingActivity.class));
            overridePendingTransition(0,0);
        }
    }

    //어댑터
    public class GridViewAdapter extends BaseAdapter {
        String TAG = MainActivity.class.getSimpleName();
        ArrayList<FoodData> item = new ArrayList<FoodData>();

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

//                tv_icon.setImageResource(itemData.getResId());
                tv_name.setText(foodData.getFoodName());
//                tv_quantity.setText(itemData.get);
                view.setBackgroundResource(R.drawable.red_ddaylist);

                Log.d(TAG, "getView() - [ " + i + " ] " + foodData.getFoodName());
            } else {
                View view_123 = new View(context);
                view_123 = (View) view;
            }

            //아이콘 클릭시 Info 팝업 생성
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_info i_info = new item_info();
                    i_info.setNameText(foodData.getFoodName());
                    startActivity(new Intent(view.getContext(), item_info.class));
                    //intent 애니메이션 효과
                    overridePendingTransition(R.anim.translate_up, R.anim.re_alpha);
                }
            });
            return view;
        }
    }
}