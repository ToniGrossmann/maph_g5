package de.htw_berlin.movation;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.lang.ref.WeakReference;
import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_sensor)
public class SensorFragment extends Fragment {

    private static final String USER_ID = "param1";

    // TODO: Rename and change types of parameters
    private User mUser;
    @FragmentArg
    long mUserId;
    private DatabaseHelper dbHelper;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @ViewById
    Button btnHeartRateConsent;
    @ViewById
    Button btnRegisterSensors;
    @ViewById
    Button btnUnregisterSensors;

    @ViewById
    TextView txtBandStatus;
    @ViewById
    TextView txtHeartRateStatus;
    @ViewById
    TextView txtPedometerStatus;
    @ViewById
    TextView txtCaloriesStatus;
    @ViewById
    TextView txtContactStatus;
    @ViewById
    TextView txtDistanceStatus;
    /*

        btnHeartRateConsent = (Button) view.findViewById(R.id.btnHeartRateConsent);
        btnRegisterSensors = (Button) view.findViewById(R.id.btnRegisterSensors);
        btnUnregisterSensors = (Button) view.findViewById(R.id.btnUnregisterSensors);

        txtBandStatus = (TextView) view.findViewById(R.id.txtBandStatus);
        txtHeartRateStatus = (TextView) view.findViewById(R.id.txtHeartRateStatus);
        txtPedometerStatus = (TextView) view.findViewById(R.id.txtPedometerStatus);
        txtCaloriesStatus = (TextView) view.findViewById(R.id.txtCaloriesStatus);
        txtContactStatus = (TextView) view.findViewById(R.id.txtContactStatus);
        txtDistanceStatus = (TextView) view.findViewById(R.id.txtDistanceStatus);
     */

