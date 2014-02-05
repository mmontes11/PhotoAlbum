package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;

import javax.management.InstanceNotFoundException;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;

/**
 */
public interface FileService {

    /**
     * Method create.
     * 
     * @param file
     *            File
     */
    void create(File file);

    /**
     * Method delete.
     * 
     * @param file
     *            File
     */
    void delete(File file);

    /**
     * Method find.
     * 
     * @param id
     *            int
     * @return File
     * @throws InstanceNotFoundException
     */
    File find(int id) throws InstanceNotFoundException;

    /**
     * Method getFileOwn.
     * 
     * @param id
     *            int
     * @param name
     *            String
     * @param userId
     *            int
     * @return File
     */
    File getFileOwn(int id, String name, int userId);

    /**
     * Method getFileShared.
     * 
     * @param id
     *            int
     * @param name
     *            String
     * @param userId
     *            int
     * @return File
     */
    File getFileShared(int id, String name, int userId);

    /**
     * Method changeAlbum.
     * 
     * @param file
     *            File
     * @param album
     *            Album
     */
    void changeAlbum(File file, Album album);

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return File
     */
    File getById(Integer id);

    /**
     * Method getAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return ArrayList<File>
     */
    ArrayList<File> getAlbumFiles(int albumId);

    /**
     * Method getAlbumFilesPaging.
     * 
     * @param albumId
     *            int
     * @param first
     *            int
     * @param count
     *            int
     * @return ArrayList<File>
     */
    ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count);

    /**
     * Method getCountAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return Long
     */
    Long getCountAlbumFiles(int albumId);
}
