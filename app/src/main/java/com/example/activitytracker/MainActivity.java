package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView limitTextView, latestAdditionView;
    int dailyGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dailyGoal = 1000;                                       //TODO get from database
        TextView tv = findViewById(R.id.caloriesAmount);
        tv.setText(String.valueOf(dailyGoal));
        limitTextView = (TextView) findViewById(R.id.calorieWarning);
        limitTextView.setVisibility(View.INVISIBLE);

        latestAdditionView = (TextView) findViewById(R.id.recentCalorieAddition);
        latestAdditionView.setVisibility(View.INVISIBLE);
    }

    public void calorieAddButton(View view){
        EditText editText = (EditText) findViewById(R.id.numberInput);
        String message = editText.getText().toString();
        Toast.makeText(this, message + " calories added.", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.caloriesAmount);
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