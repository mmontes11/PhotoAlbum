package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;

import es.udc.fi.dc.photoalbum.hibernate.Album;

/**
 */
public interface AlbumService {

    /**
     * Method create.
     * 
     * @param album
     *            Album
     */
    void create(Album album);

    /**
     * Method delete.
     * 
     * @param album
     *            Album
     */
    void delete(Album album);

    /**
     * Method getAlbum.
     * 
     * @param name
     *            String
     * @param userId
     *            int
     * @return Album
     */
    Album getAlbum(String name, int userId);

    /**
     * Method rename.
     * 
     * @param album
     *            Album
     * @param newName
     *            String
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
}
