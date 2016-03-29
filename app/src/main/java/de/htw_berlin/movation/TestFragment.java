package de.htw_berlin.movation;

import android.app.Service;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.activity_shop)
public class TestFragment extends Fragment {
    @App
    MyApplication app;
    BandClient client;

    @Background
    void connectToBand(){
        try {
            client.connect().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BandException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        //wenn kein Gerät gefunden wurde
        //client = BandClientManager.getInstance().create(getActivity().getBaseContext(), devices[0]);
        //connectToBand();
        //Log.d(getClass().getSimpleName(), client.getConnectionState().name());
    }

    @Override
    public void onStart() {
        super.onStart();
        //if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
            //BandService_.intent(getActivity().getApplication()).start();
        app.startService(BandService_.intent(app).flags(Service.START_STICKY).get());
    /*
    } else

        {
            // user has not consented yet, request it
            client.getSensorManager().requestHeartRateConsent(getActivity(), new HeartRateConsentListener() {
                @Override
                public void userAccepted(boolean consentGiven) {
                    if (consentGiven) {
                        BandService_.intent(getActivity()).start();

                    }
                }
            });


        }
        */
    }

}