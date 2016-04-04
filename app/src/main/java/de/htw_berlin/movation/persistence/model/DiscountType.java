package de.htw_berlin.movation.persistence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.io.File;
import java.sql.SQLException;

public class DiscountType extends BaseDaoEnabled {

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String description;

    @DatabaseField
    public long price;

    @DatabaseField
    public int imageFilePath;

    @DatabaseField(index = true)
    public String brand;


    @Override
    public int delete() throws SQLException {
        //new File(imageFilePath).delete();
        return super.delete();
    }

    @SuppressWarnings("unused")
    public DiscountType() {
    }

    public DiscountType(String name, String brand, String description, long price, int imageFilePath) {
        this.name = name;
        this.brand =brand;
        this.description = description;
        this.price = price;
        this.imageFilePath = imageFilePath;
    }

}