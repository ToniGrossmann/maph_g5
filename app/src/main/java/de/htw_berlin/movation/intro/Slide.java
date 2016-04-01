package de.htw_berlin.movation.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Created by Seb on 12.03.2016.
 */
@EFragment
public class Slide extends Fragment {

    @FragmentArg
    int resId;
    AppIntroActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(resId, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppIntroActivity) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isResumed() && isVisibleToUser) {
            activity.setSwipeLock(false);
            activity.setProgressButtonEnabled(true);

        }
    }
}
