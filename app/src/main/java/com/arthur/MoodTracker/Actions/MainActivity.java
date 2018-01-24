package com.arthur.MoodTracker.Actions;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;

import com.arthur.MoodTracker.MoodModel.Preference;
import com.arthur.MoodTracker.VerticalView.VerticalViewPager;
import com.arthur.MoodTracker.R;
import com.arthur.MoodTracker.VerticalView.SwipeAdapter;
import com.arthur.MoodTracker.MoodModel.Manage;
import com.arthur.MoodTracker.MoodModel.Mood;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeAdapter mAdapter;
    private ImageButton mAddNote, mHistory;
    private int mCurrentMood;
    private List<Mood> moodLog = new ArrayList<>();
    private String mCurrentMoodNote, mNote, mCurrentDay, mCurrentMoodDay;
    private File mFolder, moodFile;
    private Mood newMood;
    private VerticalViewPager mViewPager;
    private EditText mEditText;
    private Preference userPref;
    private Manage serial;

    public static final String BUNDLE_STATE_MOOD = "usersMood";
    public static final String BUNDLE_STATE_NOTE = "usersNote";
    public static final String BUNDLE_STATE_DAY = "MoodsDay";


            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);
                mAdapter = new SwipeAdapter(this);
                mViewPager.setAdapter(mAdapter);

                //check the previous saved mood
                mCurrentDay = new LocalDate().toString();

                //load last mood selected
                userPref = new Preference(this);
                if(savedInstanceState != null) {
                //stop other moods
                    mCurrentMood = savedInstanceState.getInt(BUNDLE_STATE_MOOD);
                    mCurrentMoodNote = savedInstanceState.getString(BUNDLE_STATE_NOTE);
                    mCurrentMoodDay = savedInstanceState.getString(BUNDLE_STATE_DAY);
                } else {
                    mCurrentMood = userPref.getMoodPref();
                    mCurrentMoodNote = userPref.getNotePref();
                    mCurrentMoodDay = userPref.getDayPref();
                }


        System.out.println("CurrentMoodDay: "+mCurrentMoodDay +" CurrentDay: "+mCurrentDay);
        //save the mood of the previous day
        LocalDate sDate = new LocalDate(LocalDate.parse(mCurrentMoodDay));
        LocalDate cDate = new LocalDate(LocalDate.parse(mCurrentDay));
        Period period = new Period(sDate, cDate);
        int deltaDays = period.toStandardDays().getDays();
        if (deltaDays != 0){
            mFolder = new File(getFilesDir() + "/mood");
            if (!mFolder.exists()){
                mFolder.mkdir();
            }
            moodFile = new File(mFolder.getAbsolutePath() + "/moodLog1.dat");
            serial = new Manage();
            moodLog = serial.deserialize(moodFile);

            newMood = new Mood(mCurrentMood, deltaDays, mCurrentMoodNote);
            serial.serialize(moodLog, newMood, moodFile);

            mCurrentMood = 2;
            mCurrentMoodNote = null;
            mCurrentMoodDay = mCurrentDay;

        }
                mViewPager.setCurrentItem(mCurrentMood);
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

        //Add a new content in the app MoodTracker : sounds when you change your mood w/MediaPlayer
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        System.out.println(position);
                        int ind = mViewPager.getCurrentItem();
                        MediaPlayer mp;
                        switch (ind){
                            case 0: mp = MediaPlayer.create(MainActivity.this, R.raw.sound04);
                                break;
                            case 1: mp = MediaPlayer.create(MainActivity.this, R.raw.sound01);
                                break;
                            case 2: mp = MediaPlayer.create(MainActivity.this, R.raw.sound02);
                                break;
                            case 3: mp = MediaPlayer.create(MainActivity.this, R.raw.sound00);
                                break;
                            case 4: mp = MediaPlayer.create(MainActivity.this, R.raw.sound03);
                                break;
                            default: mp = MediaPlayer.create(MainActivity.this, R.raw.sound02);
                                break;
                        }
                        mp.start();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

        mAddNote =(ImageButton)findViewById(R.id.add_note);
        mAddNote.setOnClickListener(new View.OnClickListener() {
        //display a window to insert a comment when the user use the button "note"
            @Override public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.note_layout, null);
            mEditText = (EditText) mView.findViewById(R.id.note_text);
            builder.setView(mView);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
               //confirm the notes has been saved + displays its contents
                    mNote = mEditText.getText().toString();
                    if(!mNote.isEmpty()){
                        Toast.makeText(MainActivity.this,
                                "Enregistrée",
                                Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    });

        mHistory = (ImageButton)findViewById(R.id.history_display);
            mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the "history button" open the HistoryActivity
                Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    //save the current data
        outState.putInt(BUNDLE_STATE_MOOD, mCurrentMood);
        outState.putString(BUNDLE_STATE_NOTE, mCurrentMoodNote);
        outState.putString(BUNDLE_STATE_DAY, mCurrentMoodDay);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        //when the app is closed, save the mood of the day (moodItem)
        //save the date and save the note
        int moodItem = mViewPager.getCurrentItem();
        userPref.setMoodPref(moodItem);
        String date = new LocalDate().toString();
        userPref.setDayPref(date);
        userPref.setNotePref(mNote);
        super.onStop();
    }
}
