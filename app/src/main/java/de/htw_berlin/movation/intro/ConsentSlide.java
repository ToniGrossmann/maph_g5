package de.htw_berlin.movation.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.HeartRateConsentListener;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import de.htw_berlin.movation.MyApplication;
import de.htw_berlin.movation.R;

@EFragment
public class ConsentSlide extends Fragment {

    @FragmentArg
    int resId;
    @ViewById
    Button btnHeartRateConsent;
    @App
    MyApplication app;
    BandClient client;
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

    @Click
    void btnHeartRateConsent(View v) {
        connectToBand();
    }

    void getBand() {
        BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        if (devices.length != 0)
            client = BandClientManager.getInstance().create(app, devices[0]);
    }

    @UiThread
    void showAlert(String message) {
        new AlertDialog.Builder(getActivity()).setMessage(message).show();
    }

    @Background
    void connectToBand() {
        BandPendingResult<ConnectionState> a = client.connect();
        try {
            ConnectionState state = a.await();
            if (state == ConnectionState.CONNECTED) {
                if (client.getSensorManager().getCurrentHeartRateConsent() != UserConsent.GRANTED)
                    client.getSensorManager().requestHeartRateConsent(getActivity(), new HeartRateConsentListener() {
                        @Override
                        public void userAccepted(boolean b) {
                            if (b) {
                                activity.setNextPageSwipeLock(false);
                                activity.setProgressButtonEnabled(true);
                                btnHeartRateConsent.setEnabled(false);
                                btnHeartRateConsent.setText(R.string.heart_rate_consent_given);
                            }


                        }
                    });
            } else {
                Log.d(getClass().getSimpleName(), "state != ConnectionState.CONNECTED");
            }
        } catch (InterruptedException ex) {
            // handle InterruptedException
        } catch (BandException ex) {
            StringBuilder exceptionMessage = new StringBuilder();
            switch (ex.getErrorType()) {
                case UNSUPPORTED_SDK_VERSION_ERROR:
                    exceptionMessage.append("Microsoft Health BandService unterstützt " +
                            "nicht die SDK Version. Bitte auf das neuste SDK updaten.\n");
                    break;
                case SERVICE_ERROR:
                    exceptionMessage.append("Microsoft Health BandService ist nicht erreichbar. " +
                            "Vergewissere dich, dass Microsoft Health installiert " +
                            "ist und die notwendigen Rechte vergeben sind.\n");
                    break;
                default:
                    exceptionMessage.append("Unknown error occured: " + ex.getMessage() + "\n");
                    break;

            }
            showAlert(exceptionMessage.toString());
        }


    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            getBand();
            if (client == null) {
                activity.setProgressButtonEnabled(true);
                activity.setNextPageSwipeLock(false);
                btnHeartRateConsent.setEnabled(false);
                btnHeartRateConsent.setText(R.string.heart_rate_consent_given);
            } else if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                activity.setNextPageSwipeLock(false);
                activity.setProgressButtonEnabled(true);
                btnHeartRateConsent.setEnabled(false);
                btnHeartRateConsent.setText(R.string.heart_rate_consent_given);
            } else {
                activity.setProgressButtonEnabled(false);
                activity.setNextPageSwipeLock(true);
            }
        }
    }
}
