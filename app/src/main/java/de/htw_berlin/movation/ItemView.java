package de.htw_berlin.movation;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.htw_berlin.movation.persistence.model.MovatarClothes;

/**
 * Created by Telan on 03.04.2016.
 */
@EViewGroup(R.layout.item_adapter)
public class ItemView extends RelativeLayout{
        @ViewById
        TextView firstLine;

        @ViewById
        TextView secondLine;

        @ViewById
        ImageView icon;

        public ItemView(Context context) {
            super(context);
        }

        public void bind(MovatarClothes item)
        {
            Picasso.with(getContext()).load(item.imageFilePath).resize(100,200)
                    .into(icon);
            firstLine.setText(item.name);
            secondLine.setText("Credits: " + Long.toString(item.price));
        }
}
