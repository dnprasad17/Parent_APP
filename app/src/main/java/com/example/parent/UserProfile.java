package com.example.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    public TextInputLayout edtTxtFatherName, edtTxtMotherName, edtTxtStudentName;
    private MaterialSearchBar edtTxtSchoolName;
    TextView days;
    boolean[] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String[] dayArray={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    public Button btnProceed;
    public Spinner standards;
    public TimePicker timePickerStart, timePickerEnd;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button Address;
    int addressClick=0;
    private PlacesClient placesClint;
    private List<AutocompletePrediction> predictionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Hooks
        edtTxtFatherName = findViewById(R.id.edtTxtFatherName);
        edtTxtMotherName = findViewById(R.id.edtTxtMotherName);
        edtTxtStudentName = findViewById(R.id.edtTxtStudentName);
        edtTxtSchoolName = findViewById(R.id.edtTxtSchoolName);
        days = findViewById(R.id.daysTextView);



        btnProceed = findViewById(R.id.btnProceed);
        standards = findViewById(R.id.standards);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        Address=findViewById(R.id.Address);

        //PLACES API
        Places.initialize(UserProfile.this,"AIzaSyASLi8Im7CmaJIy-XkeTJiG2dhPK3h-ubk");
        placesClint = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        edtTxtSchoolName.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(),true,null,true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode == MaterialSearchBar.BUTTON_NAVIGATION){

                }else if(buttonCode == MaterialSearchBar.BUTTON_BACK){
                    edtTxtSchoolName.closeSearch();
                }
            }
        });


        edtTxtSchoolName.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("in")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery(charSequence.toString())
                        .build();
                placesClint.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if(task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if(predictionsResponse != null){
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionList = new ArrayList<>();
                                for(int i=0; i<predictionList.size();i++){
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }
                                edtTxtSchoolName.updateLastSuggestions(suggestionList);
                                if(!edtTxtSchoolName.isSuggestionsVisible()){
                                    edtTxtSchoolName.showSuggestionsList();
                                }
                            }
                        }

                        else {
                            Log.i("mytask","prediction error");
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //initialize selected days
        selectedDay = new boolean[dayArray.length];

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initializing alert dialog
                AlertDialog.Builder builder= new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("Select Days");

                //set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        //chk condition
                        if(b){
                            //whn slctd add to daylist
                            dayList.add(i);
                            Collections.sort(dayList);
                        } else{
                            //whn unslctd remove from daylist
                            dayList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //initialize string builder
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int j=0;j<dayList.size();j++){
                            //concat array value
                            stringBuilder.append(dayArray[dayList.get(j)]);
                            //chk cnditn
                            if(j != dayList.size()-1){
                                stringBuilder.append(',');
                            }
                        }
                        //set text in textview
                        days.setText(stringBuilder.toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss builder
                        dialogInterface.dismiss();
                    }
                });
                //show dialog
                builder.show();
            }
        });


        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = getIntent().getStringExtra("mobile");
                addressClick++;
                Intent intent = new Intent(getApplicationContext(), Map.class);
                intent.putExtra("mobile", phone_number);
                startActivity(intent);
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDetailsToDatabase();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Standards, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        standards.setAdapter(adapter);
    }

    private void sendDetailsToDatabase() {
        //hooks
        String fN = edtTxtFatherName.getEditText().getText().toString().trim();
        String mN = edtTxtMotherName.getEditText().getText().toString().trim();
        String stN = edtTxtStudentName.getEditText().getText().toString().trim();
        String schN = edtTxtSchoolName.getSearchEditText().getText().toString().trim();
        String standard = standards.getSelectedItem().toString();
        String daysString= days.getText().toString();
        String phone_number = getIntent().getStringExtra("mobile");


        String address = getIntent().getStringExtra("Address");
        String local = getIntent().getStringExtra("Local");
        String stage = getIntent().getStringExtra("Stage");
        String landmark = getIntent().getStringExtra("Landmark");
        String coordinates = getIntent().getStringExtra("Coordinates");

        //Start and End Time
        int hourS = timePickerStart.getCurrentHour();
        int minuteS = timePickerStart.getCurrentMinute();
        StringBuilder timeS = new StringBuilder().append(hourS).append(" : ").append(minuteS);
        int hourE = timePickerEnd.getCurrentHour();
        int minuteE = timePickerEnd.getCurrentMinute();
        StringBuilder timeE = new StringBuilder().append(hourE).append(" : ").append(minuteE);
        String timeStart= timeS.toString();
        String timeEnd= timeE.toString();


        //checking no fields are ignored
        if(fN.isEmpty()){
            edtTxtFatherName.requestFocus();
            edtTxtFatherName.setError("Father Name Required");
        } else if(mN.isEmpty()) {
            edtTxtMotherName.requestFocus();
            edtTxtMotherName.setError("Mother Name Required");
        } else if(stN.isEmpty()){
            edtTxtStudentName.requestFocus();
            edtTxtStudentName.setError("Student Name Required");
        }else if(schN.isEmpty()){
            edtTxtSchoolName.requestFocus();
            Toast.makeText(this, "Please enter school name", Toast.LENGTH_SHORT).show();
//       }  else if(addressClick==0){
//            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        } else {

//            Toast.makeText(UserProfile.this, "phone="+phone_number, Toast.LENGTH_SHORT).show();


            userHelperClass user = new userHelperClass(fN, mN, stN, schN, standard, timeStart,
                    timeEnd, coordinates, address, local,stage,landmark,phone_number);

            database = FirebaseDatabase.getInstance();
            database.getReference("students")
                    .child(phone_number)
                    .setValue(user);


            //adding days to days field explicitly
            database.getReference("students")
                    .child(phone_number)
                    .child("days")
                    .setValue(daysString).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), WAITING.class);
                        Toast.makeText(UserProfile.this, "Details Updated", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            });

        }
    }
}
