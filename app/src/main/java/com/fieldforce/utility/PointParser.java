package com.fieldforce.utility;

import android.content.Context;
import android.os.AsyncTask;

import com.fieldforce.interfaces.TaskLoadedCallback;

import java.util.HashMap;
import java.util.List;

public class PointParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskLoadedCallback;
    String directionMode ="driving";

    public PointParser(Context context, String directionMode){
        this.taskLoadedCallback = (TaskLoadedCallback) context;
        this.directionMode = directionMode;
    }
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
        return null;
    }
}
