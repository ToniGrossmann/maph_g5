package de.htw_berlin.movation;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.htw_berlin.movation.persistence.model.DiscountType;
import de.htw_berlin.movation.persistence.model.MovatarClothes;

/**
 * Created by Telan on 03.04.2016.
 */
@EViewGroup(R.layout.item_adapter)
public class PurchaseItemView extends RelativeLayout{
        @ViewById
        TextView firstLine;

        @ViewById
        TextView secondLine;

        public PurchaseItemView(Context context) {
            super(context);
        }

        public void bind(MovatarClothes item)
        {
            firstLine.setText(item.name);
            secondLine.setText("Credits: " + Long.toString(item.price));
        }
}
