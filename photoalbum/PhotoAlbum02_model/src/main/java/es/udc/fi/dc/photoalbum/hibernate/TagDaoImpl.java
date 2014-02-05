package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 */
public class TagDaoImpl extends HibernateDaoSupport implements TagDao {

    /**
     * Method create.
     * 
     * @param tag
     *            Tag
     */
    public void create(Tag tag) {
        getHibernateTemplate().save(tag);
    }

    /**
     * Method delete.
     * 
     * @param tag
     *            Tag
     */
    public void delete(Tag tag) {
        getHibernateTemplate().delete(tag);
    }

    /**
     * Method getByName.
     * 
     * @param name
     *            String
     * @return Tag
     * @see es.udc.fi.dc.photoalbum.hibernate.TagDao#getByName(String)
     */
    @SuppressWarnings("unchecked")
    public Tag getByName(String name) {
        ArrayList<Tag> list = (ArrayList<Tag>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(Tag.class)
                                .add(Restrictions.eq("name", name))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return Tag
     * @see es.udc.fi.dc.photoalbum.hibernate.TagDao#getById(Integer)
     */
    public Tag getById(Integer id) {
        return getHibernateTemplate().get(Tag.class, id);
    }

    /**
     * Method rename.
     * 
     * @param tag
     *            Tag
     * @param name
     *            String
     * @see es.udc.fi.dc.photoalbum.hibernate.TagDao#rename(Tag, String)
     */
    public void rename(Tag tag, String name) {
        tag.setName(name);
        getHibernateTemplate().update(tag);
    }

}
