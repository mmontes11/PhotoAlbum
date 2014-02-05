package es.udc.fi.dc.photoalbum.utils;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlRootElement(name = "album")
@XmlType(name = "fileType", propOrder = { "id", "name", "username", "num",
        "date" })
public class AlbumDTO {
    private Integer id;
    private String name;
    private String username;
    private int num;
    private Calendar date;

    /**
     * Constructor for AlbumDTO.
     */
    public AlbumDTO() {
        super();
    }

    /**
     * Constructor for AlbumDTO.
     * 
     * @param id
     *            Integer
     * @param name
     *            String
     * @param username
     *            String
     * @param num
     *            int
     * @param date
     *            Calendar
     */
    public AlbumDTO(Integer id, String name, String username, int num,
            Calendar date) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
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
     * Method getUsername.
     * 
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method setUsername.
     * 
     * @param username
     *            String
     */
    public void setUsername(String username) {
        this.username = username;
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

}
