package es.udc.fi.dc.photoalbum.test.wicket;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Register;

/**
 */
public class TestRegisterPage {
    private WicketApp wicketApp;
    private WicketTester tester;

    {
        this.wicketApp = new WicketApp() {
            @Override
            protected void init() {
                ApplicationContextMock context = new ApplicationContextMock();
                UserService mockUser = new UserService() {

                    @Override
                    public void create(User user) {

                    }

                    @Override
                    public void delete(User user) {

                    }

                    @Override
                    public void update(User user) {

                    }

                    @Override
                    public User getUser(String email, String password) {

                        return null;
                    }

                    @Override
                    public User getByEmail(User userEmail) {

                        return null;
                    }

                    @Override
                    public User getById(Integer id) {

                        return null;
                    }

                    @Override
                    public User getByUsername(User user) {

                        return null;
                    }

                };

                context.putBean("userBean", mockUser);
                getComponentInstantiationListeners().add(
                        new SpringComponentInjector(this, context));
            }

        };
    }

    @Before
    public void setUp() {
        this.tester = new WicketTester(this.wicketApp);
        PageParameters pars = new PageParameters();
        Page page = new Register(pars);
        this.tester.startPage(page);
        this.tester.getSession().setLocale(Locale.US);
    }

    @Test
    public void testRendered() {
        tester.assertRenderedPage(Register.class);
    }

}
