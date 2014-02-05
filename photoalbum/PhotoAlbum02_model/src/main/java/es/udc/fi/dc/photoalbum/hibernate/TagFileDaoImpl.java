package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class TagFileDaoImpl extends HibernateDaoSupport implements TagFileDao {

    /**
     * Method create.
     * 
     * @param t
     *            TagFile
     */
    public void create(TagFile t) {
        getHibernateTemplate().save(t);
    }

    /**
     * Method delete.
     * 
     * @param t
     *            TagFile
     */
    public void delete(TagFile t) {
        getHibernateTemplate().delete(t);
    }

    /**
     * Method getById.
     * 
     * @param tagFileId
     *            int
     * @return TagFile
     * @see es.udc.fi.dc.photoalbum.hibernate.TagFileDao#getById(int)
     */
    public TagFile getById(int tagFileId) {
        return getHibernateTemplate().get(TagFile.class, tagFileId);
    }

    /**
     * Method getTagsByFileId.
     * 
     * @param fileId
     *            int
     * @return ArrayList<TagFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.TagFileDao#getTagsByFileId(int)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<TagFile> getTagsByFileId(int fileId) {
        return (ArrayList<TagFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(TagFile.class)
                        .add(Restrictions.eq("file.id", fileId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method getByTagId.
     * 
     * @param tagId
     *            Integer
     * @return ArrayList<TagFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.TagFileDao#getByTagId(Integer)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<TagFile> getByTagId(Integer tagId) {
        return (ArrayList<TagFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(TagFile.class)
                        .add(Restrictions.eq("tag.id", tagId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /**
     * Method get.
     * 
     * @param tagId
     *            Integer
     * @param fileId
     *            Integer
     * @return TagFile
     * @see es.udc.fi.dc.photoalbum.hibernate.TagFileDao#get(Integer, Integer)
     */
    @SuppressWarnings("unchecked")
    public TagFile get(Integer tagId, Integer fileId) {
        ArrayList<TagFile> list = (ArrayList<TagFile>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(TagFile.class)
                                .add(Restrictions.eq("tag.id", tagId))
                                .add(Restrictions.eq("file.id", fileId))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getByTagIdandAlbumId.
     * 
     * @param tagId
     *            Integer
     * @param albumId
     *            Integer
     * @return ArrayList<TagFile>
     * @see es.udc.fi.dc.photoalbum.hibernate.TagFileDao#getByTagIdandAlbumId(Integer,
     *      Integer)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<TagFile> getByTagIdandAlbumId(Integer tagId,
            Integer albumId) {
        return (ArrayList<TagFile>) getHibernateTemplate().findByCriteria(
                DetachedCriteria.forClass(TagFile.class)
                        .createAlias("file", "fil")
                        .createAlias("fil.album", "al")
                        .add(Restrictions.eq("al.id", albumId))
                        .add(Restrictions.eq("tag.id", tagId))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

}
