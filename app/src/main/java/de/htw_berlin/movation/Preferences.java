package de.htw_berlin.movation;

import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preferences {


    String username();
    String password();
    boolean autologin();
    boolean hasBeenStarted();
    boolean hasActiveGoal();

    int creditsEarnedLifeTime();
    int maxPulse();
    int minPulse();
    int successfullGoals();

    @DefaultInt(0)
    int indexGender();

    @DefaultInt(0)
    int indexFitness();

    @DefaultInt(0)
    int indexHairstyle();

    @DefaultInt(0)
    int indexHairColor();

    @DefaultInt(0)
    int indexExpression();

    @DefaultInt(0)
    int indexEyeColor();

    @DefaultInt(0)
    int indexTop();

    @DefaultInt(0)
    int indexBottom();
}

