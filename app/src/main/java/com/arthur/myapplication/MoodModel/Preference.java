package com.arthur.myapplication.MoodModel;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.LocalDate;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    public static final String MOOD_OF_THE_DAY = "mood_of_the_day";
    public static final String MOOD_NOTE = "mood_note";
    public static final String MOOD_DAY = "mood_day";

    private Context mContext;

    public Preference(Context context){
        this.mContext=context;
    }
    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences("user_prefs", MODE_PRIVATE);
    }

    public int getMoodPref(){
        int mood = getPreferences().getInt(MOOD_OF_THE_DAY, 2);

        return mood;
    }

    public String getNotePref(){
        String note = getPreferences().getString(MOOD_NOTE, null);

        return note;
    }

    public String getDayPref(){
        String currentDay = new LocalDate().toString();
        String day = getPreferences().getString(MOOD_DAY, currentDay);

        return day;
    }

    public void setMoodPref(int item){
        getPreferences().edit().putInt(MOOD_OF_THE_DAY, item).apply();
    }

    public void setDayPref(String day){
        getPreferences().edit().putString(MOOD_DAY, day).apply();
    }

    public void setNotePref(String note){
        getPreferences().edit().putString(MOOD_NOTE, note).apply();
    }

    private void updatePrefs(SharedPreferences preferences) {
    }
}
