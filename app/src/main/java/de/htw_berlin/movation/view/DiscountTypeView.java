package de.htw_berlin.movation.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.htw_berlin.movation.R;
import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;

/**
 * Created by Telan on 05.04.2016.
 */
@EViewGroup(R.layout.item_adapter)
public class DiscountTypeView extends RelativeLayout {
    @ViewById
    TextView firstLine;

    @ViewById
    TextView secondLine;

    @ViewById
    ImageView icon;

    public DiscountTypeView(Context context) {
        super(context);
    }

    public void bind(DiscountType item)
    {
        Picasso.with(getContext()).load(item.imageFilePath).resize(50,50)
                .into(icon);
        firstLine.setText(item.name);
        secondLine.setText("Credits: " + Long.toString(item.price));
    }
}
