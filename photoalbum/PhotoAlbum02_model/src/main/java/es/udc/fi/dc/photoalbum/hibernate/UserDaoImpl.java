package es.udc.fi.dc.photoalbum.hibernate;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    /**
     * Method create.
     * 
     * @param user
     *            User
     */
    public void create(User user) {
        getHibernateTemplate().save(user);
    }

    /**
     * Method delete.
     * 
     * @param user
     *            User
     */
    public void delete(User user) {
        getHibernateTemplate().delete(user);
    }

    /**
     * Method update.
     * 
     * @param user
     *            User
     * @see es.udc.fi.dc.photoalbum.hibernate.UserDao#update(User)
     */
    public void update(User user) {
        getHibernateTemplate().update(user);
    }

    /**
     * Method getUser.
     * 
     * @param email
     *            String
     * @param password
     *            String
     * @return User
     * @see es.udc.fi.dc.photoalbum.hibernate.UserDao#getUser(String, String)
     */
    public User getUser(String email, String password) {
        @SuppressWarnings("unchecked")
        ArrayList<User> list = (ArrayList<User>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(User.class)
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY)
                                .add(Restrictions.eq("email", email)
                                        .ignoreCase())
                                .add(Restrictions.eq("password",
                                        MD5.getHash(password))));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Method getByEmail.
     * 
     * @param userEmail
     *            User
     * @return User
     * @see es.udc.fi.dc.photoalbum.hibernate.UserDao#getByEmail(User)
     */
    public User getByEmail(User userEmail) {
        @SuppressWarnings("unchecked")
        ArrayList<User> list = (ArrayList<User>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(User.class)
                                .add(Restrictions.eq("email",
                                        userEmail.getEmail()))
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
     * @return User
     * @see es.udc.fi.dc.photoalbum.hibernate.UserDao#getById(Integer)
     */
    public User getById(Integer id) {
        return getHibernateTemplate().get(User.class, id);
    }

    /**
     * Method getByUsername.
     * 
     * @param user
     *            User
     * @return User
     * @see es.udc.fi.dc.photoalbum.hibernate.UserDao#getByUsername(User)
     */
    public User getByUsername(User user) {
        @SuppressWarnings("unchecked")
        ArrayList<User> list = (ArrayList<User>) getHibernateTemplate()
                .findByCriteria(
                        DetachedCriteria
                                .forClass(User.class)
                                .add(Restrictions.eq("username",
                                        user.getUsername()))
                                .setResultTransformer(
                                        Criteria.DISTINCT_ROOT_ENTITY));
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
