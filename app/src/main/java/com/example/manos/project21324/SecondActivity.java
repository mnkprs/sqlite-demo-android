package com.example.manos.project21324;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manos on 11/12/2017.
 */

public class SecondActivity extends Activity {
    String spinnerValue;
    EditText userIdField;
    DatabaseHandler db;
    Spinner spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2nd);

        final Button resultsActivityButton = findViewById(R.id.resultsActivityButtonID);
        resultsActivityButton.setOnClickListener(resultsActivityListener);
        resultsActivityButton.setEnabled(false);
        this.db = new DatabaseHandler(this);
        userIdField = findViewById(R.id.userSearchID);

        List<String> list = db.getAllDateTimes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.dtSpinnerID);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                 spinnerValue = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        userIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    resultsActivityButton.setEnabled(true);
                } else {
                    resultsActivityButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    View.OnClickListener resultsActivityListener = new View.OnClickListener() {
        public void onClick(View view) {
            String userIdValue = userIdField.getText().toString();
            Intent intent = new Intent( SecondActivity.this, ResultsActivity.class);
            intent.putExtra("selectedUserId", userIdValue);
            intent.putExtra("dateTime", spinnerValue);
            startActivity(intent);

        }
    };

}
