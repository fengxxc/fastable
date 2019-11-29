package test.bean;

import java.util.Date;

/**
 * People
 */
public class People {
    private String name;
    private int old;
    private String unit;
    private Date birth;
    private char gender;

    public People() {
    }

    public People(String name, int old, String unit, Date birth, char gender) {
        setName(name);
        setOld(old);
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

    public int getOld() {
        return old;
    }

    public void setOld(final int old) {
        this.old = old;
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

    @Override
    public String toString() {
        return "{name: " + getName() + ", old: " + getOld() + ", birth: " + getBirth() + ", gender: " + getGender() + "}";
    }
}