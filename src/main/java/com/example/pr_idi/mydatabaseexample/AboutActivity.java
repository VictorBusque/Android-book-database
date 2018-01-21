package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClick(View v) {
        Intent i = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void onBackPressed() {
        Intent i = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(i);
    }
}
