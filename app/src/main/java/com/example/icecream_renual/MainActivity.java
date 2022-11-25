package com.example.icecream_renual;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
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

    private LinearLayout ll_cold;
    private LinearLayout ll_warm;
    private LinearLayout ll_freeze;
    private int cold = 0;
    private int cold_count = 0;
    private int warm = 0;
    private int freeze = 0;

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
        boolean directorycreate = file.mkdir();
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

        ll_cold = (LinearLayout) findViewById(R.id.linearLayout_gridtableLayout1);
        ll_warm = (LinearLayout) findViewById(R.id.linearLayout_gridtableLayout2);
        ll_freeze = (LinearLayout) findViewById(R.id.linearLayout_gridtableLayout3);

        cold = 0;
        warm = 0;
        cold_count = 0;

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
                String emoji = txt_split[0];
                String name = txt_split[1];
                int year = Integer.parseInt(txt_split[2]);
                int month = Integer.parseInt(txt_split[3]);
                int day = Integer.parseInt(txt_split[4]);
                String category = txt_split[5];

                if(category.equals("냉장")){
                    cold_count++;
                    if(cold_count%2 == 1){
                        cold = cold + 240;
                        ll_cold.getLayoutParams().width = cold;
                    }
                    adapter_cold.addItem(new FoodData(emoji, name, category, year, month, day));
                    gridView_cold.setAdapter(adapter_cold);
                }
                else if(category.equals("상온")){
                    warm = warm + 240;
                    ll_warm.getLayoutParams().width = warm;
                    adapter_warm.addItem(new FoodData(emoji, name, category, year, month, day));
                    gridView_warm.setAdapter(adapter_warm);
                }
                else if(category.equals("냉동")){
                    freeze = freeze + 240;
                    ll_freeze.getLayoutParams().width = freeze;
                    adapter_freeze.addItem(new FoodData(emoji,name, category, year, month, day));
                    gridView_freeze.setAdapter(adapter_freeze);
                }
            }
        }
        //검색 기능
