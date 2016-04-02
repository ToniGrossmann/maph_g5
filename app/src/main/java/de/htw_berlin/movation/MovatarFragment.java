package de.htw_berlin.movation;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import de.htw_berlin.movation.persistence.DatabaseHelper;
import de.htw_berlin.movation.persistence.model.*;

@EFragment(R.layout.fragment_movatarchange)
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
    @ViewById
    ImageView imgThumbnail;
    @ViewById
    TextView txtFitness;
    @ViewById
    TextView txtCategory;
    @Pref
    Preferences_ preferences;

    private String[] categories = {"Frisur", "Haarfarbe", "Gesicht", "Augenfarbe", "Oberteil", "Unterteil", "Geschlecht"};
    private int currentCategoryIndex = 0;
    private TypedArray layer1;
    private TypedArray layer2_female;
    private TypedArray layer2_male;
    private TypedArray layer3;
    private TypedArray layer4_female;
    private TypedArray layer4_male;
    private TypedArray layer5_female;
    private TypedArray layer5_male;
    private TypedArray layer6_female;
    private TypedArray layer6_male;
    private TypedArray layer7;

    private TypedArray tn_eyecolors;
    private TypedArray tn_haircolors;
    private TypedArray tn_hairstyles;
    private TypedArray tn_genders;
    private TypedArray tn_tops;
    private TypedArray tn_bottoms;
    private TypedArray tn_expressions;

    Drawable[] layers = new Drawable[7];
    LayerDrawable layerDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = app.getHelper();
        if (mUserId != -1) {
            try {
                mUser = userDao.queryForId(mUserId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        currentCategoryIndex = 0;
    }

    @AfterViews
    void afterViews() {

        Resources r = getResources();

        layer1 = r.obtainTypedArray(R.array.layer1);
        layer2_female = r.obtainTypedArray(R.array.layer2_female);
        layer2_male = r.obtainTypedArray(R.array.layer2_male);
        layer3 = r.obtainTypedArray(R.array.layer3);
        layer4_female = r.obtainTypedArray(R.array.layer4_female);
        layer4_male = r.obtainTypedArray(R.array.layer4_male);
        layer5_female = r.obtainTypedArray(R.array.layer5_female);
        layer5_male = r.obtainTypedArray(R.array.layer5_male);
        layer6_female = r.obtainTypedArray(R.array.layer6_female);
        layer6_male = r.obtainTypedArray(R.array.layer6_male);
        layer7 = r.obtainTypedArray(R.array.layer7);

        tn_eyecolors = r.obtainTypedArray(R.array.tn_eyecolors);
        tn_haircolors = r.obtainTypedArray(R.array.tn_haircolors);
        tn_hairstyles = r.obtainTypedArray(R.array.tn_hairstyles);
        tn_genders = r.obtainTypedArray(R.array.tn_genders);
        tn_tops = r.obtainTypedArray(R.array.tn_tops);
        tn_bottoms = r.obtainTypedArray(R.array.tn_bottoms);
        tn_expressions = r.obtainTypedArray(R.array.tn_expressions);

        redrawLayers();

        if (preferences.indexFitness().get() == Constants.Fitness.FIT.ordinal())
            txtFitness.setText(getString(R.string.fitness_level_x, getText(R.string.fit)));
        else if (preferences.indexFitness().get() == Constants.Fitness.AVERAGE.ordinal())
            txtFitness.setText(getString(R.string.fitness_level_x, getText(R.string.normal)));
        else
            txtFitness.setText(getString(R.string.fitness_level_x, getText(R.string.unfit)));

        txtCategory.setText(categories[currentCategoryIndex]);
    }

    private void redrawLayers()
    {
        if (currentCategoryIndex == 0)
        {
            imgThumbnail.setImageDrawable(tn_hairstyles.getDrawable(preferences.indexHairstyle().get()));
        }
        else if (currentCategoryIndex == 1)
        {
            imgThumbnail.setImageDrawable(tn_haircolors.getDrawable(preferences.indexHairColor().get()));
        }
        else if (currentCategoryIndex == 2)
        {
            imgThumbnail.setImageDrawable(tn_expressions.getDrawable(preferences.indexExpression().get()));
        }
        else if (currentCategoryIndex == 3)
        {
            imgThumbnail.setImageDrawable(tn_eyecolors.getDrawable(preferences.indexEyeColor().get()));
        }
        else if (currentCategoryIndex == 4)
        {
            imgThumbnail.setImageDrawable(tn_tops.getDrawable(preferences.indexTop().get()));
        }
        else if (currentCategoryIndex == 5)
        {
            imgThumbnail.setImageDrawable(tn_bottoms.getDrawable(preferences.indexBottom().get()));
        }
        else
        {
            imgThumbnail.setImageDrawable(tn_genders.getDrawable(preferences.indexGender().get()));
        }


        if (preferences.indexGender().get() == 0) // female
        {
            layers[0] = layer1.getDrawable(preferences.indexHairstyle().get() * 3 + preferences.indexHairColor().get());
            layers[1] = layer2_female.getDrawable(preferences.indexFitness().get());
            layers[2] = layer3.getDrawable(preferences.indexHairstyle().get());
            layers[3] = layer4_female.getDrawable(preferences.indexHairColor().get() * 15 + preferences.indexExpression().get() * 3 + preferences.indexEyeColor().get());
            layers[4] = layer5_female.getDrawable(preferences.indexBottom().get() + preferences.indexFitness().get() * 4);
            layers[5] = layer6_female.getDrawable(preferences.indexTop().get() + preferences.indexFitness().get() * 4);
            layers[6] = layer7.getDrawable(preferences.indexHairstyle().get() * 3 + preferences.indexHairColor().get());
        }
        else // male
        {
            layers[0] = layer1.getDrawable(preferences.indexHairstyle().get() * 3 + preferences.indexHairColor().get());
            layers[1] = layer2_male.getDrawable(preferences.indexFitness().get());
            layers[2] = layer3.getDrawable(preferences.indexHairstyle().get());
            layers[3] = layer4_male.getDrawable(preferences.indexHairColor().get() * 15 + preferences.indexExpression().get() * 3 + preferences.indexEyeColor().get());
            layers[4] = layer5_male.getDrawable(preferences.indexBottom().get() + preferences.indexFitness().get() * 4);
            layers[5] = layer6_male.getDrawable(preferences.indexTop().get() + preferences.indexFitness().get() * 4);
            layers[6] = layer7.getDrawable(preferences.indexHairstyle().get() * 3 + preferences.indexHairColor().get());
        }

        layerDrawable = new LayerDrawable(layers);
        imgMovatar.setImageDrawable(layerDrawable);
    }

    @Click
    void btnMovatarCategoryChangeLeft() {
        if (currentCategoryIndex > 0)
        {
            currentCategoryIndex -= 1;
        }
        else
        {
            currentCategoryIndex = 6;
        }
        redrawLayers();
        txtCategory.setText(categories[currentCategoryIndex]);
    }

    @Click
    void btnMovatarCategoryChangeRight() {
        if (currentCategoryIndex < 6)
        {
            currentCategoryIndex += 1;
        }
        else
        {
            currentCategoryIndex = 0;
        }
        redrawLayers();
        txtCategory.setText(categories[currentCategoryIndex]);
    }

    @Click
    void btnMovatarChangeLeft() {

        if (currentCategoryIndex == 0) // Hairstyle
        {
            if (preferences.indexHairstyle().get() - 1 >= 0)
                preferences.indexHairstyle().put(preferences.indexHairstyle().get() - 1);
            else
                preferences.indexHairstyle().put(4);
        }
        else if (currentCategoryIndex == 1) // Hair color
        {
            if (preferences.indexHairColor().get() - 1 >= 0)
                preferences.indexHairColor().put(preferences.indexHairColor().get() - 1);
            else
                preferences.indexHairColor().put(4);
        }
        else if (currentCategoryIndex == 2) // Expression
        {
            if (preferences.indexExpression().get() - 1 >= 0)
                preferences.indexExpression().put(preferences.indexExpression().get() - 1);
            else
                preferences.indexExpression().put(4);
        }
        else if (currentCategoryIndex == 3) // Eye color
        {
            if (preferences.indexEyeColor().get() - 1 >= 0)
                preferences.indexEyeColor().put(preferences.indexEyeColor().get() - 1);
            else
                preferences.indexEyeColor().put(2);
        }
        else if (currentCategoryIndex == 4) // Top
        {
            if (preferences.indexTop().get() - 1 >= 0)
                preferences.indexTop().put(preferences.indexTop().get() - 1);
            else
                preferences.indexTop().put(3);
        }
        else if (currentCategoryIndex == 5) // Bottom
        {
            if (preferences.indexBottom().get() - 1 >= 0)
                preferences.indexBottom().put(preferences.indexBottom().get() - 1);
            else
                preferences.indexBottom().put(3);
        }
        else // Gender
        {
            if (preferences.indexGender().get() - 1 >= 0)
                preferences.indexGender().put(preferences.indexGender().get() - 1);
            else
                preferences.indexGender().put(1);
        }

        redrawLayers();
    }

    @Click
    void btnMovatarChangeRight() {
        if (currentCategoryIndex == 0) // Hairstyle
        {
            if (preferences.indexHairstyle().get() + 1 < 5)
                preferences.indexHairstyle().put(preferences.indexHairstyle().get() + 1);
            else
                preferences.indexHairstyle().put(0);
        }
        else if (currentCategoryIndex == 1) // Hair color
        {
            if (preferences.indexHairColor().get() + 1 < 3)
                preferences.indexHairColor().put(preferences.indexHairColor().get() + 1);
            else
                preferences.indexHairColor().put(0);
        }
        else if (currentCategoryIndex == 2) // Expression
        {
            if (preferences.indexExpression().get() + 1 < 5)
                preferences.indexExpression().put(preferences.indexExpression().get() + 1);
            else
                preferences.indexExpression().put(0);
        }
        else if (currentCategoryIndex == 3) // Eye color
        {
            if (preferences.indexEyeColor().get() + 1 < 3)
                preferences.indexEyeColor().put(preferences.indexEyeColor().get() + 1);
            else
                preferences.indexEyeColor().put(0);
        }
        else if (currentCategoryIndex == 4) // Top
        {
            if (preferences.indexTop().get() + 1 < 4)
                preferences.indexTop().put(preferences.indexTop().get() + 1);
            else
                preferences.indexTop().put(0);
        }
        else if (currentCategoryIndex == 5) // Bottom
        {
            if (preferences.indexBottom().get() + 1 < 4)
                preferences.indexBottom().put(preferences.indexBottom().get() + 1);
            else
                preferences.indexBottom().put(0);
        }
        else // Gender
        {
            if (preferences.indexGender().get() + 1 < 2)
                preferences.indexGender().put(preferences.indexGender().get() + 1);
            else
                preferences.indexGender().put(0);
        }
        redrawLayers();
    }




    @Click
    void btnShareMovatar() {

        int width = layerDrawable.getIntrinsicWidth();
        int height = layerDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        layerDrawable.draw(canvas);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
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
