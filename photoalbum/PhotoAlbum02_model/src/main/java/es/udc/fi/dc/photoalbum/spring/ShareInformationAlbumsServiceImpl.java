package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbumsDao;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotosDao;
import es.udc.fi.dc.photoalbum.hibernate.User;

/**
 */
@Transactional
public class ShareInformationAlbumsServiceImpl implements
        ShareInformationAlbumsService {

    private ShareInformationAlbumsDao shareInformationAlbumsDao;

    private ShareInformationPhotosDao shareInformationPhotosDao;

    /**
     * Method setShareInformationAlbumsDao.
     * 
     * @param shareInformationDao
     *            ShareInformationAlbumsDao
     */
    public void setShareInformationAlbumsDao(
            ShareInformationAlbumsDao shareInformationDao) {
        this.shareInformationAlbumsDao = shareInformationDao;
    }

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
     * Method create.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#create(ShareInformationAlbums)
     */
    public void create(ShareInformationAlbums shareInformation) {

        if (getShare(shareInformation.getAlbum().getId(), shareInformation
                .getUser().getId()) == null) {
            shareInformationAlbumsDao.create(shareInformation);
        }
        ArrayList<File> files = new ArrayList<File>(shareInformation.getAlbum()
                .getFiles());
        User user = shareInformation.getUser();
        for (File f : files) {
            if (shareInformationPhotosDao.getSharingInformation(f.getId(),
                    user.getId()) == null) {
                shareInformationPhotosDao.create(new ShareInformationPhotos(
                        null, f, user));
            }
        }
    }

    /**
     * Method delete.
     * 
     * @param shareInformation
     *            ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#delete(ShareInformationAlbums)
     */
    public void delete(ShareInformationAlbums shareInformation) {
        shareInformationAlbumsDao.delete(shareInformation);
        ArrayList<File> files = new ArrayList<File>(shareInformation.getAlbum()
                .getFiles());
        User user = shareInformation.getUser();
        for (File f : files) {
            ShareInformationPhotos sip = shareInformationPhotosDao
                    .getSharingInformation(f.getId(), user.getId());
            if (sip != null) {
                shareInformationPhotosDao.delete(sip);
            }
        }
    }

    /**
     * Method getShares.
     * 
     * @param userShared
     *            User
     * @param userSharedTo
     *            User
     * @return List<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#getShares(User,
     *      User)
     */
    public List<ShareInformationAlbums> getShares(User userShared,
            User userSharedTo) {
        return shareInformationAlbumsDao.getShares(userShared, userSharedTo);
    }

    /**
     * Method alreadyShared.
     * 
     * @param album
     *            Album
     * @param userSharedToId
     *            int
     * @return boolean
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#alreadyShared(Album,
     *      int)
     */
    public boolean alreadyShared(Album album, int userSharedToId) {
        return (shareInformationAlbumsDao.getShare(album.getId(),
                userSharedToId) != null)
                && (shareInformationPhotosDao.getPhotosShares(album.getId(),
                        userSharedToId).size() == album.getFiles().size());
    }

    /**
     * Method getShare.
     * 
     * @param albumId
     *            int
     * @param userSharedToId
     *            int
     * @param userSharedEmail
     *            String
     * @return ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#getShare(int,
     *      int, String)
     */
    public ShareInformationAlbums getShare(int albumId, int userSharedToId,
            String userSharedEmail) {
        return shareInformationAlbumsDao.getShare(albumId, userSharedToId,
                userSharedEmail);
    }

    /**
     * Method getAlbumShares.
     * 
     * @param albumId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#getAlbumShares(int)
     */
    public ArrayList<ShareInformationAlbums> getAlbumShares(int albumId) {
        return shareInformationAlbumsDao.getAlbumShares(albumId);
    }

    /**
     * Method getUserShares.
     * 
     * @param userId
     *            int
     * @return ArrayList<ShareInformationAlbums>
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#getUserShares(int)
     */
    public ArrayList<ShareInformationAlbums> getUserShares(int userId) {
        return shareInformationAlbumsDao.getUserShares(userId);
    }

    /**
     * Method getShare.
     * 
     * @param albumId
     *            int
     * @param userSharedToId
     *            int
     * @return ShareInformationAlbums
     * @see es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService#getShare(int,
     *      int)
     */
    public ShareInformationAlbums getShare(int albumId, int userSharedToId) {
        return shareInformationAlbumsDao.getShare(albumId, userSharedToId);
    }

}