    private BandClient client;
    private BandHeartRateEventListener mHeartRateEventListener;
    private BandPedometerEventListener mPedometerEventListener;
    private BandCaloriesEventListener mCaloriesEventListener;
    private BandContactEventListener mContactEventListener;
    private BandDistanceEventListener mDistanceEventListener;
    WeakReference<Activity> reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = new WeakReference<Activity>(getActivity());
        dbHelper = ((MyApplication)getActivity().getApplication()).getHelper();
        if (getArguments() != null) {
            try {
                mUser = dbHelper.<User, Integer> getGenericDao(User.class).queryForId(getArguments().getInt(USER_ID));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        client = null;
        mHeartRateEventListener = new BandHeartRateEventListener() {
            @Override
            public void onBandHeartRateChanged(final BandHeartRateEvent event) {
                if (event != null) {
                    appendToUI(String.format("Herzfrequenz = %d Schläge pro Minute\n"
                            + "Messqualität = %s\n", event.getHeartRate(), event.getQuality()),
                            Constants.SensorTypes.HEART_RATE);
                }
            }
        };
        mPedometerEventListener = new BandPedometerEventListener() {
            @Override
            public void onBandPedometerChanged(final BandPedometerEvent event) {
                if (event != null) {
                    appendToUI(String.format("Schritte insgesamt: %d\n",
                            event.getTotalSteps()), Constants.SensorTypes.PEDOMETER);
                }
            }
        };

        mCaloriesEventListener = new BandCaloriesEventListener() {
            @Override
            public void onBandCaloriesChanged(final BandCaloriesEvent event) {
                if (event != null) {
                    appendToUI(String.format("Kalorien insgesamt: %d\n", event.getCalories()),
                            Constants.SensorTypes.CALORIES);
                }
            }
        };

        mContactEventListener = new BandContactEventListener() {
            @Override
            public void onBandContactChanged(final BandContactEvent event) {
                if (event != null) {
                    String contactState = "";
                    switch (event.getContactState()) {
                        case UNKNOWN:
                            contactState = "unbekannt";
                        break;
                        case WORN:
                            contactState = "wird getragen";
                            break;
                        case NOT_WORN:
                            contactState = "wird nicht getragen";
                            break;
                        default:
                            break;
                    }
                    appendToUI(String.format("Bandkontakt: %s\n", contactState),
                            Constants.SensorTypes.CONTACT);
                }
            }
        };

        mDistanceEventListener = new BandDistanceEventListener() {
            @Override
            public void onBandDistanceChanged(final BandDistanceEvent event) {
                if (event != null) {
                    String motionType = "";
                    switch (event.getMotionType()) {
                        case IDLE:
                            motionType = "Ruhe";
                            break;
                        case JOGGING:
                            motionType = "joggend";
                            break;
                        case RUNNING:
                            motionType = "rennend";
                            break;
                        case UNKNOWN:
                            motionType = "unbekannt";
                            break;
                        case WALKING:
                            motionType = "gehend";
                            break;
                        default:
                            break;
                    }
                    appendToUI(String.format("Bewegungstyp: %s\n\nTempo: %.2f min/km\n\n" +
                            "Geschwindigkeit: %.2f km/h\n\nDistanz insgesamt: %.2f km\n",
                            motionType, event.getPace() * 0.016666666666667,
                            event.getSpeed() * 0.036, event.getTotalDistance() * 0.00001),
                            Constants.SensorTypes.DISTANCE);
                }
            }
        };
    }

    @Click
    void btnHeartRateConsent(){
        new HeartRateConsentTask().execute(reference);
    }
    @Click
    void btnRegisterSensors(){
        new SensorSubscriptionTask().execute();
    }
    @Click
    void btnUnregisterSensors(){
        if (client != null) {
            try {
                client.getSensorManager().unregisterAllListeners();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnRegisterSensors.setEnabled(true);
                        btnUnregisterSensors.setEnabled(false);
                    }
                });
                txtBandStatus.setText("Messung beendet.");
                txtHeartRateStatus.setText("");
                txtPedometerStatus.setText("");
                txtCaloriesStatus.setText("");
                txtContactStatus.setText("");
                txtDistanceStatus.setText("");
            } catch (BandIOException e) {
                appendToUI(e.getMessage(), Constants.SensorTypes.NONE);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                txtBandStatus.setText("Bandclient konnte aufgrund einer Verbindungsunterbrechung" +
                        "nicht beendet werden");
            } catch (BandException e) {
                txtBandStatus.setText("Bandclient konnte nicht beendet werden");
            }
        }
    }








    //Fragt nach der Erlaubnis auf die Herzfrequenz zugreifen zu duerfen,
    // wenn eine Verbindung zum Band besteht.
    //Laeuft asynchron im Background Thread. Ergebnisausgabe im UI Thread.
    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(WeakReference<Activity>... params) {
            try {
                //wenn Band Client existiert
                if (getConnectedBandClient()) {
                    if (params[0].get() != null) {
                        appendToUI("Verbindung mit Band hergestellt.\n", Constants.SensorTypes.NONE);
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                                if (consentGiven) {
                                    appendToUI("Pulsmessung erlaubt.\n", Constants.SensorTypes.NONE);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnHeartRateConsent.setEnabled(false);
                                            btnRegisterSensors.setEnabled(true);
                                        }
                                    });
                                }
                            }
                        });

                    }
                } else {
                    appendToUI("Band ist nicht verbunden. Vergewissere dich, dass Bluetooth" +
                            " angeschaltet ist und sich das Band in Reichweite befindet.\n",
                            Constants.SensorTypes.NONE);
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService unterstützt " +
                                "nicht die SDK Version. Bitte auf das neuste SDK updaten.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService ist nicht erreichbar. " +
                                "Vergewissere dich, dass Microsoft Health installiert " +
                                "ist und die notwendigen Rechte vergeben sind.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage, Constants.SensorTypes.NONE);

            } catch (Exception e) {
                appendToUI(e.getMessage(), Constants.SensorTypes.NONE);
            }
            return null;
        }
    }

    //Registiert den EventListener fuer die Sensoren, wenn eine Verbindung zum Band
    //und die Erlaubnis zum Zugriff besteht. Laeuft asynchron im Background Thread.
    // Ergebnisausgabe im UI Thread.
    private class SensorSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //wenn Band Client existiert
                if (getConnectedBandClient()) {
                    //wenn die Erlaubnis erteilt ist auf die Herzfrequenz zugreifen zu duerfen
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {

                        client.getSensorManager().registerHeartRateEventListener(mHeartRateEventListener);
                        client.getSensorManager().registerPedometerEventListener(mPedometerEventListener);
                        client.getSensorManager().registerCaloriesEventListener(mCaloriesEventListener);
                        client.getSensorManager().registerContactEventListener(mContactEventListener);
                        client.getSensorManager().registerDistanceEventListener(mDistanceEventListener);
                        appendToUI("Messung gestartet.\n", Constants.SensorTypes.NONE);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnRegisterSensors.setEnabled(false);
                                btnUnregisterSensors.setEnabled(true);
                            }
                        });

                    } else {
                        appendToUI("Du hast der Anwendung noch keine Zustimmung " +
                                "gegeben auf die Herzfrequenz zugreifen zu können."
                                + " Bitte den Button \"Pulsmessung erlauben\" drücken.\n",
                                Constants.SensorTypes.NONE);
                    }
                } else {
                    appendToUI("Band ist nicht verbunden. Vergewissere dich, dass Bluetooth " +
                            "angeschaltet ist und sich das Band in Reichweite befindet.\n",
                            Constants.SensorTypes.NONE);
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService unterstützt" +
                                " nicht die SDK Version. Bitte auf das neuste SDK updaten.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService ist nicht erreichbar." +
                                " Vergewissere dich, dass Microsoft Health installiert ist und" +
                                " die notwendigen Rechte vergeben sind.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage, Constants.SensorTypes.NONE);

            } catch (Exception e) {
                appendToUI(e.getMessage(), Constants.SensorTypes.NONE);
            }
            return null;
        }
    }

    //Der uebergebene String wird in der Status-Textview angezeigt
    private void appendToUI(final String string, final Constants.SensorTypes sensorType) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (sensorType) {
                    case NONE:
                        txtBandStatus.setText(string);
                        break;
                    case HEART_RATE:
                        txtHeartRateStatus.setText(string);
                        break;
                    case PEDOMETER:
                        txtPedometerStatus.setText(string);
                        break;
                    case CALORIES:
                        txtCaloriesStatus.setText(string);
                        break;
                    case CONTACT:
                        txtContactStatus.setText(string);
                        break;
                    case DISTANCE:
                        txtDistanceStatus.setText(string);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //Ist noch kein Band Client vorhanden, wird nach Baendern gesucht, die mit dem
    //Smartphone verbunden sind. Mit dem ersten gefundenen Gerät wird der Band Client
    //erzeugt.
    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        //wenn noch kein Band Client existiert
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            //wenn kein Gerät gefunden wurde
            if (devices.length == 0) {
                appendToUI("Band ist nicht mit dem Smartphone verbunden.\n", Constants.SensorTypes.NONE);
                return false;
            }
            client = BandClientManager.getInstance().create(getActivity().getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }
        //UI Statusausgabe waehrend die Verbindung aufgebaut wird
        appendToUI("Verbindung mit Band wird hergestellt...\n", Constants.SensorTypes.NONE);
        return ConnectionState.CONNECTED == client.connect().await();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
