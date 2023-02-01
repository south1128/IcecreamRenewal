package com.icecream.icecream_renual;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Func extends Activity{

    //파일 읽기 함수
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
