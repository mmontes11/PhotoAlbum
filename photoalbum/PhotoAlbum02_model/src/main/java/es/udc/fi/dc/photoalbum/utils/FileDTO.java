package es.udc.fi.dc.photoalbum.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlRootElement(name = "file")
@XmlType(name = "fileType", propOrder = { "id", "name", "fileSmall",
        "albumName", "num", "date" })
public class FileDTO {

    private Integer id;
    private String name;
    private byte[] fileSmall;
    private String albumName;
    private int num;
    private Calendar date;

    /**
     * Constructor for FileDTO.
     */
    public FileDTO() {
    }

    /**
     * Constructor for FileDTO.
     * 
     * @param id
     *            Integer
     * @param name
     *            String
     * @param fileSmall
     *            byte[]
     * @param albumName
     *            String
     * @param num
     *            int
     * @param date
     *            Calendar
     */
    public FileDTO(Integer id, String name, byte[] fileSmall, String albumName,
            int num, Calendar date) {
        super();
        this.id = id;
        this.name = name;
        this.fileSmall = Arrays.copyOf(fileSmall, fileSmall.length);
        this.albumName = albumName;
        this.num = num;
        this.date = date;
    }

    /**
     * Method getId.
     * 
     * @return Integer
     */
    public Integer getId() {
        return id;
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
    public String getName() {
        return name;
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
     * Method getFileSmall.
     * 
     * @return byte[]
     */
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
     * Method getAlbumName.
     * 
     * @return String
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Method setAlbumName.
     * 
     * @param albumName
     *            String
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Method getNum.
     * 
     * @return int
     */
    public int getNum() {
        return num;
    }

    /**
     * Method setNum.
     * 
     * @param num
     *            int
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Method getDate.
     * 
     * @return Calendar
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Method setDate.
     * 
     * @param date
     *            Calendar
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return "FileDTO [id=" + id + ", name=" + name + ", albumName="
                + albumName + ", num=" + num + ", date="
                + sdf.format(date.getTime()) + "]";
    }

}
