package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class CommentFileDaoImpl extends HibernateDaoSupport implements
        CommentFileDao {

    /**
     * Method create.
     * 
     * @param t
     *            CommentFile
     */
    @Override
    public void create(CommentFile t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            CommentFile
     */
    @Override
    public void delete(CommentFile t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getById.
     * 
     * @param commentFileId
     *            int
     * @return CommentFile
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentFileDao#getById(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public CommentFile getById(int commentFileId) {
        ArrayList<CommentFile> list = (ArrayList<CommentFile>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(CommentFile.class)
                                .add(Restrictions.eq("id", commentFileId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getCommentFilesByFile.
     * 
     * @param fileId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentFileDao#getCommentFilesByFile(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentFile> getCommentFilesByFile(int fileId) {
        return (ArrayList<CommentFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentFile.class)
                        .add(Restrictions.eq("file.id", fileId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getCommentFilesByUser.
     * 
     * @param userId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentFileDao#getCommentFilesByUser(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentFile> getCommentFilesByUser(int userId) {
        return (ArrayList<CommentFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentFile.class)
                        .createAlias("comment", "co")
                        .createAlias("co.user", "us")
                        .add(Restrictions.eq("us.id", userId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getCommentFileByFileUser.
     * 
     * @param fileId
     *            int
     * @param userId
     *            int
     * @return List<CommentFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.CommentFileDao#getCommentFileByFileUser(int,
     *      int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CommentFile> getCommentFileByFileUser(int fileId, int userId) {
        return (ArrayList<CommentFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(CommentFile.class)
                        .createAlias("comment", "co")
                        .createAlias("co.user", "us")
                        .add(Restrictions.eq("file.id", fileId))
                        .add(Restrictions.eq("us.id", userId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }
}
