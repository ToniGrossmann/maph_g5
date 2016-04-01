package de.htw_berlin.movation;

import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preferences {
    String username();
    String password();
    boolean autologin();
    boolean hasBeenStarted();
    boolean hasActiveGoal();
}
