package com.example.icecream_renual;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icecream_renual.databinding.ActivityNotificationBinding;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

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
    ArrayList<SampleData> notification_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        notification_list = new ArrayList<SampleData>();
    }

    @Override
    public void onResume() {
        super.onResume();
        b.nNotification.setOnClickListener(this);
        b.nSetting.setOnClickListener(this);
        notification_list = new ArrayList<SampleData>();
        newAdapter = new NewAdapter(this,notification_list);
        this.InitializeFoodData();

        b.ddayList.setLayoutManager(new LinearLayoutManager(this));
        b.ddayList.setAdapter(newAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
    public void InitializeFoodData() {
//
        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = count; i < (fileNames.length); i++) {
                String rFile = func.readFile("/data/data/com.example.icecream_renual/files/" + fileNames[i]);
                //읽어온 파일 나누기
                String[] txt_split = rFile.split("\\|");
                String foodname = txt_split[0];
                String category = txt_split[5];
                int year = Integer.parseInt(txt_split[1]);
                int month = Integer.parseInt(txt_split[2]);
                int day = Integer.parseInt(txt_split[3]);
                //int quantity = Integer.parseInt(txt_split[1]);
                count++;

                notification_list.add(new SampleData(foodname,year, month, day));
                b.ddayList.setAdapter(newAdapter);
            }
            count = 0;
        }
    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.n_notification){
            startActivity(new Intent(this, MainActivity.class));
        }
        if(v.getId() == R.id.n_setting){
            startActivity(new Intent(this,SettingActivity.class));
        }
    }
}
