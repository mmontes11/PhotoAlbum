package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */
public interface AlbumDao extends GenericDao<Album> {

    /**
     * 
     * @param name
     *            name of Album
     * @param userId
     *            album's user id
     * 
     * @return album instance if exists or null
     */
    Album getAlbum(String name, int userId);

    /**
     * 
     * Changes album name
     * 
     * @param album
     *            this album's name will be changed
     * @param newName
     *            change name to
     */
    void rename(Album album, String newName);

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return Album
     */
    Album getById(Integer id);

    /**
     * Method getAlbums.
     * 
     * @param id
     *            Integer
     * @return ArrayList<Album>
     */
    ArrayList<Album> getAlbums(Integer id);

    /**
     * Method searchPublicAlbums.
     * 
     * @param keywordName
     *            String
     * @param keywordComment
     *            String
     * @param keywordTag
     *            String
     * @param initDate
     *            Calendar
     * @param endDate
     *            Calendar
     * @return List<Album>
     */
    List<Album> searchPublicAlbums(String keywordName, String keywordComment,
            String keywordTag, Calendar initDate, Calendar endDate);
}
