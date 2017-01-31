package com.test.catalog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class Apps extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.entrance,R.anim.exit)
                .replace(R.id.container,new GridApps()).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof GridApps){
            Apps.this.finish();
        } else if (fragment instanceof Details){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.in_right,R.anim.out_right)
                    .replace(R.id.container,new GridApps()).commitAllowingStateLoss();
        }

    }
}
