package com.example.android.rgb_hackermode;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String my_pref = "My Prefs File";
    int r, g, b;
    LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (LinearLayout) findViewById(R.id.background);
        SharedPreferences prefs = getSharedPreferences(my_pref, 0);
        r = prefs.getInt("r", 0);
        g = prefs.getInt("g", 0);
        b = prefs.getInt("b", 0);
        changeBackgroundColor(r, g, b);
        ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
        int color = colorDrawable.getColor();
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);
        Button redButton = (Button) findViewById(R.id.red_button);
        redButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (r >= 255) r = 0;
                                             else r += 5;
                                             changeBackgroundColor(r, g, b);
                                         }
                                     }
        );
        Button greenButton = (Button) findViewById(R.id.green_button);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g >= 255) g = 0;
                else g += 5;
                changeBackgroundColor(r, g, b);
            }
        });
        Button blueButton = (Button) findViewById(R.id.blue_button);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b >= 255) b = 0;
                else b += 5;
                changeBackgroundColor(r, g, b);
            }
        });
        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r = g = b = 0;
                changeBackgroundColor(r, g, b);
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(my_pref, 0).edit();
        editor.putInt("r", r);
        editor.putInt("g", g);
        editor.putInt("b", b);
        editor.commit();

    }

    void changeBackgroundColor(int r, int g, int b) {
        view.setBackgroundColor(Color.rgb(r, g, b));
        TextView textView = (TextView) findViewById(R.id.red_value);
        textView.setText(String.valueOf(r));
        TextView textView1 = (TextView) findViewById(R.id.green_value);
        textView1.setText(String.valueOf(g));
        TextView textView2 = (TextView) findViewById(R.id.blue_value);
        textView2.setText(String.valueOf(b));
    }
}