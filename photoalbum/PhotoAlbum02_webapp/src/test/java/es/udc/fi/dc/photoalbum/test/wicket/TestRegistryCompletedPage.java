package es.udc.fi.dc.photoalbum.test.wicket;

import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.RegistryCompleted;

/**
 */
public class TestRegistryCompletedPage {
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

                    public User getUser(User userEmail) {
                        return null;
                    }

                    public User getById(Integer id) {
                        return null;
                    }

                    @Override
                    public User getByEmail(User userEmail) {
                        return null;
                    }

                    @Override
                    public User getByUsername(User user) {
                        return null;
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
        this.tester.getSession().setLocale(new Locale("en", "EN"));
    }

    @Test
    public void testRendered() {
        this.tester.startPage(RegistryCompleted.class);
        tester.assertRenderedPage(RegistryCompleted.class);
    }
}
