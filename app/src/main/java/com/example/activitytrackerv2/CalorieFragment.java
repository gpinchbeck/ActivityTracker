package com.example.activitytrackerv2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalorieFragment extends Fragment {
    View v;
    TextView limitTextView, latestAdditionView;
    Button btn;
    int dailyGoal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_calorie, container, false);

        dailyGoal = 1000;                                       //TODO get from database
        TextView tv = v.findViewById(R.id.caloriesAmount);
        tv.setText(String.valueOf(dailyGoal));
        limitTextView = (TextView) v.findViewById(R.id.calorieWarning);
        limitTextView.setVisibility(View.INVISIBLE);

        btn = (Button) v.findViewById(R.id.calorieAddButton);
        btn.setOnClickListener(this::calorieAddButton);

        latestAdditionView = (TextView) v.findViewById(R.id.recentCalorieAddition);
        latestAdditionView.setVisibility(View.INVISIBLE);

        return v;
    }

    public void calorieAddButton(View view){

        EditText editText = (EditText) v.findViewById(R.id.numberInput);
        String message = editText.getText().toString();
        Toast.makeText(getActivity(), message + " calories added.", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) v.findViewById(R.id.caloriesAmount);
        int amount = Integer.parseInt(textView.getText().toString());
        amount -= Integer.parseInt(message);
        textView.setText(String.valueOf(amount));

        if (amount <= 0){
            limitTextView.setVisibility(View.VISIBLE);
            textView.setTextColor(Color.parseColor("#970000"));
        }
        else if (amount <= dailyGoal/4){
            textView.setTextColor(Color.parseColor("#db9914"));
        }

        latestAdditionView.setText("Last addition: +" + message);
        latestAdditionView.setVisibility(View.VISIBLE);
    }
}
