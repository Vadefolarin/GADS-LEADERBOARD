package com.adedayo.gadsleaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.adedayo.gadsleaderboard.adapter.FragmentAdapter;
import com.adedayo.gadsleaderboard.ui.IqFragment;
import com.adedayo.gadsleaderboard.ui.LearningFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;
    ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tabs);

        mFragments = new ArrayList<>();

        mFragments.add(new LearningFragment());
        mFragments.add(new IqFragment());

        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getApplicationContext(), mFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText("Learning Leaders");
        mTabLayout.getTabAt(1).setText("Skill IQ Leaders");

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProjectSubmissionActivity.class);
            startActivity(intent);
            finish();
        });

    }
}