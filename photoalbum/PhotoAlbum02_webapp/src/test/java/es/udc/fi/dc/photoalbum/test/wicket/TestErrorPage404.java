package es.udc.fi.dc.photoalbum.test.wicket;

import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_EMAIL_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_PASS_YES;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_USERNAME_EXIST;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;

/**
 */
public class TestErrorPage404 {
    private WicketApp wicketApp;
    private WicketTester tester;

    {
        this.wicketApp = new WicketApp() {
            @Override
            protected void init() {
                ApplicationContextMock context = new ApplicationContextMock();
                UserService mock = new UserService() {
                    public void create(User user) {
                    }

                    public void delete(User user) {
                    }

                    public void update(User user) {
                    }

                    public User getUser(String email, String password) {
                        return null;
                    }

                    public User getById(Integer id) {
                        return new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                    }

                    @Override
                    public User getByUsername(User user) {
                        return new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                    }

                    @Override
                    public User getByEmail(User userEmail) {
                        return new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                    }
                };
                context.putBean("userBean", mock);
                getComponentInstantiationListeners().add(
                        new SpringComponentInjector(this, context));
            }
        };
    }

    @Before
    public void setUp() {
        this.tester = new WicketTester(this.wicketApp);
        this.tester.getSession().setLocale(new Locale("en", "ES"));
        ((MySession) Session.get()).setuId(1);
    }

    @Test
    public void testRendered() {
        this.tester.startPage(ErrorPage404.class);
        tester.assertRenderedPage(ErrorPage404.class);
        tester.assertContains("404.404");
    }
}
