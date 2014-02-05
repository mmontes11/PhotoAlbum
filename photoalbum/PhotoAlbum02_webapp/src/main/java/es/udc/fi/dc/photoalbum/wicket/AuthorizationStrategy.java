package es.udc.fi.dc.photoalbum.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;

import es.udc.fi.dc.photoalbum.wicket.pages.auth.HottestPics;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.SignOut;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.ForgotPassword;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Login;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Register;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.RegistryCompleted;

/**
 * Authorization Strategy (for Pages). If not authorized - can access only
 * Login, Register, ForgotPassword and RegistryCompleted pages. If authorized -
 * can't access Login, Register, ForgotPassword and RegistryCompleted pages.
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
class AuthorizationStrategy implements IAuthorizationStrategy {

    /**
     * Method isActionAuthorized.
     * 
     * @param component
     *            Component
     * @param action
     *            Action
     * @return boolean
     * @see org.apache.wicket.authorization.IAuthorizationStrategy#isActionAuthorized(Component,
     *      Action)
     */
    public boolean isActionAuthorized(Component component, Action action) {
        return true;
    }

    /**
     * Method isInstantiationAuthorized.
     * 
     * @param componentClass
     *            Class<T>
     * @return boolean
     * @see org.apache.wicket.authorization.IAuthorizationStrategy#
     *      isInstantiationAuthorized(Class<T>)
     */
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
            Class<T> componentClass) {
        if (!Page.class.isAssignableFrom(componentClass)) {
            return true;
        }
        if ((Login.class.isAssignableFrom(componentClass))
                || (Register.class.isAssignableFrom(componentClass))
                || (RegistryCompleted.class.isAssignableFrom(componentClass))
                || (ForgotPassword.class.isAssignableFrom(componentClass))) {
            if (((MySession) Session.get()).isAuthenticated()
                    || (((MySession) Session.get())
                            .isAuthenticatedWithCookies())) {
                throw new RestartResponseAtInterceptPageException(
                        HottestPics.class);
            } else {
                return true;
            }
        }
        if (SignOut.class.isAssignableFrom(componentClass)) {
            if (((MySession) Session.get()).isAuthenticated()
                    || (((MySession) Session.get())
                            .isAuthenticatedWithCookies())) {
                return true;
            } else {
                throw new RestartResponseAtInterceptPageException(Login.class);
            }
        }
        if (!(((MySession) Session.get()).isAuthenticated())) {
            throw new RestartResponseAtInterceptPageException(Login.class);
        }
        return true;
    }
}
