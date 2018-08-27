package com.learnera.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.learnera.app.R;

/**
 * Created by Prejith on 7/13/2016.
 */

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        RelativeLayout loginrl = (RelativeLayout) findViewById(R.id.login_root_layout);
//        loginrl.setBackground(getResources().getDrawable(R.color.md_red_700));

        setFullscreen(true);
        super.onCreate(savedInstanceState);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
                .fragment(R.layout.fragment_intro_welcome, R.style.Theme_Intro)
                .canGoBackward(false)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
//                .background(R.color.md_green_500)
//                .backgroundDark(R.color.md_green_800)
                .fragment(R.layout.fragment_intro_syllabus, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
//                .background(R.color.md_orange_500)
//                .backgroundDark(R.color.md_orange_800)
                .fragment(R.layout.fragment_intro_announcement, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
//                .background(R.color.md_light_green_500)
//                .backgroundDark(R.color.md_light_green_800)
                .fragment(R.layout.fragment_intro_marks, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
//                .background(R.color.md_pink_500)
//                .backgroundDark(R.color.md_pink_800)
                .fragment(R.layout.fragment_intro_attendance, R.style.Theme_Intro)
                .build());

//        addSlide(new FragmentSlide.Builder()
//                .background(R.color.md_red_600)
//                .backgroundDark(R.color.md_red_900)
//                .fragment(new LoginFragment())
//
//                .canGoForward(false)
//                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_grey_800)
                .backgroundDark(R.color.md_grey_900)
//                .background(R.color.md_red_600)
//                .backgroundDark(R.color.md_red_800)
                .fragment(R.layout.fragment_intro_seating_plan, R.style.Theme_Intro)
                .build());

    }

    @Override
    public void finish() {
        startActivity(new Intent(this, LoginActivity.class));
        super.finish();
    }


    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }
}