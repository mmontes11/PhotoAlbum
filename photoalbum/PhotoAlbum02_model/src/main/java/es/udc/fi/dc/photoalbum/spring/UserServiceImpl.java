package es.udc.fi.dc.photoalbum.spring;

import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.hibernate.UserDao;

/**
 */
@Transactional
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    /**
     * Method setUserDao.
     * 
     * @param userDao
     *            UserDao
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Method create.
     * 
     * @param user
     *            User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#create(User)
     */
    public void create(User user) {
        userDao.create(user);
    }

    /**
     * Method delete.
     * 
     * @param user
     *            User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#delete(User)
     */
    public void delete(User user) {
        userDao.delete(user);
    }

    /**
     * Method update.
     * 
     * @param user
     *            User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#update(User)
     */
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * Method getUser.
     * 
     * @param email
     *            String
     * @param password
     *            String
     * @return User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#getUser(String, String)
     */
    public User getUser(String email, String password) {
        return userDao.getUser(email, password);
    }

    /**
     * Method getByEmail.
     * 
     * @param userEmail
     *            User
     * @return User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#getByEmail(User)
     */
    public User getByEmail(User userEmail) {
        return userDao.getByEmail(userEmail);
    }

    /**
     * Method getById.
     * 
     * @param id
     *            Integer
     * @return User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#getById(Integer)
     */
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    /**
     * Method getByUsername.
     * 
     * @param user
     *            User
     * @return User
     * @see es.udc.fi.dc.photoalbum.spring.UserService#getByUsername(User)
     */
    public User getByUsername(User user) {
        return userDao.getByUsername(user);
    }

}
