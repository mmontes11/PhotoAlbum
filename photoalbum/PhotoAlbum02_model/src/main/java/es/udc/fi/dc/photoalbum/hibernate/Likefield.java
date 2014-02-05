package es.udc.fi.dc.photoalbum.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "LIKE_TABLE")
@SuppressWarnings("serial")
public class Likefield implements Serializable {

    private Integer id;
    private User user;
    private int megusta;

    /**
     * Constructor for Likefield.
     */
    public Likefield() {
    }

    /**
     * Constructor for Likefield.
     * 
     * @param id
     *            Integer
     * @param user
     *            User
     * @param megusta
     *            int
     */
    public Likefield(Integer id, User user, int megusta) {
        this.id = id;
        this.user = user;
        this.megusta = megusta;
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
     * Method getMegusta.
     * 
     * @return int
     */
    @Column(name = "MEGUSTA")
    public int getMegusta() {
        return megusta;
    }

    /**
     * Method setMegusta.
     * 
     * @param megusta
     *            int
     */
    public void setMegusta(int megusta) {
        this.megusta = megusta;
    }

}
