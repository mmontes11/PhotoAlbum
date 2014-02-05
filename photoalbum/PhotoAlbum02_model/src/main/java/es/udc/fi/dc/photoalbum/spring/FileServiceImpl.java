package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;

import javax.management.InstanceNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.FileDao;

/**
 */
@Transactional
public class FileServiceImpl implements FileService {

    private FileDao fileDao;

    /**
     * Method setFileDao.
     * 
     * @param fileDao
     *            FileDao
     */
    public void setFileDao(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    /**
     * Method create.
     * 
     * @param file
     *            File
     * @see es.udc.fi.dc.photoalbum.spring.FileService#create(File)
     */
    public void create(File file) {
        fileDao.create(file);
    }

    /**
     * Method delete.
     * 
     * @param file
     *            File
     * @see es.udc.fi.dc.photoalbum.spring.FileService#delete(File)
     */
    public void delete(File file) {
        fileDao.delete(file);
    }

    /**
     * Method find.
     * 
     * @param id
     *            int
     * @return File
     * @throws InstanceNotFoundException
     * @see es.udc.fi.dc.photoalbum.spring.FileService#find(int)
     */
    @Override
    public File find(int id) throws InstanceNotFoundException {
        File f = fileDao.getById(id);
        if (f == null) {
            throw new InstanceNotFoundException("The file with id " + id
                    + " not exists");
        } else {
            return f;
        }
    }

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
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getFileOwn(int, String,
     *      int)
     */
    public File getFileOwn(int id, String name, int userId) {
        return fileDao.getFileOwn(id, name, userId);
    }

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
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getFileShared(int,
     *      String, int)
     */
    public File getFileShared(int id, String name, int userId) {
        return fileDao.getFileShared(id, name, userId);
    }

    /**
     * Method changeAlbum.
     * 
     * @param file
     *            File
     * @param album
     *            Album
     * @see es.udc.fi.dc.photoalbum.spring.FileService#changeAlbum(File, Album)
     */
    public void changeAlbum(File file, Album album) {
        fileDao.changeAlbum(file, album);
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return File
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getById(Integer)
     */
    public File getById(Integer id) {
        return fileDao.getById(id);
    }

    /**
     * Method getAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return ArrayList<File>
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getAlbumFiles(int)
     */
    public ArrayList<File> getAlbumFiles(int albumId) {
        return fileDao.getAlbumFiles(albumId);
    }

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
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getAlbumFilesPaging(int,
     *      int, int)
     */
    public ArrayList<File> getAlbumFilesPaging(int albumId, int first, int count) {
        return fileDao.getAlbumFilesPaging(albumId, first, count);
    }

    /**
     * Method getCountAlbumFiles.
     * 
     * @param albumId
     *            int
     * @return Long
     * @see es.udc.fi.dc.photoalbum.spring.FileService#getCountAlbumFiles(int)
     */
    public Long getCountAlbumFiles(int albumId) {
        return fileDao.getCountAlbumFiles(albumId);
    }
}
