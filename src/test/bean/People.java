package test.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * People
 */
public class People {
    private String name;
    private String unit;
    private Date birth;
    private char gender;
    private String species;
    private int stature;
    private int weight;

    public People() {
    }

    public People(String name, String unit, Date birth, char gender) {
        setName(name);
        setUnit(unit);
        setBirth(birth);
        setGender(gender);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(final Date birth) {
        this.birth = birth;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getStature() {
        return stature;
    }

    public void setStature(int stature) {
        this.stature = stature;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private static String date(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Override
    public String toString() {
        return "{name: " + getName() 
                    + ", unit: " + getUnit() 
                    + ", birth: " + date(getBirth()) 
                    + ", gender: " + getGender()
                    + ", stature: " + getStature() 
                    + ", weight: " + getWeight() 
                + "}";
    }
}