package de.htw_berlin.movation.persistence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.util.Date;

public class Vitals extends BaseDaoEnabled {
    @DatabaseField
    public int pulse;
    @DatabaseField(id = true)
    public Date timeStamp;
    @DatabaseField(foreign = true)
    public User user;
    @DatabaseField
    public double lat;
    @DatabaseField
    public double lon;
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    public Assignment assignment;
    @DatabaseField
    /**
     * meters per second
     */
    public double velocity;



    public Vitals(){}
    public Vitals(int pulse, Date timeStamp)
    {
        this.pulse = pulse;
        this.timeStamp = timeStamp;
    }
}