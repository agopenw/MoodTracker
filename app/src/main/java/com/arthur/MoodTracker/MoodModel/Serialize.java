package com.arthur.MoodTracker.MoodModel;

import java.io.Serializable;
import java.util.Comparator;

//Create a Serializable class
public class Serialize implements Serializable {
    private String note;
    private int date, mood;

    private static final long serialVersionUID = 1L;

    public Serialize(){
    }

    public Serialize(int mood, int date, String note){
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
        return "Serialize position: "+mood+", day of the year: "+date+" note: "+note;
    }
    //compare the dates previously saved
    public static Comparator<Serialize> moodDayComparator = new Comparator<Serialize>() {
        @Override
        public int compare(Serialize o1, Serialize o2) {
            int moodDay1 = o1.getDate();
            int moodDay2 = o2.getDate();

            return moodDay1 - moodDay2;
        }
    };

    public static void fromString(String moodString) {
    }
}
