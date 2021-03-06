package com.arthur.MoodTracker.Actions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arthur.MoodTracker.MoodModel.Serialize;
import com.arthur.MoodTracker.VerticalView.Adapter;
import com.arthur.MoodTracker.R;
import com.arthur.MoodTracker.MoodModel.Manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class HistoryActivity extends AppCompatActivity {

    private File mFolder, moodFile;
    private FileInputStream fis;
    private FileOutputStream fos;
    private List<Serialize> moodLog;
    private ObjectInputStream mInputStream;
    private ObjectOutputStream mOutputStream;
    private ListAdapter mAdapter;
    private ListView mListView;
    private Manage ser;
    private TextView mTextView;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //Moodlog
        mFolder = new File(getFilesDir() + "/mood");
        if (!mFolder.exists()){
            mFolder.mkdir();
        }
        moodFile = new File(mFolder.getAbsolutePath() + "/moodLog1.dat");
        ser = new Manage();
        moodLog = ser.deserialize(moodFile);
        //Check if the moodlog is empty
        if(!moodLog.isEmpty()){
            mAdapter = new Adapter(this, moodLog);
            mListView = (ListView)findViewById(R.id.history_listview);
            mListView.setAdapter(mAdapter);
        } else {
            //If the Moodlog is empty, display a message
            mTextView = (TextView)findViewById(R.id.empty_list);
            mTextView.setVisibility(View.VISIBLE);
        }
    }
}
