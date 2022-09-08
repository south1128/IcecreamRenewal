package com.example.icecream_renual;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.icecream_renual.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySettingBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        b.sNotification.setOnClickListener(this);
        b.sSetting.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.s_notification){
            startActivity(new Intent(this, NotificationActivity.class));
        }
        if(v.getId() == R.id.s_setting){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
