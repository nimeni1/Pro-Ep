package proep.fhict.work.driver.model;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class DateTime implements Serializable {
    private String day;
    private String month;
    private String year;

    public DateTime(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return day+ "." + month + "."+year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public GregorianCalendar getCalendar() {
        int year = Integer.getInteger(getYear());
        int month = Integer.getInteger(getMonth());
        int day = Integer.getInteger(getYear());
        return new GregorianCalendar(year,month,day);
    }
}