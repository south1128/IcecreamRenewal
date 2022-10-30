package com.example.icecream_renual;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class InfoDialog extends Dialog {
    public InfoDialog(@NonNull Context context) {
        super(context);
    }

    private static String name;
    boolean editMode = false;

    String category;

    TextView info_name,info_date,info_category,info_memo,et_info_date;
    EditText et_info_name,et_info_memo;
    Spinner s_info_category;
    TextView tv_positive,tv_negative;
    ImageView btn_edit,btn_delete,btn_cancel,btn_save,btn_calendar;

    String path = "/data/data/com.example.icecream_renual/files/";

    boolean buttonState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info);

        info_name = findViewById(R.id.tv_info_foodname);
        info_date = findViewById(R.id.tv_info_date);
        info_category = findViewById(R.id.tv_info_category);
        info_memo = findViewById(R.id.tv_info_memo);
        et_info_name = findViewById(R.id.et_info_foodname);
        et_info_date = findViewById(R.id.et_info_date);
        s_info_category = findViewById(R.id.s_info_category);
        et_info_memo = findViewById(R.id.et_info_memo);
        tv_positive = findViewById(R.id.tv_positive);
        tv_negative = findViewById(R.id.tv_negative);

        btn_delete = findViewById(R.id.btn_delete);
        btn_edit = findViewById(R.id.btn_edit);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        btn_calendar = findViewById(R.id.btn_calendar);


        buttonState = false;

        String rFile = readFile(path + name + ".txt");
        //읽어온 파일 나누기
        String[] txt_split = rFile.split("\\|");
        String itemName = txt_split[0];
        int itemYear = Integer.parseInt(txt_split[1]);
        int itemMonth = Integer.parseInt(txt_split[2]);
        int itemDay = Integer.parseInt(txt_split[3]);
        String itemCategory = txt_split[4];
        String memo = txt_split[5];


        info_name.setText(itemName);
        info_date.setText(itemYear + "." + itemMonth + "." + itemDay);
        info_category.setText(itemCategory);
        info_memo.setText(memo);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonState = false;
                editMode = true;
                ModeChange();
                ;
                s_info_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       category = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
//                AddDialog addDialog = new AddDialog(getContext());
//                addDialog.show();
//                addDialog.tv_delete.setText("cancel");
//                addDialog.et_name.setText(itemName);
//                addDialog.et_date.setText(itemYear + "년" + itemMonth + "월" + itemDay + "일");
//                ArrayAdapter spinnerAdapter = (ArrayAdapter) addDialog.s_category.getAdapter();
//                int spinnerposition = spinnerAdapter.getPosition(itemCategory);
//                addDialog.s_category.setSelection(spinnerposition);
//                addDialog.et_memo.setText(memo);


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonState = false;
                editMode = false;
                ModeChange();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonState = false;
                File file = new File(path+name+".txt");
                file.delete();
                dismiss();

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonState = true;
                dismiss();

            }
        });
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),R.style.Datepicker, datepicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#326199"));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#326199"));

            }
        });
    }


    private void ModeChange() {

        if (editMode){
            btn_edit.setVisibility(View.INVISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);


            info_name.setVisibility(View.INVISIBLE);
            info_date.setVisibility(View.INVISIBLE);
            info_category.setVisibility(View.INVISIBLE);
            info_memo.setVisibility(View.INVISIBLE);

            et_info_name.setVisibility(View.VISIBLE);
            et_info_date.setVisibility(View.VISIBLE);
            s_info_category.setVisibility(View.VISIBLE);
            et_info_memo.setVisibility(View.VISIBLE);

            btn_calendar.setVisibility(View.VISIBLE);

            et_info_name.setText(info_name.getText());
            et_info_date.setText(info_date.getText());
            ArrayAdapter spinnerAdapter = (ArrayAdapter) s_info_category.getAdapter();
            int spinnerposition = spinnerAdapter.getPosition(info_category.getText());
            s_info_category.setSelection(spinnerposition);
            et_info_memo.setText(info_memo.getText());

            tv_positive.setText("save");
            tv_negative.setText("cancel");
        }
        else {
            btn_edit.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.INVISIBLE);
            btn_cancel.setVisibility(View.INVISIBLE);


            info_name.setVisibility(View.VISIBLE);
            info_date.setVisibility(View.VISIBLE);
            info_category.setVisibility(View.VISIBLE);
            info_memo.setVisibility(View.VISIBLE);

            et_info_name.setVisibility(View.INVISIBLE);
            et_info_date.setVisibility(View.INVISIBLE);
            s_info_category.setVisibility(View.INVISIBLE);
            et_info_memo.setVisibility(View.INVISIBLE);

            btn_calendar.setVisibility(View.INVISIBLE);

            tv_positive.setText("edit");
            tv_negative.setText("delete");
        }
    }

    public boolean getButtonStates(){return buttonState;}
    public String[] getelements(){
        String name = et_info_name.getText().toString();
        //유통기한 (년 | 월 | 일 로 나눠서 저장)
        String date = et_info_date.getText().toString();
//        String[] date_split = date.split("\\."); //YYYY.MM.DD 형식을 YYYY MM DD 로 나누기
//        int year = Integer.parseInt(date_split[0]);
//        int month = Integer.parseInt(date_split[1]);
//        int day = Integer.parseInt(date_split[2]);
//        //메모 (선택사항)
        String memo = et_info_memo.getText().toString();

        return new String[] {name,date,category,memo};
    }
    //달력 표시를 위한 함수 1 https://stickode.tistory.com/224
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    //달력 표시를 위한 함수 2
    private void updateLabel() {
        et_info_date.setText(calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH));
    }
    public String readFile(String fileName){
        StringBuffer strBuffer = new StringBuffer();
        try{
            InputStream iStream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream));
            String line = "";
            while((line = bufferedReader.readLine()) != null)
                strBuffer.append(line + "\n");
            bufferedReader.close();
            iStream.close();
        }

        catch (IOException e){
            e.printStackTrace();
            return "";
        }
        return strBuffer.toString();
    }

    public void setNameText( String text ) {
        name = text;
    }
}