//        EditText et_search = (EditText) findViewById(R.id.et_search);
        b.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //입력값
                String search = b.etSearch.getText().toString();
                //초기화
                adapter_cold = new GridViewAdapter();
                adapter_warm = new GridViewAdapter();
                adapter_freeze = new GridViewAdapter();
                gridView_cold.setAdapter(adapter_cold);
                gridView_warm.setAdapter(adapter_warm);
                gridView_freeze.setAdapter(adapter_freeze);

                cold = 0;
                warm = 0;
                cold_count = 0;

                for (int i = 0; i < (fileNames.length); i++) {
                    if(fileNames[i].toLowerCase(Locale.ROOT).contains(search)){
                        String rFile = func.readFile(path + fileNames[i]);
                        //읽어온 파일 나누기
                        String[] txt_split = rFile.split("\\|");
                        String emoji = txt_split[0];
                        String name = txt_split[1];
                        int year = Integer.parseInt(txt_split[2]);
                        int month = Integer.parseInt(txt_split[3]);
                        int day = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];

                        if(category.equals("냉장")){
                            cold_count++;
                            if(cold_count%2 == 1){
                                cold = cold + 240;
                                ll_cold.getLayoutParams().width = cold;
                            }
                            adapter_cold.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }
                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        b.fabAdd.setVisibility(View.GONE);
        b.fabCancel.setVisibility(View.GONE);
        b.fabSort.setVisibility(View.GONE);
        super.onPause();
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
//            startActivity(new Intent(this, AddActivity.class));
            addDialogMethod();
//            adapter_cold = new GridViewAdapter();
//            adapter_warm = new GridViewAdapter();
//            adapter_freeze = new GridViewAdapter();
            gridView_cold.setAdapter(adapter_cold);
            gridView_warm.setAdapter(adapter_warm);
            gridView_freeze.setAdapter(adapter_freeze);
            onPause();
        }

        if(v.getId() == R.id.fab_cancel){
            //https://mrw0119.tistory.com/146
            createNotificationChannel("DEFAULT", "default channel", NotificationManager.IMPORTANCE_HIGH);
            createNofification("DEFAULT", 1, "아이스크림 제목", "아이스크림 내용");

            alarmBroadcastReceiver();
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
                        String emoji = txt_split[0];
                        String name = txt_split[1];
                        int year = Integer.parseInt(txt_split[2]);
                        int month = Integer.parseInt(txt_split[3]);
                        int day = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }
                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);
                        }
                    }
                }
            }
            else if(sort_state == 1){
                sort_state = 2;
                gridView_cold.setAdapter(null);
                adapter_cold = new GridViewAdapter();
                adapter_warm = new GridViewAdapter();
                adapter_freeze = new GridViewAdapter();

                ArrayList<Name_Dday_Sort> name_dday = new ArrayList<Name_Dday_Sort>();

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
                        String name = txt_split[1];
                        int year = Integer.parseInt(txt_split[2]);
                        int month = Integer.parseInt(txt_split[3]);
                        int day = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];
                        name_dday.add(new Name_Dday_Sort(name, calculateDday(year, month, day)));
                    }
                }
                Collections.sort(name_dday);
                if (fileName.length > 0) {
                    for (int i = 0; i < (fileName.length); i++) {
                        String rFile = func.readFile(path + name_dday.get(i).getName() + ".txt");
                        //읽어온 파일 나누기
                        String[] txt_split = rFile.split("\\|");
                        String emoji = txt_split[0];
                        String name = txt_split[1];
                        int year = Integer.parseInt(txt_split[2]);
                        int month = Integer.parseInt(txt_split[3]);
                        int day = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }

                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);

                        }
                    }
                }
            }
            else if(sort_state == 2){
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
                        String emoji = txt_split[0];
                        String name = txt_split[1];
                        int year = Integer.parseInt(txt_split[2]);
                        int month = Integer.parseInt(txt_split[3]);
                        int day = Integer.parseInt(txt_split[4]);
                        String category = txt_split[5];

                        if(category.equals("냉장")){
                            adapter_cold.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_cold.setAdapter(adapter_cold);
                        }

                        else if(category.equals("상온")){
                            adapter_warm.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_warm.setAdapter(adapter_warm);
                        }
                        else if(category.equals("냉동")){
                            adapter_freeze.addItem(new FoodData(emoji,name, category, year, month, day));
                            gridView_freeze.setAdapter(adapter_freeze);

                        }
                    }
                }
            }
        }

        if(v.getId() == R.id.btn_notification){
            Intent tonotification = new Intent(this,NotificationActivity.class);
            tonotification.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tonotification);
            overridePendingTransition(0,0);
        }

        if(v.getId() == R.id.btn_setting){
            Intent tosetting = new Intent(this,SettingActivity.class);
            tosetting.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tosetting);
            overridePendingTransition(0,0);
        }
    }

    public void addDialogMethod(){
        final AddDialog addDialog = new AddDialog(this);
        addDialog.show();

        addDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                boolean isSaved = addDialog.getButtonStates();
                if (isSaved == true) {
                    String[] elements = addDialog.getelements();
                    String emoji = elements[0];
                    String name = elements[1];
                    String date = elements[2];
                    String[] date_split = date.split("\\."); //YYYY.MM.DD 형식을 YYYY MM DD 로 나누기
                    int year = Integer.parseInt(date_split[0]);
                    int month = Integer.parseInt(date_split[1]);
                    int day = Integer.parseInt(date_split[2]);
                    String category = elements[3];
                    String memo = elements[4];

                    writeFile(name + ".txt", emoji + "|" +name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo);

                }
                onResume();
            }
        });
    }
    public void writeFile(String fileName, String msg){
        try{
            OutputStreamWriter oStreamWriter = new OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE));
            oStreamWriter.write(msg);
            oStreamWriter.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
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
                View view_123 = new View(context);
                view_123 = (View) view;
            }

            //아이콘 클릭시 Info 팝업 생성
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    item_info i_info = new item_info();
//                    i_info.setNameText(foodData.getFoodName());
//                    startActivity(new Intent(view.getContext(), item_info.class));

                    InfoDialog infoDialog = new InfoDialog(MainActivity.this);
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

                                File existingfile = new File(path+foodData.getFoodName()+".txt");
                                existingfile.delete();

                                writeFile(name + ".txt", emoji + "|" + name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo);
                            }
                            onResume();

                        }
                    });
                    overridePendingTransition(R.anim.translate_up, R.anim.re_alpha);
                }
            });
            return view;
        }

    }


    public int calculateDday(int e_year, int e_month, int e_day){

        // today date
        Calendar calendar = Calendar.getInstance();
        int n_year = calendar.get(Calendar.YEAR);
        int n_month = calendar.get(Calendar.MONTH);
        int n_day = calendar.get(Calendar.DAY_OF_MONTH);

        long today = calendar.getTimeInMillis();

        Calendar e_Calendar = Calendar.getInstance();
        e_Calendar.set(e_year,e_month-1,e_day);
        long expiryday = e_Calendar.getTimeInMillis();

        long result = (expiryday-today)/(24*60*60*1000);
        int remaindays = (int)result;

        return remaindays;
    }

    public class Name_Dday_Sort implements Comparable<Name_Dday_Sort>{
        private String name;
        private int dday;

        public Name_Dday_Sort(String name, int dday){
            this.name = name;
            this.dday = dday;
        }

        public int compareTo(Name_Dday_Sort nds){
            if(nds.dday < dday){
                return 1;
            }
            else if(nds.dday > dday){
                return -1;
            }
            return 0;
        }

        public String getName(){
            return name;
        }
    }

    public void createNotificationChannel(String channelId, String channelName, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    public void createNofification(String channelID, int id, String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_main_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public void alarmBroadcastReceiver(){
        Intent alarmBroadcastReceiverintent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmBroadcastReceiverintent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the alarm to start at a particular time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 06);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);

    }

    public class AlarmBroadcastReceiver extends BroadcastReceiver{
        public AlarmBroadcastReceiver(){
        }
        @Override
        public void onReceive(Context context, Intent intent){
            Intent alarmIntentServiceIntent = new Intent(context, AlarmIntentService.class);
            context.startService(alarmIntentServiceIntent);
        }
    }

    public class AlarmIntentService extends IntentService{
        public final int NOTIFICATION_ID = 1001;

        public AlarmIntentService(){
            super("AlarmIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent){
            new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        }
    }
}