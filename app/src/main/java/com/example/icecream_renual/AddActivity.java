package com.example.icecream_renual;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AddActivity extends Activity {

    public EditText et_name;
    public EditText et_date;
    public EditText et_quantity;
    public EditText et_category;
    public EditText et_memo;

    public Button btn_save;
    public Button btn_cancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Func funfun = new Func();

        et_name = (EditText) findViewById(R.id.et_name);
        et_date = (EditText) findViewById(R.id.et_date);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_category = (EditText) findViewById(R.id.et_category);
        et_memo = (EditText) findViewById(R.id.et_memo);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //필수정보가 하나라도 없는 경우
                if((et_name.getText().toString().equals("") || et_name.getText().toString() == null) ||
                        (et_date.getText().toString().equals("") || et_date.getText().toString() == null) ||
                        (et_quantity.getText().toString().equals("") || et_quantity.getText().toString() == null) ||
                        (et_category.getText().toString().equals("") || et_category.getText().toString() == null)){
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
                    //수량
                    String quantity = et_quantity.getText().toString();
                    int quantity_int = Integer.parseInt(quantity);
                    //카테고리
                    String category = et_category.getText().toString();
                    //메모 (선택사항)
                    String memo = et_memo.getText().toString();

                    //파일 저장
                    funfun.writeFile(name + ".txt",name + "|" + year + "|" + month + "|" + day + "|" + quantity + "|" + category + "|" + memo);

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