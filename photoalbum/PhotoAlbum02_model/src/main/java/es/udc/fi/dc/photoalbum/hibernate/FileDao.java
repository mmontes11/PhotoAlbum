package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */
public interface FileDao extends GenericDao<File> {

    /**
     * @param id
     *            id of file
     * @param name
     *            name of file
     * @param userId
     *            id of user, file belongs to
     * 
     * @return File, that belongs to this userId
     */
    File getFileOwn(int id, String name, int userId);

    /**
     * @param id
     *            id of file
     * @param name
     *            name of file
     * @param userId
     *            Id of user, file shared to
     * 
     * @return file with this id and name, that shared to user with userIdS
     */
    File getFileShared(int id, String name, int userId);

    /**
     * @param file
     *            file, album of what will be changed
     * @param album
     *            change to this
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

    /**
     * Method searchPublicFiles.
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
     * @return List<File>
     */
    List<File> searchPublicFiles(String keywordName, String keywordComment,
            String keywordTag, Calendar initDate, Calendar endDate);
}
