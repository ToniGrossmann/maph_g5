package de.htw_berlin.movation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateQuality;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.Assignment;
import de.htw_berlin.movation.persistence.model.Vitals;

@EService
public class BandService extends Service {

    private final String TAG = getClass().getSimpleName();
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1f;
    BandClient client;
    boolean started = false;
    boolean run = true;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Vitals, Long> mVitalsDao;
    @SystemService
    LocationManager locationManager;
    @SystemService
    NotificationManager notificationManager;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Assignment, Long> assignmentDao;
    @Pref
    Preferences_ prefs;
    Assignment currentAssignment;
    float runMeters = 0;
    int currentPulse = 0;
    boolean firstGpsFixAcquired = false;
    boolean firstPulseFixAcquired = false;
    boolean hasStartVibrated = false;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            //toast(location.toString());
            if (mLastLocation.getAccuracy() != 0.0 && firstPulseFixAcquired)
                runMeters += location.distanceTo(mLastLocation);
            mLastLocation.set(location);
            if(runMeters >= currentAssignment.goal.runDistance){
                currentAssignment.status = Assignment.Status.COMPLETED;
                try {
                currentAssignment.update();
                    client.getNotificationManager().vibrate(VibrationType.THREE_TONE_HIGH);
                } catch (BandIOException | SQLException e) {
                    e.printStackTrace();
                }
                prefs.startedAssignmentId().remove();
                run = false;
                showSuccessNotification();
                stopSelf();
            }
            firstGpsFixAcquired = true;
            showInfoNotification(true, firstPulseFixAcquired);
            Log.d(getClass().getSimpleName(), String.valueOf(runMeters));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
            String statusString = null;
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    statusString = "OUT_OF_SERVICE";
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    statusString = "TEMPORARILY_UNAVAILABLE";
                    break;
                case LocationProvider.AVAILABLE:
                    statusString = "AVAILABLE";
                    break;

            }

            Log.e(TAG, "Status: " + statusString);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private void toast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(BandService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "onCreate()");
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers)
            if (locationManager.getLastKnownLocation(provider) != null)
                toast("provider: " + provider + ", " + locationManager.getLastKnownLocation(provider).toString());
        BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
        client = BandClientManager.getInstance().create(getApplication(), devices[0]);
        connectToBand();
    }

    @Background
    void pollHeartRate() {
        while (run) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.d(getClass().getSimpleName(), "pollHeartRate()");
        }


        stopSelf();
    }

    @Background
    void connectToBand() {
        BandPendingResult<ConnectionState> a = client.connect();
        try {
            ConnectionState state = a.await();
            if(!run){
                client.disconnect().await();
                return;
            }
            if (state == ConnectionState.CONNECTED) {
                client.getSensorManager().registerHeartRateEventListener(new BandHeartRateEventListener() {
                    @Override
                    public void onBandHeartRateChanged(BandHeartRateEvent bandHeartRateEvent) {
                        /*if (mLocationListeners[0].mLastLocation.getAccuracy() == 0) {
                            Log.d(getClass().getSimpleName(), "accuracy 0");
                            return;
                        }*/
                        //Log.i(getClass().getSimpleName(), "onBandHeartRateChanged() " + bandHeartRateEvent);
                        Vitals vitals = new Vitals();
                        vitals.pulse = bandHeartRateEvent.getHeartRate();
                        vitals.timeStamp = new Date(bandHeartRateEvent.getTimestamp());
                        vitals.lat = mLocationListeners[0].mLastLocation.getLatitude();
                        vitals.lon = mLocationListeners[0].mLastLocation.getLongitude();
                        vitals.assignment = currentAssignment;

                        if (bandHeartRateEvent.getQuality().equals(HeartRateQuality.LOCKED)) {
                            currentPulse = bandHeartRateEvent.getHeartRate();
                            firstPulseFixAcquired = true;

                            if (!firstGpsFixAcquired) {
                                showInfoNotification(false, true);
                                return;
                            }
                            if (firstPulseFixAcquired && firstGpsFixAcquired && !hasStartVibrated) {
                                hasStartVibrated = true;
                                showInfoNotification(true, true);
                                try {
                                    client.getNotificationManager().vibrate(VibrationType.THREE_TONE_HIGH);
                                } catch (BandIOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                showInfoNotification(true, true);


                            try {
                                mVitalsDao.create(vitals);


                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else
                            currentPulse = 0;
                    }
                });
                Log.d(getClass().getSimpleName(), "state == ConnectionState.CONNECTED");
            } else {
                Log.d(getClass().getSimpleName(), "state != ConnectionState.CONNECTED");
            }
        } catch (InterruptedException ex) {
            // handle InterruptedException
        } catch (BandException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        run = false;
        notificationManager.cancel(Constants.NOTIFICATION_ID);
        try {
            client.disconnect().await();
        } catch (InterruptedException | BandException e) {
            e.printStackTrace();
        }
        for (LocationListener l : mLocationListeners)
            locationManager.removeUpdates(l);
        Log.d(getClass().getSimpleName(), "onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* prevent multiple polling instances */
        /*try {
            prefs.startedAssignmentId().put((long) assignmentDao.create(new Assignment() {{
                goal = new Goal() {{
                    description = "asdf";
                }};
            }}));
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        if (!started) {
            started = true;
            if (!prefs.startedAssignmentId().exists()) {
                Log.d(getClass().getSimpleName(), "!prefs.startedAssignmentId().exists()");

                stopSelf();
                run = false;
                return START_NOT_STICKY;
            }
            try {
                currentAssignment = assignmentDao.queryForId(prefs.startedAssignmentId().get());
                if (currentAssignment == null) {
                    Log.d(getClass().getSimpleName(), "currentAssignment == null");
                    stopSelf();
                    run = false;
                    return START_NOT_STICKY;
                }
                currentAssignment.status = Assignment.Status.STARTED;
                currentAssignment.update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            showInfoNotification(firstGpsFixAcquired, firstPulseFixAcquired);
            pollHeartRate();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void showSuccessNotification(){
        Intent intent = new Intent(this, MainActivity_.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setTicker(getResources().getString(R.string.notification_success, (int)runMeters))
                .setContentTitle(getResources().getString(R.string.assignment_finished))
                .setContentText(getResources().getString(R.string.notification_success, (int)runMeters))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT));
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    void showInfoNotification(boolean gpsFixAcquired, boolean pulseFixAcquired) {
        if (!run)
            return;
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] textLines = new String[3];
        textLines[0] = getResources().getString(R.string.notification_started_assignment, currentAssignment.goal.description);
        if (!gpsFixAcquired)
            textLines[1] = getResources().getString(R.string.notification_please_wait_for_gps);
        else
            textLines[1] = getResources().getString(R.string.notification_run_meters, (int) runMeters);
        if (!pulseFixAcquired)
            textLines[2] = getResources().getString(R.string.notification_please_wait_for_pulse);

        if (currentPulse != 0)
            textLines[2] = getResources().getString(R.string.notification_current_pulse, currentPulse);

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(getResources().getString(R.string.notification_title));
        // Moves events into the expanded layout
        for (String s : textLines) {

            inboxStyle.addLine(s);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setTicker(getResources().getString(R.string.notification_ticker))
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setStyle(inboxStyle);


        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build());
    }
}
