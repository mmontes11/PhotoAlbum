package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface LikeAlbumDao extends GenericDao<LikeAlbum> {

    /**
     * Method getLikeAlbumsByAlbum.
     * 
     * @param albumId
     *            int
     * @return List<LikeAlbum>
     */
    List<LikeAlbum> getLikeAlbumsByAlbum(int albumId);

    /**
     * Method getPositiveLikesByAlbum.
     * 
     * @param albumId
     *            int
     * @return int
     */
    int getPositiveLikesByAlbum(int albumId);

}
