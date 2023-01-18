package com.example.icecream_renual;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import com.example.icecream_renual.databinding.ActivityMainBinding;

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

    FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String s) {
            return s.contains(".txt");
        }
    };

    int sort_state = 0; // 0 : default , 1 : name, 2 : date
    boolean delete_mode = false;
    Func func = new Func();

    Boolean isAllFabVisible;

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            CharSequence name = getString(R.string.channel_name);
////            String description = getString(R.string.channel_description);
//            CharSequence name = "Icecream fridge";
//            String description = "This is Icecream project";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_main);
//        setContentView(R.layout.activity_main1);
        boolean directorycreate = file.mkdir();

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
        Log.v("HelloAlarmActivity", mCalender.getTime().toString());

        setAlarm();

//        Intent intent = new Intent(this, NotificationActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.fridge)
//                .setContentTitle("my notification")
//                .setContentText("this is my notification")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
////        notificationManager.notify(notificationId, builder.build());
//
//        notificationManager.notify(0, builder.build());


    }


    @Override
    public void onResume(){
        super.onResume();
        //버튼 생성 및 OnclickListner 선언

        b.fabAdd.setVisibility(View.GONE);
        b.fabCancel.setVisibility(View.GONE);
        b.fabSort.setVisibility(View.GONE);
        b.sortText.setVisibility(View.GONE);


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
        freeze = 0;
        cold_count = 0;

        //파일 읽기
//        FilenameFilter filter = new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String s) {
//                return s.contains(".txt");
//            }
//        };
        String[] fileNames = file.list(filter);
        if (fileNames.length > 0) {
            for (int i = 0; i < (fileNames.length); i++) {
                fileDivision(fileNames[i]);
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
                freeze = 0;
                cold_count = 0;

                for (int i = 0; i < (fileNames.length); i++) {
                    if(fileNames[i].toLowerCase(Locale.ROOT).contains(search)){
                        fileDivision(fileNames[i]);
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v){ //implements View.OnClickListner 추가 필요

        if(!isAllFabVisible){
            b.fabAdd.show();
            b.fabCancel.show();
            b.fabSort.show();
            b.sortText.setVisibility(View.VISIBLE);
            isAllFabVisible = true;

        }
        else{
            b.fabAdd.hide();
            b.fabCancel.hide();
            b.fabSort.hide();
            b.sortText.setVisibility(View.GONE);
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
            //createNotificationChannel("DEFAULT", "default channel", NotificationManager.IMPORTANCE_HIGH);
            //createNofification("DEFAULT", 1, "아이스크림 제목", "아이스크림 내용");
            if (delete_mode == false){
                delete_mode = true;
            }
            else delete_mode = false;

            if(sort_state == 1){
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
                        fileDivision(fileName[i]);
                    }
                }
            }
            else if(sort_state == 2){
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
                        fileDivision(fileName[i]);
                    }
                }
            }
            else if(sort_state == 0){
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
                        fileDivision(fileName[i]);
                    }
                }
            }


        }

        if(v.getId() == R.id.fab_sort){
            cold = 0;
            warm = 0;
            freeze = 0;
            cold_count = 0;
            if(sort_state == 0){
                    sort_state = 1;
                    b.sortText.setText("sortstate_1");
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
                        fileDivision(fileName[i]);
                    }
                }
            }
            else if(sort_state == 1){
                sort_state = 2;
                b.sortText.setText("sortstate_2");
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
                        fileDivision(fileName[i]);
                    }
                }
            }
            else if(sort_state == 2){
                sort_state = 0;
                b.sortText.setText("sortstate_0");
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
                        fileDivision(fileName[i]);
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

                    writeFile(name + ".txt", emoji + "|" +name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo +"|");

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

        public void deleteItem(int position){
//        final int position = foodData.getAdapterPosition();
            FoodData deletedfood = item.get(position);
            item.remove(position);
//            notifyItemRemoved(position);


            File file = new File("/data/data/com.example.icecream_renual/files/"+deletedfood.getFoodName()+".txt");
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
                View view_123 = new View(context);
                view_123 = (View) view;
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
                    File file = new File("/data/data/com.example.icecream_renual/files/"+deletedfood.getFoodName()+".txt");
                    file.delete();
                }
            });
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

                                writeFile(name + ".txt", emoji + "|" + name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo+"|");
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


    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE);


        String from = SettingActivity.getTime();
//        String from = "06:53:00";

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        //AlarmManager.INTERVAL_DAY < 하루주기
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_FIFTEEN_MINUTES,pendingIntent);

    }

    public void fileDivision(String fName){
        String rFile = func.readFile(path + fName);
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
            adapter_freeze.addItem(new FoodData(emoji, name, category, year, month, day));
            gridView_freeze.setAdapter(adapter_freeze);
        }
    }
}