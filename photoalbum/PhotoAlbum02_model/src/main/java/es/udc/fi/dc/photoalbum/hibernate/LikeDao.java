package es.udc.fi.dc.photoalbum.hibernate;

/**
 */
public interface LikeDao extends GenericDao<Likefield> {

    /**
     * Method getById.
     * 
     * @param likeId
     *            int
     * @return Likefield
     */
    Likefield getById(int likeId);

    /**
     * Method updateLikeDislike.
     * 
     * @param likeId
     *            int
     * @param megusta
     *            int
     */
    void updateLikeDislike(int likeId, int megusta);

    /**
     * Method findLike.
     * 
     * @param megusta
     *            int
     * @param user
     *            User
     * @return Likefield
     */
    Likefield findLike(int megusta, User user);

}
