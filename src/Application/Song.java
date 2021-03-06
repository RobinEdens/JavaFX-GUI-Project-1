package Application;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Song class that works as a companion class to Album, to allow a little more variety in creating Inventories
    - can be entered.
 */
public class Song {
    private String name;

    // Wanted to output as a LocalTime or something to format better but did not have time to implement
    private double length;

    Song (String name, double length) {
        this.name = name;
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName() + " - Time: " + this.getLength();
    }
}
