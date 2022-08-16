package com.example.icecream_renual;

public class ItemData {

    String name;
    String category;
    int buyYear;
    int buyMonth;
    int buyDay;
    int resId;

    public ItemData(String name, String category, int buyYear, int buyMonth, int buyDay, int resId){
        this.name = name;
        this.category = category;
        this.buyYear = buyYear;
        this.buyMonth = buyMonth;
        this.buyDay = buyDay;
        this.resId = resId;
    }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getCategory(){ return category; }

    public void setCategory(String category){ this.category = category; }

    public int getBuyYear(){ return buyYear; }

    public void setBuyYear(int buyYear){ this.buyYear = buyYear; }

    public int getBuyMonth(){ return buyMonth; }

    public void setBuyMonth(int buyMonth){ this.buyMonth = buyMonth; }

    public int getBuyDay(){ return buyDay; }

    public void setBuyDay(int buyDay){ this.buyDay = buyDay; }

    public int getResId(){ return resId; }

    public void setResId(int resId){ this.resId = resId; }
}
