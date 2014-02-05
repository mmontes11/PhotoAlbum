package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 */
@Entity
@Table(name = "ALBUM")
@SuppressWarnings("serial")
public class Album implements Serializable {

    private Integer id;
    private String name;
    private User user;
    private Set<File> files = new HashSet<File>();
    private Set<ShareInformationAlbums> shareInformation = new HashSet<ShareInformationAlbums>();
    private Calendar albumDate;

    /**
     * Constructor for Album.
     */
    public Album() {
        this.albumDate = Calendar.getInstance();
    }

    /**
     * Constructor for Album.
     * 
     * @param id
     *            INTeger
     * @param name
     *            String
     * @param user
     *            User
     * @param files
     *            Set<File>
     * @param shareInformation
     *            Set<ShareInformationAlbums>
     */
    public Album(Integer id, String name, User user, Set<File> files,
            Set<ShareInformationAlbums> shareInformation) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.files = files;
        this.shareInformation = shareInformation;
        this.albumDate = Calendar.getInstance();
    }

    /**
     * Method getFiles.
     * 
     * @return Set<File>
     */
    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    public Set<File> getFiles() {
        return files;
    }

    /**
     * Method setFiles.
     * 
     * @param files
     *            Set<File>
     */
    public void setFiles(Set<File> files) {
        this.files = files;
    }

    /**
     * Method getShareInformation.
     * 
     * @return Set<ShareInformationAlbums>
     */
    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
    public Set<ShareInformationAlbums> getShareInformation() {
        return shareInformation;
    }

    /**
     * Method setShareInformation.
     * 
     * @param shareInformation
     *            Set<ShareInformationAlbums>
     */
    public void setShareInformation(Set<ShareInformationAlbums> shareInformation) {
        this.shareInformation = shareInformation;
    }

    /**
     * Method getUser.
     * 
     * @return User
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    /**
     * Method setUser.
     * 
     * @param user
     *            User
     */
    public void setUser(User user) {
        this.user = user;
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
    @Column(name = "NAME")
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
     * Method getAlbumDate.
     * 
     * @return Calendar
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ALBUM_DATE")
    public Calendar getAlbumDate() {
        return albumDate;
    }

    /**
     * Method setAlbumDate.
     * 
     * @param albumDate
     *            Calendar
     */
    public void setAlbumDate(Calendar albumDate) {
        this.albumDate = albumDate;
    }
}
