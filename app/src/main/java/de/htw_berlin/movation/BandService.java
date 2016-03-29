package de.htw_berlin.movation;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;

@EService
public class BandService extends IntentService {

    BandClient client;
    boolean run = true;

    public BandService() {
        super(BandService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "onCreate()");
        BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        //wenn kein Gerät gefunden wurde
        client = BandClientManager.getInstance().create(getApplication(), devices[0]);
    }

    @Background
    void connectToBand() {
        BandPendingResult<ConnectionState> a = client.connect();
        try {
            ConnectionState state = a.await();
            if (state == ConnectionState.CONNECTED) {
                client.getSensorManager().registerHeartRateEventListener(new BandHeartRateEventListener() {
                    @Override
                    public void onBandHeartRateChanged(BandHeartRateEvent bandHeartRateEvent) {
                        Log.i(getClass().getSimpleName(), "onBandHeartRateChanged() " + bandHeartRateEvent);
                    }
                });
                Log.d(getClass().getSimpleName(), "state == ConnectionState.CONNECTED");
            } else {
                Log.d(getClass().getSimpleName(), "state != ConnectionState.CONNECTED");
            }
        } catch (InterruptedException ex) {
            // handle InterruptedException
        } catch (BandException ex) {
            // handle BandException
        }


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(getClass().getSimpleName(), "onStart()");
        connectToBand();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
