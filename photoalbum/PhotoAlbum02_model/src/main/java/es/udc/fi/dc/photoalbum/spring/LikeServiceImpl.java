package es.udc.fi.dc.photoalbum.spring;

import java.util.ArrayList;
import java.util.List;

import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.LikeCommentDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;
import es.udc.fi.dc.photoalbum.hibernate.LikeFileDao;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;

/**
 */
public class LikeServiceImpl implements LikeService {

    private LikeDao likeDao;
    private LikeAlbumDao likeAlbumDao;
    private LikeCommentDao likeCommentDao;
    private LikeFileDao likeFileDao;

    /**
     * Method setLikeDao.
     * 
     * @param likeDao
     *            LikeDao
     */
    public void setLikeDao(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    /**
     * Method setLikeAlbumDao.
     * 
     * @param likeAlbumDao
     *            LikeAlbumDao
     */
    public void setLikeAlbumDao(LikeAlbumDao likeAlbumDao) {
        this.likeAlbumDao = likeAlbumDao;
    }

    /**
     * Method setLikeCommentDao.
     * 
     * @param likeCommentDao
     *            LikeCommentDao
     */
    public void setLikeCommentDao(LikeCommentDao likeCommentDao) {
        this.likeCommentDao = likeCommentDao;
    }

    /**
     * Method setLikeFileDao.
     * 
     * @param likeFileDao
     *            LikeFileDao
     */
    public void setLikeFileDao(LikeFileDao likeFileDao) {
        this.likeFileDao = likeFileDao;
    }

    // Like
    // ******************************************************************************************************
    /**
     * Method updateLikeDislike.
     * 
     * @param likeId
     *            int
     * @param megusta
     *            int
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#updateLikeDislike(int,
     *      int)
     */
    @Override
    public void updateLikeDislike(int likeId, int megusta) {
        likeDao.updateLikeDislike(likeId, megusta);
    }

    // Album
    // ********************************************************************************************************

    /**
     * Method create.
     * 
     * @param likeAlbum
     *            LikeAlbum
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#create(LikeAlbum)
     */
    @Override
    public void create(LikeAlbum likeAlbum) {
        likeDao.create(likeAlbum.getLike());
        likeAlbumDao.create(likeAlbum);
    }

    /**
     * Method delete.
     * 
     * @param like
     *            LikeAlbum
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#delete(LikeAlbum)
     */
    @Override
    public void delete(LikeAlbum like) {
        Likefield l = like.getLike();
        likeAlbumDao.delete(like);
        likeDao.delete(l);
    }

    /**
     * Method getLikeAlbumsByAlbumId.
     * 
     * @param albumId
     *            int
     * @return List<LikeAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeAlbumsByAlbumId(int)
     */
    @Override
    public List<LikeAlbum> getLikeAlbumsByAlbumId(int albumId) {
        return likeAlbumDao.getLikeAlbumsByAlbum(albumId);
    }

    /**
     * Method getLikesAlbum.
     * 
     * @param albumId
     *            int
     * @param megusta
     *            int
     * @return List<LikeAlbum>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikesAlbum(int, int)
     */
    @Override
    public List<LikeAlbum> getLikesAlbum(int albumId, int megusta) {
        List<LikeAlbum> likealbums = likeAlbumDao.getLikeAlbumsByAlbum(albumId);
        List<LikeAlbum> likes = new ArrayList<LikeAlbum>();
        for (LikeAlbum la : likealbums) {
            if (la.getLike().getMegusta() == megusta) {
                likes.add(la);
            }
        }
        return likes;
    }

    /**
     * Method getLikeAlbumByUserAlbum.
     * 
     * @param userId
     *            int
     * @param albumId
     *            int
     * @return LikeAlbum
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeAlbumByUserAlbum(int,
     *      int)
     */
    @Override
    public LikeAlbum getLikeAlbumByUserAlbum(int userId, int albumId) {
        for (LikeAlbum aux : likeAlbumDao.getLikeAlbumsByAlbum(albumId)) {
            if (aux.getLike().getUser().getId() == userId) {
                return aux;
            }
        }
        return null;
    }

    // Comment
    // ******************************************************************************************************

    /**
     * Method create.
     * 
     * @param likeComment
     *            LikeComment
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#create(LikeComment)
     */
    @Override
    public void create(LikeComment likeComment) {
        likeDao.create(likeComment.getLike());
        likeCommentDao.create(likeComment);
    }

    /**
     * Method delete.
     * 
     * @param like
     *            LikeComment
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#delete(LikeComment)
     */
    @Override
    public void delete(LikeComment like) {
        Likefield l = like.getLike();
        likeCommentDao.delete(like);
        likeDao.delete(l);
    }

    /**
     * Method getLikeCommentByCommentId.
     * 
     * @param commentId
     *            int
     * @return List<LikeComment>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeCommentByCommentId(int)
     */
    @Override
    public List<LikeComment> getLikeCommentByCommentId(int commentId) {
        return likeCommentDao.getLikeCommentByComment(commentId);
    }

    /**
     * Method getLikesComment.
     * 
     * @param commentId
     *            int
     * @param megusta
     *            int
     * @return List<LikeComment>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikesComment(int, int)
     */
    @Override
    public List<LikeComment> getLikesComment(int commentId, int megusta) {
        List<LikeComment> likecomments = likeCommentDao
                .getLikeCommentByComment(commentId);
        List<LikeComment> likes = new ArrayList<LikeComment>();
        for (LikeComment lc : likecomments) {
            if (lc.getLike().getMegusta() == megusta) {
                likes.add(lc);
            }
        }
        return likes;
    }

    /**
     * Method getLikeCommentByUserComment.
     * 
     * @param userId
     *            int
     * @param commentId
     *            int
     * @return LikeComment
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeCommentByUserComment(int,
     *      int)
     */
    @Override
    public LikeComment getLikeCommentByUserComment(int userId, int commentId) {
        for (LikeComment aux : likeCommentDao
                .getLikeCommentByComment(commentId)) {
            if (aux.getLike().getUser().getId() == userId) {
                return aux;
            }
        }
        return null;
    }

    // File
    // ******************************************************************************************************

    /**
     * Method create.
     * 
     * @param likeFile
     *            LikeFile
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#create(LikeFile)
     */
    @Override
    public void create(LikeFile likeFile) {
        likeDao.create(likeFile.getLike());
        likeFileDao.create(likeFile);
    }

    /**
     * Method delete.
     * 
     * @param like
     *            LikeFile
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#delete(LikeFile)
     */
    @Override
    public void delete(LikeFile like) {
        Likefield l = like.getLike();
        likeFileDao.delete(like);
        likeDao.delete(l);
    }

    /**
     * Method getLikeFileByFileId.
     * 
     * @param fileId
     *            int
     * @return List<LikeFile>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeFileByFileId(int)
     */
    @Override
    public List<LikeFile> getLikeFileByFileId(int fileId) {
        return likeFileDao.getLikeFileByFile(fileId);
    }

    /**
     * Method getLikesFile.
     * 
     * @param fileId
     *            int
     * @param megusta
     *            int
     * @return List<LikeFile>
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikesFile(int, int)
     */
    @Override
    public List<LikeFile> getLikesFile(int fileId, int megusta) {
        List<LikeFile> likefiles = likeFileDao.getLikeFileByFile(fileId);
        List<LikeFile> likes = new ArrayList<LikeFile>();
        for (LikeFile lf : likefiles) {
            if (lf.getLike().getMegusta() == megusta) {
                likes.add(lf);
            }
        }
        return likes;
    }

    /**
     * Method getLikeFileByUserFile.
     * 
     * @param userProfileId
     *            int
     * @param fileId
     *            int
     * @return LikeFile
     * @see es.udc.fi.dc.photoalbum.spring.LikeService#getLikeFileByUserFile(int,
     *      int)
     */
    @Override
    public LikeFile getLikeFileByUserFile(int userProfileId, int fileId) {
        for (LikeFile aux : likeFileDao.getLikeFileByFile(fileId)) {
            if (aux.getLike().getUser().getId() == userProfileId) {
                return aux;
            }
        }
        return null;
    }

}
