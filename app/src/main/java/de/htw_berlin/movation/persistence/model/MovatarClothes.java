package de.htw_berlin.movation.persistence.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.io.File;
import java.sql.SQLException;

import de.htw_berlin.movation.Constants;

public class MovatarClothes extends BaseDaoEnabled {

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    public Constants.Fitness fitness;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    public Constants.Sex sex;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    public Constants.ClothType clothType;

    @DatabaseField
    public String type;

    @DatabaseField
    public String name;

    @DatabaseField
    public long price;

    @DatabaseField
    public int imageFilePath;

    @DatabaseField
    public boolean owned;

    @SuppressWarnings("unused")
    public MovatarClothes() {
    }

    public MovatarClothes(String name, long price,int image,Constants.Sex sex, Constants.Fitness fitness,boolean owned,Constants.ClothType clothType)
    {
        this.name = name;
        this.price = price;
        this.imageFilePath = image;
        this.sex = sex;
        this.fitness = fitness;
        this.owned = owned;
        this.clothType = clothType;
    }

    @Override
    public int delete() throws SQLException {
        //new File(imageFilePath).delete();
        return super.delete();
    }
}