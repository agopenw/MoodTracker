package com.arthur.MoodTracker.MoodModel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.util.Collections;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;

import static com.arthur.MoodTracker.MoodModel.Serialize.moodDayComparator;

public class Manage {
    private FileInputStream fis;
    private FileOutputStream fos;
    private ObjectOutputStream mOutputStream;
    private ObjectInputStream mInputStream;
    private List<Serialize> moodLog;

    public void Serialize(){};

    public void serialize (List<Serialize> moodLog, Serialize md, File moodFile) {
        try{
            FileOutputStream fos = new FileOutputStream(moodFile);
            mOutputStream = new ObjectOutputStream(fos);

            for (Iterator<Serialize> it = moodLog.iterator(); it.hasNext(); ){
                Serialize nextMd = it.next();
                int newDate = nextMd.getDate() + md.getDate();
                nextMd.setDate(newDate);
            }

            while (moodLog.size() > 6){
                moodLog.remove(0);
            }

            moodLog.add(md);
            Collections.sort(moodLog, moodDayComparator);
            System.out.println(moodLog.toString());

            mOutputStream.writeObject(moodLog);
            mOutputStream.flush();
            mOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                mOutputStream.flush();
                mOutputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public List<Serialize> deserialize(File moodFile){
        if(moodFile.exists()){
            try{
                fis = new FileInputStream(moodFile);
                mInputStream = new ObjectInputStream(fis);

                moodLog = (ArrayList<Serialize>) mInputStream.readObject();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        } else {
            try{
                moodFile.createNewFile();

                fos = new FileOutputStream(moodFile);
                mOutputStream = new ObjectOutputStream(fos);

                moodLog = new ArrayList<>();

                mOutputStream.writeObject(moodLog);
                mOutputStream.flush();
                mOutputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return moodLog;
    }
}
