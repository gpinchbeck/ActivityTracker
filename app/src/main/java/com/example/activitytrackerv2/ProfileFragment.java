package com.example.activitytrackerv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileFragment extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container,false);

        final Button button = v.findViewById(R.id.edit_profile_btn);

        button.setOnClickListener(v -> {

            // Ask the user if they want to edit their profile details through a dialog.

            AlertDialog.Builder userQuery = new AlertDialog.Builder(getContext());

            userQuery.setTitle("Modify Profile");
            userQuery.setMessage("Are you sure you want to change your details?");
            userQuery.setPositiveButton("Yes", (dialog, which) -> {

                Intent intent;

                intent = new Intent(getContext(), EditProfile.class);
                v.getContext().startActivity(intent);

            });

            userQuery.setNegativeButton("No", (dialog, which)
                    -> Toast.makeText(getContext(),
                    "No Changes were Made.", Toast.LENGTH_SHORT).show());

            AlertDialog dialog = userQuery.create();
            dialog.show();

        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        try {

            FileInputStream inputStream = getActivity().openFileInput("userData.csv");
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

                TextView textToChange = v.findViewById(R.id.user_fn);
                textToChange.setText(firstName);

                textToChange = v.findViewById(R.id.user_ln);
                textToChange.setText(lastName);

                textToChange = v.findViewById(R.id.user_email);
                textToChange.setText(emailAddress);

                textToChange = v.findViewById(R.id.user_weight);
                textToChange.setText(weight + "lb");

                textToChange = v.findViewById(R.id.user_plan);
                textToChange.setText(plan);

                textToChange = v.findViewById(R.id.user_target);
                textToChange.setText(target);

            }

        }

        catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        catch (IOException e) {

            e.printStackTrace();

        }

    }
}
