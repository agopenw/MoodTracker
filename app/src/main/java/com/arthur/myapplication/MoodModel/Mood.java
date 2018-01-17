package com.arthur.myapplication.MoodModel;

import java.io.Serializable;
import java.util.Comparator;

public class Mood implements Serializable {
    private String note;
    private int date, mood;

    private static final long serialVersionUID = 1L;

    public Mood(){
    }

    public Mood(int mood, int date, String note){
        this.mood = mood;
        this.date = date;
        this.note = note;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public int getMood() {
        return mood;
    }

    @Override
    public String toString() {
        return "Mood position: "+mood+", day of the year: "+date+" note: "+note;
    }

    public static Comparator<Mood> moodDayComparator = new Comparator<Mood>() {
        @Override
        public int compare(Mood o1, Mood o2) {
            int moodDay1 = o1.getDate();
            int moodDay2 = o2.getDate();

            return moodDay1 - moodDay2;
        }
    };

    public static void fromString(String moodString) {
    }
}
