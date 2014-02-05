package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "USUARIO")
@SuppressWarnings("serial")
public class User implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Set<Album> albums = new HashSet<Album>();
    private Set<ShareInformationAlbums> shareInformation = new HashSet<ShareInformationAlbums>();

    /**
     * Constructor for User.
     */
    public User() {
    }

    /**
     * Constructor for User.
     * 
     * @param id
     *            Integer
     * @param username
     *            String
     * @param email
     *            String
     * @param password
     *            String
     */
    public User(Integer id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Method getAlbums.
     * 
     * @return Set<Album>
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public Set<Album> getAlbums() {
        return albums;
    }

    /**
     * Method setAlbums.
     * 
     * @param albums
     *            Set<Album>
     */
    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    /**
     * Method getShareInformation.
     * 
     * @return Set<ShareInformationAlbums>
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
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
     * Method getEmail.
     * 
     * @return String
     */
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    /**
     * Method setEmail.
     * 
     * @param email
     *            String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method getPassword.
     * 
     * @return String
     */
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    /**
     * Method setPassword.
     * 
     * @param password
     *            String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method getUsername.
     * 
     * @return String
     */
    @Column(name = "USERNAME")
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

}
