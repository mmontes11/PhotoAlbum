package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.AlbumDao;

/**
 */
@Transactional
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumDao albumDao;

    /**
     * Method setAlbumDao.
     * 
     * @param albumDao
     *            AlbumDao
     */
    public void setAlbumDao(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    /**
     * Method create.
     * 
     * @param album
     *            Album
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#create(Album)
     */
    public void create(Album album) {
        albumDao.create(album);
    }

    /**
     * Method delete.
     * 
     * @param album
     *            Album
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#delete(Album)
     */
    public void delete(Album album) {
        albumDao.delete(album);
    }

    /**
     * Method getAlbum.
     * 
     * @param name
     *            String
     * @param userId
     *            int
     * @return Album
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#getAlbum(String, int)
     */
    public Album getAlbum(String name, int userId) {
        return albumDao.getAlbum(name, userId);
    }

    /**
     * Method rename.
     * 
     * @param album
     *            Album
     * @param newName
     *            String
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#rename(Album, String)
     */
    public void rename(Album album, String newName) {
        albumDao.rename(album, newName);
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return Album
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#getById(Integer)
     */
    public Album getById(Integer id) {
        return albumDao.getById(id);
    }

    /**
     * Method getAlbums.
     * 
     * @param id
     *            Integer
     * @return ArrayList<Album>
     * @see es.udc.fi.dc.photoalbum.spring.AlbumService#getAlbums(Integer)
     */
    public ArrayList<Album> getAlbums(Integer id) {
        return albumDao.getAlbums(id);
    }
}
