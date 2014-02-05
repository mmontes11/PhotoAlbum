package es.udc.fi.dc.photoalbum.spring;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao;

/**
 */
@Transactional
public class ShareInformationPhotosServiceImpl implements
        ShareInformationPhotosService {

    private ShareInformationPhotosDao shareInformationPhotosDao;

    private ShareInformationAlbumsDao shareInformationAlbumsDao;

    /**
     * Method setShareInformationPhotosDao.
     * 
     * @param shareInformationPhotosDao
     *            ShareInformationPhotosDao
     */
    public void setShareInformationPhotosDao(
            ShareInformationPhotosDao shareInformationPhotosDao) {
        this.shareInformationPhotosDao = shareInformationPhotosDao;
    }

    /**
     * Method setShareInformationAlbumsDao.
     * 
     * @param shareInformationAlbumsDao
     *            ShareInformationAlbumsDao
     */
    public void setShareInformationAlbumsDao(
            ShareInformationAlbumsDao shareInformationAlbumsDao) {
        this.shareInformationAlbumsDao = shareInformationAlbumsDao;
    }

    /**
     * Method create.
     * 
     * @param sip
     *            ShareInformationPhotos
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#create(ShareInformationPhotos)
     */
    public void create(ShareInformationPhotos sip) {
        shareInformationPhotosDao.create(sip);
        Album album = sip.getFile().getAlbum();
        if (shareInformationAlbumsDao.getAlbumShares(album.getId()).isEmpty()) {
            shareInformationAlbumsDao.create(new ShareInformationAlbums(null,
                    album, sip.getUser()));
        }
    }

    /**
     * Method delete.
     * 
     * @param sip
     *            ShareInformationPhotos
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#delete(ShareInformationPhotos)
     */
    public void delete(ShareInformationPhotos sip) {
        shareInformationPhotosDao.delete(sip);

        int albumId = sip.getFile().getAlbum().getId();
        int userSharedToId = sip.getUser().getId();
        if (shareInformationPhotosDao.getPhotosShares(albumId, userSharedToId)
                .isEmpty()) {
            shareInformationAlbumsDao.delete(shareInformationAlbumsDao
                    .getShare(albumId, userSharedToId));
        }
    }

    /**
     * Method getPhotosShares.
     * 
     * @param photoId
     *            int
     * @return List<ShareInformationPhotos>
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#getPhotosShares(int)
     */
    public List<ShareInformationPhotos> getPhotosShares(int photoId) {
        return shareInformationPhotosDao.getPhotosShares(photoId);
    }

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
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#getShare(int,
     *      int, String)
     */
    public ShareInformationPhotos getShare(int photoId, int userSharedToId,
            String userSharedEmail) {
        return shareInformationPhotosDao.getShare(photoId, userSharedToId,
                userSharedEmail);
    }

    /**
     * Method getShareInformation.
     * 
     * @param photoId
     *            int
     * @param userId
     *            int
     * @return ShareInformationPhotos
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#getShareInformation(int,
     *      int)
     */
    public ShareInformationPhotos getShareInformation(int photoId, int userId) {
        return shareInformationPhotosDao.getSharingInformation(photoId, userId);
    }

    /**
     * Method getPhotosShares.
     * 
     * @param albumId
     *            int
     * @param userId
     *            int
     * @return List<ShareInformationPhotos>
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService#getPhotosShares(int,
     *      int)
     */
    public List<ShareInformationPhotos> getPhotosShares(int albumId, int userId) {
        return shareInformationPhotosDao.getPhotosShares(albumId, userId);
    }

}
