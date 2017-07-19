package com.learnera.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by Prejith on 7/13/2016.
 */

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setFullscreen(true);
        super.onCreate(savedInstanceState);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_teal_400)
                .backgroundDark(R.color.md_teal_700)
                .fragment(R.layout.fragment_intro_welcome, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_pink_400)
                .backgroundDark(R.color.md_pink_700)
                .fragment(R.layout.fragment_intro_attendance, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()

                .background(R.color.md_green_400)
                .backgroundDark(R.color.md_green_700)
                .fragment(R.layout.fragment_intro_marks, R.style.Theme_Intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_cyan_400)
                .backgroundDark(R.color.md_cyan_700)
                .fragment(R.layout.fragment_intro_contact, R.style.Theme_Intro)
                .build());

        //// TODO: 7/5/2017 Implement later: 'Login later' button which has same action as next button in intros 
        addSlide(new FragmentSlide.Builder()
                .background(R.color.md_deep_purple_400)
                .backgroundDark(R.color.md_deep_purple_700)
                .fragment(R.layout.fragment_login, R.style.Theme_Intro)
                .build());

    }
}