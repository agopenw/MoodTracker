package com.arthur.myapplication.Controller;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;

import com.arthur.myapplication.Model.Preference;
import com.arthur.myapplication.View.VerticalViewPager;
import com.arthur.myapplication.R;
import com.arthur.myapplication.View.CustomSwipeAdapter;
import com.arthur.myapplication.Model.Serialize;
import com.arthur.myapplication.Model.Mood;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton mAddNote, mHistory;
    private CustomSwipeAdapter mAdapter;
    private EditText mEditText;
    private File mFolder, moodFile;
    private int mCurrentMood;
    private List<Mood> moodLog = new ArrayList<>();
    private Mood newMood;
    private ObjectInputStream mInputStream;
    private ObjectOutputStream mOutputStream;
    private String mCurrentMoodNote, mNote, mCurrentDay, mCurrentMoodDay;
    private VerticalViewPager mViewPager;
    private Serialize serial;
    private Preference userPref;

    public static final String BUNDLE_STATE_MOOD = "usersMood";
    public static final String BUNDLE_STATE_NOTE = "usersNote";
    public static final String BUNDLE_STATE_DAY = "MoodsDay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);
        mAdapter = new CustomSwipeAdapter(this);
        mViewPager.setAdapter(mAdapter);

        mCurrentDay = new LocalDate().toString();

        userPref = new Preference(this);
        if(savedInstanceState != null) {
            mCurrentMood = savedInstanceState.getInt(BUNDLE_STATE_MOOD);
            mCurrentMoodNote = savedInstanceState.getString(BUNDLE_STATE_NOTE);
            mCurrentMoodDay = savedInstanceState.getString(BUNDLE_STATE_DAY);
        } else {
            mCurrentMood = userPref.getMoodPref();
            mCurrentMoodNote = userPref.getNotePref();
            mCurrentMoodDay = userPref.getDayPref();
        }

        System.out.println("CurrentMoodDay: "+mCurrentMoodDay +" CurrentDay: "+mCurrentDay);

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
            serial = new Serialize();
            moodLog = serial.deserialize(moodFile);

            newMood = new Mood(mCurrentMood, deltaDays, mCurrentMoodNote);
            serial.serialize(moodLog, newMood, moodFile);

            mCurrentMood = 2;
            mCurrentMoodNote = null;
            mCurrentMoodDay = mCurrentDay;

        }

        mAddNote =(ImageButton)findViewById(R.id.add_note);
        mAddNote.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.note_layout, null);
            mEditText = (EditText) mView.findViewById(R.id.note_text);
            builder.setView(mView);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        };
    });

        mHistory = (ImageButton)findViewById(R.id.history_display);
            mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(BUNDLE_STATE_MOOD, mCurrentMood);
        outState.putString(BUNDLE_STATE_NOTE, mCurrentMoodNote);
        outState.putString(BUNDLE_STATE_DAY, mCurrentMoodDay);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        int moodItem = mViewPager.getCurrentItem();
        userPref.setMoodPref(moodItem);
        String date = new LocalDate().toString();
        userPref.setDayPref(date);
        userPref.setNotePref(mNote);
        super.onStop();
    }
}
