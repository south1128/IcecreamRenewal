package com.example.icecream_renual;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class item_info extends Activity {

    private static String name;
    private TextView tv_info_name;
    private TextView tv_info_date;
    private TextView tv_info_category;
    private TextView tv_info_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        tv_info_name = (TextView) findViewById(R.id.tv_info_name);
        tv_info_date = (TextView) findViewById(R.id.tv_info_date);
        tv_info_category = (TextView) findViewById(R.id.tv_info_category);
        tv_info_quantity = (TextView) findViewById(R.id.tv_info_quantity);

        String path = "/data/data/com.example.icecream_renual/files/";

        String rFile = readFile(path + name + ".txt");
        //읽어온 파일 나누기
        String[] txt_split = rFile.split("\\|");
        String itemName = txt_split[0];
        int itemYear = Integer.parseInt(txt_split[1]);
        int itemMonth = Integer.parseInt(txt_split[2]);
        int itemDay = Integer.parseInt(txt_split[3]);
        String itemCategory = txt_split[5];

        tv_info_name.setText(itemName);
        tv_info_date.setText(itemYear + "년" + itemMonth + "월" + itemDay + "일");
        tv_info_category.setText(itemCategory);
        tv_info_quantity.setText(txt_split[4]);

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