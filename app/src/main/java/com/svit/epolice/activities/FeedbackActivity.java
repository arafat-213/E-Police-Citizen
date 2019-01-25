package com.svit.epolice.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.svit.epolice.Models.Feedback;
import com.svit.epolice.R;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> policeStationsArrayList;
    ArrayList<String> policemenArrayList;
    Spinner policeStationsSpinner, policeMenSpinner;
    ArrayAdapter policeStationsAdapter;
    ArrayAdapter policeMenArrayAdapter;
    ArrayList<String> ManjalpurPoliceMen;
    ArrayList<String> oldCityPoliceMen;
    CheckBox anonymousCB;
    FirebaseDatabase mDatabase;
    DatabaseReference mFeedbackRef;
    RatingBar policeRatingBar;
    EditText descriptionET;
    Button submitBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        init();

        mDatabase = FirebaseDatabase.getInstance();
        mFeedbackRef = mDatabase.getReference("feedback");


        policeStationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        policeMenArrayAdapter.clear();
                        policeMenArrayAdapter.addAll(oldCityPoliceMen);
                        policeMenArrayAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        policeMenArrayAdapter.clear();
                        policeMenArrayAdapter.addAll(ManjalpurPoliceMen);
                        policeMenArrayAdapter.notifyDataSetChanged();
                        break;
                    default:
                        policeMenArrayAdapter.clear();
                        policeMenArrayAdapter.addAll(policemenArrayList);
                        policeMenArrayAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void init() {
        policeStationsSpinner = findViewById(R.id.policeStationsSpinner);
        policeMenSpinner = findViewById(R.id.policeMenSpinner);
        anonymousCB = findViewById(R.id.anonymousCB);
        policeRatingBar = findViewById(R.id.policeRatingBar);
        descriptionET = findViewById(R.id.descriptionET);
        submitBTN = findViewById(R.id.submitBTN);
        submitBTN.setOnClickListener(this);
        policeStationsArrayList = new ArrayList<String>();
        policemenArrayList = new ArrayList<String>();
        policeStationsArrayList.add("Old City");
        policeStationsArrayList.add("Karelibag");
        policeStationsArrayList.add("Manjalpur");
        policeStationsArrayList.add("Gotri");
        policeStationsArrayList.add("Sayajigunj");

        policemenArrayList.add("Bajirao Singham");
        policemenArrayList.add("Jaykant sikhre");
        policemenArrayList.add("Constable Patil");
        policemenArrayList.add("Hawaldar Gokhle");


        ManjalpurPoliceMen = new ArrayList<>();
        ManjalpurPoliceMen.add("Policewala kaka");

        oldCityPoliceMen = new ArrayList<>();
        oldCityPoliceMen.add("S.P.Shinchan");
        oldCityPoliceMen.add("Officer Jenny");

        policeStationsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, policeStationsArrayList);
        policeStationsSpinner.setAdapter(policeStationsAdapter);

        policeMenArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, policemenArrayList);
        policeMenSpinner.setAdapter(policeMenArrayAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                String username = "anonymous";
                Feedback feedback;
                if (!anonymousCB.isChecked()) {
                    username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                }
                feedback = new Feedback(
                        policeStationsSpinner.getSelectedItem().toString(),
                        policeMenSpinner.getSelectedItem().toString(),
                        policeRatingBar.getRating(),
                        descriptionET.getText().toString(),
                        username
                );
                String key = mFeedbackRef.push().getKey();
                mFeedbackRef.child(key).setValue(feedback);
        }
    }
}
