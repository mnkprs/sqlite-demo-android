package com.example.manos.project21324;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manos on 11/12/2017.
 */

public class ResultsActivity extends Activity {

    DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        String selectedUserId = getIntent().getStringExtra("selectedUserId");
        String dateTimePassed = getIntent().getStringExtra("dateTime");
        TextView resultUserID = findViewById(R.id.resultUserID);
        resultUserID.setText(selectedUserId);
        this.db = new DatabaseHandler(this);

        ListView coordinatesListView =  findViewById(R.id.resultCoordsID);
        List<String> resultCoordsID;
        resultCoordsID = db.getCoordinates(selectedUserId, dateTimePassed);
        ArrayAdapter mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultCoordsID);
        coordinatesListView.setAdapter(mAdapter);
    }
}
