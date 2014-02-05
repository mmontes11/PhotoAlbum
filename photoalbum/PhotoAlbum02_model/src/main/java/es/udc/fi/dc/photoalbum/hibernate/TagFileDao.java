package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

/**
 */
public interface TagFileDao extends GenericDao<TagFile> {

    /**
     * Method getById.
     * 
     * @param tagFileId
     *            int
     * @return TagFile
     */
    TagFile getById(int tagFileId);

    /**
     * Method getTagsByFileId.
     * 
     * @param fileId
     *            int
     * @return ArrayList<TagFile>
     */
    ArrayList<TagFile> getTagsByFileId(int fileId);

    /**
     * @param tagId
     *            el id del tag (no el id de tagAlbum)
     * 
     * @return ArrayList<TagFile>
     */
    ArrayList<TagFile> getByTagId(Integer tagId);

    /**
     * Method getByTagIdandAlbumId.
     * 
     * @param tagId
     *            Integer
     * @param albumId
     *            Integer
     * @return ArrayList<TagFile>
     */
    ArrayList<TagFile> getByTagIdandAlbumId(Integer tagId, Integer albumId);

    /**
     * Method get.
     * 
     * @param tagId
     *            Integer
     * @param fileId
     *            Integer
     * @return TagFile
     */
    TagFile get(Integer tagId, Integer fileId);

}
