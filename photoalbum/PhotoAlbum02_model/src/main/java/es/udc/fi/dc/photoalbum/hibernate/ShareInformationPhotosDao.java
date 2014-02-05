package es.udc.fi.dc.photoalbum.hibernate;

import java.util.List;

/**
 */
public interface ShareInformationPhotosDao extends
        GenericDao<ShareInformationPhotos> {

    /**
     * Method getShare.
     * 
     * @param photoId
     *            int
     * @param userSharedToId
     *            int
     * @param userSharedEmail
     *            String
     * @return ShareInformationPhotos
     */
    ShareInformationPhotos getShare(int photoId, int userSharedToId,
            String userSharedEmail);

    /**
     * Method getPhotosShares.
     * 
     * @param photoId
     *            int
     * @return List<ShareInformationPhotos>
     */
    List<ShareInformationPhotos> getPhotosShares(int photoId);

    /**
     * Method getPhotosSharesTo.
     * 
     * @param userId
     *            int
     * @return List<ShareInformationPhotos>
     */
    List<ShareInformationPhotos> getPhotosSharesTo(int userId);

    /**
     * Method getSharingInformation.
     * 
     * @param photoId
     *            int
     * @param userId
     *            int
     * @return ShareInformationPhotos
     */
    ShareInformationPhotos getSharingInformation(int photoId, int userId);

    /**
     * Method getPhotosShares.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<ShareInformationPhotos>
     */
    List<ShareInformationPhotos> getPhotosShares(int albumId, int userId);
}
