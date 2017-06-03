package com.example.android.rgb_with_silder;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
        final SeekBar redSeekBar = (SeekBar) findViewById(R.id.red_seekbar);
        redSeekBar.setMax(255);
        final SeekBar greenSeekBar = (SeekBar) findViewById(R.id.green_seekbar);
        greenSeekBar.setMax(255);
        final SeekBar blueSeekBar = (SeekBar) findViewById(R.id.blue_seekbar);
        blueSeekBar.setMax(255);
        SharedPreferences prefs = getSharedPreferences(my_pref, 0);
        r = prefs.getInt("r", 0);
        g = prefs.getInt("g", 0);
        b = prefs.getInt("b", 0);
        redSeekBar.setProgress(r);
        greenSeekBar.setProgress(g);
        blueSeekBar.setProgress(b);
        changeBackgroundColor(r, g, b);
        ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
        int color = colorDrawable.getColor();
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

        redSeekBar.setProgress(r);

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                changeBackgroundColor(r,g,b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekBar.setProgress(g);

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                changeBackgroundColor(r,g,b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setProgress(b);

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                changeBackgroundColor(r,g,b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=g=b=0;
                changeBackgroundColor(r,g,b);
                redSeekBar.setProgress(r);
                greenSeekBar.setProgress(g);
                blueSeekBar.setProgress(b);
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