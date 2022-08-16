package com.example.icecream_renual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_add;
    private Button btn_remove;
    private Button btn_sort;
    private Button btn_notification;
    private Button btn_setting;

    private GridView gridView;
    private GridViewAdapter adapter;
    //파일 경로
    private String path = "/data/data/com.example.icecream_renual/files";
    //파일 이름 저장
    File file = new File(path);
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Func funfun = new Func();

        //버튼 생성 및 OnclickListner 선언
        Button btn_main = (Button) findViewById(R.id.btn_main);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_sort = (Button) findViewById(R.id.btn_sort);
        btn_notification = (Button) findViewById(R.id.btn_notification);
        btn_setting = (Button) findViewById(R.id.btn_setting);

        btn_main.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        //GridView에 아이콘 생성
//        adapter = new GridViewAdapter();
//        gridView = (GridView) findViewById(R.id.field_cold);

        //파일 읽기
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(".txt");
            }
        };
        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = count; i < (fileNames.length); i++) {
                String rFile = funfun.readFile("/data/data/com.example.icecream/files/" + "jj");
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
                gridView.setAdapter(adapter);
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
            }
            else {
                btn_add.setVisibility(View.INVISIBLE);
                btn_remove.setVisibility(View.INVISIBLE);
                btn_sort.setVisibility(View.INVISIBLE);
            }
        }

        if(v.getId() == R.id.btn_add){
            startActivity(new Intent(this, AddActivity.class));
        }

        if(v.getId() == R.id.btn_remove){

        }

        if(v.getId() == R.id.btn_sort){

        }

        if(v.getId() == R.id.btn_notification){

        }

        if(v.getId() == R.id.btn_setting){

        }
    }

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
            final ItemData myitem = item.get(i);

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_item_icon, viewGroup, false);

                ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

                iv_icon.setImageResource(myitem.getResId());
                tv_name.setText(myitem.getName());

                Log.d(TAG, "getView() - [ " + i + " ] " + myitem.getName());
            } else {
                View view_1 = new View(context);
                view_1 = (View) view;
            }

            //아이콘 클릭시 Info 팝업 생성
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return view;
        }
    }

}