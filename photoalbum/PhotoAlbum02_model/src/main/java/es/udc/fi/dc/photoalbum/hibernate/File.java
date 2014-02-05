package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 */
@Entity
@Table(name = "ARCHIVO")
@SuppressWarnings("serial")
public class File implements Serializable {

    private Integer id;
    private String name;
    private byte[] file;
    private byte[] fileSmall;
    private Album album;
    private Calendar fileDate;

    /**
     * Constructor for File.
     */
    public File() {
        this.fileDate = Calendar.getInstance();
    }

    /**
     * Constructor for File.
     * 
     * @param id
     *            Integer
     * @param name
     *            String
     * @param file
     *            byte[]
     * @param fileSmall
     *            byte[]
     * @param album
     *            Album
     */
    public File(Integer id, String name, byte[] file, byte[] fileSmall,
            Album album) {
        this.id = id;
        this.name = name;
        if (file != null) {
            this.file = Arrays.copyOf(file, file.length);
        } else {
            this.file = new byte[0];
        }
        this.fileSmall = Arrays.copyOf(fileSmall, fileSmall.length);
        this.album = album;
        this.fileDate = Calendar.getInstance();
    }

    /**
     * Method getId.
     * 
     * @return Integer
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }

    /**
     * Method setId.
     * 
     * @param id
     *            Integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Method getName.
     * 
     * @return String
     */
    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }

    /**
     * Method setName.
     * 
     * @param name
     *            String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method getFile.
     * 
     * @return byte[]
     */
    @Column(name = "FILE")
    public byte[] getFile() {
        return file;
    }

    /**
     * Method setFile.
     * 
     * @param file
     *            byte[]
     */
    public void setFile(byte[] file) {
        this.file = Arrays.copyOf(file, file.length);
    }

    /**
     * Method getFileSmall.
     * 
     * @return byte[]
     */
    @Column(name = "FILE_SMALL")
    public byte[] getFileSmall() {
        return fileSmall;
    }

    /**
     * Method setFileSmall.
     * 
     * @param fileSmall
     *            byte[]
     */
    public void setFileSmall(byte[] fileSmall) {
        this.fileSmall = Arrays.copyOf(fileSmall, fileSmall.length);
    }

    /**
     * Method getAlbum.
     * 
     * @return Album
     */
    @ManyToOne
    @JoinColumn(name = "ALBUM_ID")
    public Album getAlbum() {
        return this.album;
    }

    /**
     * Method setAlbum.
     * 
     * @param album
     *            Album
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Method getFileDate.
     * 
     * @return Calendar
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILE_DATE")
    public Calendar getFileDate() {
        return fileDate;
    }

    /**
     * Method setFileDate.
     * 
     * @param fileDate
     *            Calendar
     */
    public void setFileDate(Calendar fileDate) {
        this.fileDate = fileDate;
    }
}
