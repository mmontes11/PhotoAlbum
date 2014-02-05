package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

/**
 */
public interface TagAlbumDao extends GenericDao<TagAlbum> {

    /**
     * Method getById.
     * 
     * @param tagAlbumId
     *            int
     * @return TagAlbum
     */
    TagAlbum getById(int tagAlbumId);

    /**
     * @param tagId
     *            el id del tag (no el id de tagAlbum)
     * 
     * @return ArrayList<TagAlbum>
     */
    ArrayList<TagAlbum> getByTagId(int tagId);

    /**
     * Method getTagsByAlbumId.
     * 
     * @param albumId
     *            int
     * @return ArrayList<TagAlbum>
     */
    ArrayList<TagAlbum> getTagsByAlbumId(int albumId);

    /**
     * Method get.
     * 
     * @param tagId
     *            Integer
     * @param albumId
     *            Integer
     * @return TagAlbum
     */
    TagAlbum get(Integer tagId, Integer albumId);

}
