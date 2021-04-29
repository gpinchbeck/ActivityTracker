package com.example.activitytrackerv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        // Load User Data

        loadData();

        // Button Functionality.

        final Button returnButton = findViewById(R.id.return_btn);
        final Button submitButton = findViewById(R.id.submit_btn);

        returnButton.setOnClickListener(v -> {

            // Ask the user if they want to edit their profile details through a dialog.

            AlertDialog.Builder userQuery = new AlertDialog.Builder(EditProfile.this);

            userQuery.setTitle("Ignore Changes");
            userQuery.setMessage("Are you sure you want to leave and cancel your changes?");
            userQuery.setPositiveButton("Yes", (dialog, which) -> {

                Intent intent;

                intent = new Intent(EditProfile.this, MainActivity.class);
                EditProfile.this.startActivity(intent);

                Toast.makeText(getApplicationContext(), "No Changes were Implemented.",
                        Toast.LENGTH_SHORT).show();

            });

            userQuery.setNegativeButton("No", (dialog, which)
                    -> Toast.makeText(getApplicationContext(),
                    "No worries!", Toast.LENGTH_SHORT).show());

            AlertDialog dialog = userQuery.create();
            dialog.show();

        });

        submitButton.setOnClickListener(v -> {

            // Ask the user if they want to submit their new profile details through a dialog.

            AlertDialog.Builder userQuery = new AlertDialog.Builder(EditProfile.this);

            userQuery.setTitle("Submit Details");
            userQuery.setMessage("Are you sure you want to submit your changes?");
            userQuery.setPositiveButton("Yes", (dialog, which) -> {

               if (validateData()) {

                   // If the data is valid, submit it.

                   String fileContents = obtainData();

                   FileOutputStream fos = null;

                   try {

                       fos = openFileOutput("userData.csv", MODE_PRIVATE);
                       fos.write(fileContents.getBytes());
                       fos.close();

                   }

                   catch (FileNotFoundException e) {

                       e.printStackTrace();

                   }

                   catch (IOException e) {

                       e.printStackTrace();

                   }

                   // After data has been sent, return to original activity
                   finish();

               }

            });

            userQuery.setNegativeButton("No", (dialog, which)
                    -> Toast.makeText(getApplicationContext(),
                    "No worries!", Toast.LENGTH_SHORT).show());

            AlertDialog dialog = userQuery.create();
            dialog.show();

        });

    }

    private void loadData() {

        try {

            FileInputStream inputStream = openFileInput("userData.csv");
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                String[] userData = line.split(",");

                String firstName = userData[0];
                String lastName = userData[1];
                String emailAddress = userData[2];
                String weight = userData[3];
                String plan = userData[4];
                String target = userData[5];

                TextInputLayout textToChange = findViewById(R.id.textInputLayout_1);

                textToChange.getEditText().setText(firstName);

                textToChange = findViewById(R.id.textInputLayout_2);
                textToChange.getEditText().setText(lastName);

                textToChange = findViewById(R.id.textInputLayout_3);
                textToChange.getEditText().setText(emailAddress);

                textToChange = findViewById(R.id.textInputLayout_4);
                textToChange.getEditText().setText((weight));

                textToChange = findViewById(R.id.textInputLayout_5);
                textToChange.getEditText().setText(target);

                if (plan.equals("Standard")) {

                    RadioButton radioButton = (RadioButton) findViewById(R.id.standard_button);
                    radioButton.setChecked(true);

                }

                if (plan.equals("Moderate")) {

                    RadioButton radioButton = (RadioButton) findViewById(R.id.moderate_button);
                    radioButton.setChecked(true);

                }

                if (plan.equals("Intense")) {

                    RadioButton radioButton = (RadioButton) findViewById(R.id.intense_button);
                    radioButton.setChecked(true);

                }

            }

        }

        catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        catch (IOException e) {

            e.printStackTrace();

        }

    }

    private String obtainData() {

        String firstName, lastName, emailAddress, weight, selectedPlan, target, data;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.planButtonGroup);

        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout_1);
        firstName = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_2);
        lastName = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_3);
        emailAddress = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_4);
        weight = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_5);
        target = textInputLayout.getEditText().getText().toString();

        int buttonID = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) findViewById(buttonID);
        selectedPlan = radioButton.getText().toString();

        data = firstName + "," + lastName + "," + emailAddress + "," + weight + "," + selectedPlan +
                "," + target;

        return data;

    }

    private boolean validateData() {

        String firstName, lastName, emailAddress, weight, selectedPlan, target;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.planButtonGroup);

        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout_1);
        firstName = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_2);
        lastName = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_3);
        emailAddress = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_4);
        weight = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.textInputLayout_5);
        target = textInputLayout.getEditText().getText().toString();

        int buttonID = radioGroup.getCheckedRadioButtonId();

        if (buttonID != -1) {

            RadioButton radioButton = (RadioButton) findViewById(buttonID);
            selectedPlan = radioButton.getText().toString();

        }

        if ((firstName.isEmpty()) | (lastName.isEmpty()) | (emailAddress.isEmpty()) |
                (weight.isEmpty()) | (target.isEmpty()) | (buttonID == -1)) {

            Toast.makeText(EditProfile.this,
                    "Data Error: Don't leave properties blank.",
                    Toast.LENGTH_SHORT).show();

            return false;

        }

        if ((!firstName.isEmpty()) | (!lastName.isEmpty()) | (!emailAddress.isEmpty())) {

            if ((firstName.contains(" ")) | (lastName.contains(" ")) | (emailAddress.contains(" "))) {

                Toast.makeText(EditProfile.this,
                        "Data Error: Do not include spaces in your personal information",
                        Toast.LENGTH_SHORT).show();

                return false;

            }

        }

        return true;

    }

}