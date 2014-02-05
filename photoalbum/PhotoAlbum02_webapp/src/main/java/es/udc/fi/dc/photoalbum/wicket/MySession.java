package es.udc.fi.dc.photoalbum.wicket;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.cookies.CookieUtils;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;

/**
 * Session that holds current user
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class MySession extends WebSession {

    @SpringBean
    private UserService userService;
    private Integer uId;

    /**
     * Method getuId.
     * 
     * @return Integer
     */
    public Integer getuId() {
        return uId;
    }

    /**
     * Method setuId.
     * 
     * @param uId
     *            Integer
     */
    public void setuId(Integer uId) {
        this.uId = uId;
    }

    /**
     * Constructor for MySession.
     * 
     * @param request
     *            Request
     */
    MySession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    /**
     * Checks if user is Authenticated
     * 
     * 
     * @return boolean
     */
    public boolean isAuthenticated() {
        return (this.uId != null);
    }

    /**
     * Method isAuthenticatedWithCookies.
     * 
     * @return boolean
     */
    public boolean isAuthenticatedWithCookies() {
        CookieUtils cu = new CookieUtils();
        String username = cu.load("username");
        String email = cu.load("email");
        String password = cu.load("password");
        if ((username != null) && (email != null) && (password != null)) {
            User user = new User(null, username, email, password);
            System.out.println(email + "              " + password);
            System.out.println(user.getEmail() + user.getPassword());
            User userDB = userService.getByEmail(user);
            if (userDB == null) {
                return false;
            }
            if (userDB.getPassword().equals(password)) {
                this.uId = userDB.getId();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
