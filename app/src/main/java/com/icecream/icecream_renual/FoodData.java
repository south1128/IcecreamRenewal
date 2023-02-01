package com.icecream.icecream_renual;

//DDay 계산
import java.util.Calendar;

public class FoodData {
    private String emoji;
    private String foodname;
    private String category;
    private String dday;
    //현재 날짜
    private int n_year;
    private int n_month;
    private int n_day;
    //유통기한
    private int e_year;
    private int e_month;
    private int e_day;
    //남은 날짜 (단위 : 일)
    private int remaindays;
    private int resultdays;

    long today, expiryday;

    public FoodData(){
    }
    public FoodData(String emoji, String foodname, String category, int e_year, int e_month, int e_day){
        this.emoji = emoji;

        this.foodname = foodname;
        this.category = category;
        this.e_year = e_year;
        this.e_month = e_month;
        this.e_day = e_day;
    }
    public String getEmoji(){return emoji;}
    public String getFoodName(){return foodname;}
    //DDay 계산 1
    public String getDday(){
        resultdays = calculateDday(this.e_year, this.e_month, this.e_day);

        if(resultdays > 0){
            dday = String.format("D-%d", resultdays);
        }
        else if(resultdays < 0){
            dday = String.format("D+%d",Math.abs(resultdays));
        }
        else{
            dday = "D_Day!";
        }
        return dday;
    }
    //DDay 계산 2
    public int calculateDday(int e_year, int e_month, int e_day){

        // today date
        Calendar calendar = Calendar.getInstance();
        n_year = calendar.get(Calendar.YEAR);
        n_month = calendar.get(Calendar.MONTH);
        n_day = calendar.get(Calendar.DAY_OF_MONTH);

        today = calendar.getTimeInMillis();

        Calendar e_Calendar = Calendar.getInstance();
        e_Calendar.set(e_year,e_month-1,e_day);
        expiryday = e_Calendar.getTimeInMillis();

        long result = (expiryday-today)/(24*60*60*1000);
        remaindays = (int)result;

        return remaindays;

    }

}
