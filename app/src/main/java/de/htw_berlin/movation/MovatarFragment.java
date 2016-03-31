package de.htw_berlin.movation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.lang.ref.WeakReference;
import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.User;

@EFragment(R.layout.fragment_movatar)
public class MovatarFragment extends Fragment {

    private User mUser;
    @FragmentArg
    long mUserId = -1;
    private DatabaseHelper dbHelper;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<User, Long> userDao;
    @App
    MyApplication app;
    @SystemService
    WindowManager wm;
    @ViewById
    ImageView imgMovatar;

    WeakReference<Activity> reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = new WeakReference<Activity>(getActivity());
        dbHelper = app.getHelper();
        if (mUserId != -1) {
            try {
                mUser = userDao.queryForId(mUserId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterViews
    void afterViews() {
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int[] bitmaps = {R.drawable.layer1_haare_justin_bieber_hintergrund_braun,
                R.drawable.layer2_female_fit_mittel_koerper,
                R.drawable.layer3_mittel_haare_justin_bieber_hautschattierungen,
                R.drawable.layer4_female_mittel_gesichtsausdruck_zufrieden_braun,
                R.drawable.layer5_female_fit_mittel_sporthose_kurz,
                R.drawable.layer6_female_fit_mittel_tshirt_nike,
                R.drawable.layer7_haare_justin_bieber_braun};
        Bitmap dimensions = BitmapFactory.decodeResource(getResources(), R.drawable.layer1_haare_justin_bieber_hintergrund_braun);
        Bitmap canvasBitmap = Bitmap.createBitmap(dimensions.getWidth(), dimensions.getHeight(), Bitmap.Config.ARGB_8888);
        dimensions.recycle();
        Canvas c = new Canvas(canvasBitmap);
        final float scale = getResources().getDisplayMetrics().density;
        int px = (int) (400 * scale + 0.5f);
        for (int bitmap : bitmaps) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), bitmap);
            c.drawBitmap(b, 0, 0, null);
        }
        imgMovatar.setImageDrawable(new BitmapDrawable(getResources(), canvasBitmap));
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
