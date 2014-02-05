package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

/**
 */
public interface ShareInformationAlbumsDao extends
        GenericDao<ShareInformationAlbums> {

    /**
     * @param userShared
     *            user, that shared albums
     * @param userSharedTo
     *            user, to whom shared
     * 
     * @return corresponding shares
     */
    List<ShareInformationAlbums> getShares(User userShared, User userSharedTo);

    /**
     * 
     * @param userSharedToId
     *            Id of user album shared to
     * @param userSharedEmail
     *            email of user, that shares
     * 
     * @param albumId
     *            int
     * @return share
     */
    ShareInformationAlbums getShare(int albumId, int userSharedToId,
            String userSharedEmail);

    /**
     * Method getAlbumShares.
     * 
     * @param albumId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     */
    ArrayList<ShareInformationAlbums> getAlbumShares(int albumId);

    /**
     * Method getUserShares.
     * 
     * @param userId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     */
    ArrayList<ShareInformationAlbums> getUserShares(int userId);

    /**
     * Method getShare.
     * 
     * @param albumId
     *            int
     * @param userSharedToId
     *            int
     * @return ShareInformationAlbums
     */
    ShareInformationAlbums getShare(int albumId, int userSharedToId);
}
