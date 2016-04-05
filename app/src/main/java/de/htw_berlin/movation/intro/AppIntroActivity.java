package de.htw_berlin.movation.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import de.htw_berlin.movation.MainActivity_;
import de.htw_berlin.movation.MyApplication;
import de.htw_berlin.movation.Preferences_;
import de.htw_berlin.movation.R;

public class AppIntroActivity extends AppIntro {
    Preferences_ preferences;

    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        preferences = ((MyApplication) getApplication()).getPreferences();
        addSlide(Slide_.builder().resId(R.layout.intro_slide1).build());
        addSlide(ConsentSlide_.builder().resId(R.layout.intro_slide2).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide3).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide4).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide5).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide6).build());



        /*
        addSlide(Slide_.builder().resId(R.layout.intro_slide3).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide4).build());
        addSlide(Slide_.builder().resId(R.layout.intro_slide5).build());
        */
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color.

        setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
        finish();
        preferences.hasBeenStarted().put(true);
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }
}
