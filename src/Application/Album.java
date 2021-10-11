package Application;

import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Album class used throughout to populate the Inventory.
 */
public class Album {
    private static int totalAlbums;

    private SimpleStringProperty bandName;
    private SimpleStringProperty albumName;
    private ArrayList<Song> trackList;
    private SimpleStringProperty publisher;
    private LocalDate datePublished;

    Album(String bandName, String albumName, String publisher,
          ArrayList<Song> trackList, LocalDate datePublished) {
        this.setBandName(bandName);
        this.setAlbumName(albumName);
        this.setPublisher(publisher);
        this.trackList = trackList;

        this.setDatePublished(datePublished);
        Album.totalAlbums = Album.getTotalAlbums() + 1;
    }

    public static int getTotalAlbums() {
        return Album.totalAlbums;
    }

    protected static void deleteAlbum () {
        Album.totalAlbums--;
    }

    public String getBandName() { return this.bandName.get();}

    public void setBandName(String bandName) { this.bandName = new SimpleStringProperty(bandName);}

    public String getAlbumName() {
        return this.albumName.get();
    }

    public void setAlbumName(String albumName) { this.albumName = new SimpleStringProperty(albumName); }

    public String getPublisher() { return this.publisher.get(); }

    public void setPublisher(String publisher) { this.publisher = new SimpleStringProperty(publisher); }

    public ArrayList<Song> getTrackList() {
        return this.trackList;
    }

    public LocalDate getDatePublished() { return this.datePublished; }

    public void setDatePublished(LocalDate datePublished) { this.datePublished = datePublished;}

    public int numOfSongs () {
        return this.getTrackList().size();
    }

    public double length() {
        double length = 0;
        for (Song song: this.getTrackList()) {
            length += song.getLength();
        }
        return length;
    }

    public boolean isSelfTitled() {
        return this.getBandName().equals(this.getAlbumName());
    }

    @Override
    public String toString() {
        return this.getBandName() + " - " + this.getAlbumName() + " | Released " + this.getDatePublished()
                + " | Length: " + this.length() + " | Publisher: " + this.getPublisher() ;
    }
}
