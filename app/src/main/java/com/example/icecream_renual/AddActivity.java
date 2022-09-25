package com.example.icecream_renual;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class AddActivity extends Activity {

    public EditText et_name;
    public EditText et_date;
    public EditText et_quantity;
    public EditText et_memo;

    public String category;
    public Spinner s_category;

    public Button btn_save;
    public Button btn_cancel;
    private Button btn_calendar;

//    private DatePickerDialog.OnDateSetListener callbackMethod;

    Func fun = new Func();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_name = (EditText) findViewById(R.id.et_search);
        et_date = (EditText) findViewById(R.id.et_date);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_memo = (EditText) findViewById(R.id.et_memo);


        s_category = (Spinner)findViewById(R.id.s_category);
        s_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //필수정보가 하나라도 없는 경우
                if((et_name.getText().toString().equals("") || et_name.getText().toString() == null) ||
                        (et_date.getText().toString().equals("") || et_date.getText().toString() == null) ||
                        (category.equals("") || category == null)){
                    Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_LONG).show();
                }
                // 모든 필수 정보가 다 입력된 경우 Edit Text로 받은 정보 각 형식에 맞게 변환
                else{
                    // 이름
                    String name = et_name.getText().toString();
                    //유통기한 (년 | 월 | 일 로 나눠서 저장)
                    String date = et_date.getText().toString();
                    String[] date_split = date.split("\\."); //YYYY.MM.DD 형식을 YYYY MM DD 로 나누기
                    int year = Integer.parseInt(date_split[0]);
                    int month = Integer.parseInt(date_split[1]);
                    int day = Integer.parseInt(date_split[2]);
                    //메모 (선택사항)
                    String memo = et_memo.getText().toString();

                    //파일 저장
                    writeFile(name + ".txt",name + "|" + year + "|" + month + "|" + day + "|" + category + "|" + memo);

                    //종료
                    finish();
                }
            }
        });
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_calendar = (Button) findViewById(R.id.btn_date_option);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, datepicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

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
        et_date.setText(calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH));
    }

    //파일 쓰기 함수
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
}