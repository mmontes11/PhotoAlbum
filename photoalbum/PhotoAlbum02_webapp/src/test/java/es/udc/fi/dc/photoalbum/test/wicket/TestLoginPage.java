package es.udc.fi.dc.photoalbum.test.wicket;

import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_EMAIL_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_EMAIL_NOT;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_PASS_NO;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_PASS_YES;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_USERNAME_EXIST;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Albums;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Login;

/**
 */
public class TestLoginPage {

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
                        return new User(null, USER_USERNAME_EXIST, email,
                                password);
                    }

                    public User getById(Integer id) {
                        return new User(id, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                    }

                    @Override
                    public User getByEmail(User userEmail) {
                        new User(null, USER_USERNAME_EXIST,
                                userEmail.getEmail(), USER_PASS_YES);
                        return null;
                    }

                    @Override
                    public User getByUsername(User user) {
                        new User(null, user.getUsername(), user.getEmail(),
                                USER_PASS_YES);
                        return null;
                    }
                };
                AlbumService mockAlbum = new AlbumService() {
                    public void rename(Album album, String newName) {
                    }

                    public Album getAlbum(String name, int userId) {
                        return null;
                    }

                    public void delete(Album album) {
                    }

                    public void create(Album album) {
                    }

                    public Album getById(Integer id) {
                        return null;
                    }

                    public ArrayList<Album> getAlbums(Integer id) {
                        return new ArrayList<Album>();
                    }
                };
                context.putBean("userBean", mock);
                context.putBean("albumBean", mockAlbum);
                getComponentInstantiationListeners().add(
                        new SpringComponentInjector(this, context));
            }
        };
    }

    @Before
    public void setUp() {
        this.tester = new WicketTester(this.wicketApp);
        this.tester.startPage(Login.class);
        this.tester.getSession().setLocale(new Locale("en", "EN"));
    }

    @Test
    public void testNoInputAndLocale() {
        tester.assertInvisible("signout");
        FormTester formTester = this.tester.newFormTester("form");
        this.tester.getSession().setLocale(Locale.ENGLISH);
        formTester = this.tester.newFormTester("form");
        formTester.setValue("email", "");
        formTester.setValue("password", "");
        formTester.submit();
        this.tester.assertErrorMessages("'Email or Username' is required.",
                "'Password' is required.");
    }

    @Test
    public void testNotEmailNoPass() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("email", USER_EMAIL_NOT);
        formTester.setValue("password", "");
        formTester.submit();
        this.tester.assertErrorMessages("'Password' is required.");
    }

    @Test
    public void testNotEmailPass() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("email", USER_EMAIL_NOT);
        formTester.setValue("password", USER_PASS_NO);
        formTester.submit();
        this.tester.assertErrorMessages();
    }

    @Test
    public void testNoEmailPass() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("email", "");
        formTester.setValue("password", USER_PASS_NO);
        formTester.submit();
        this.tester.assertErrorMessages("'Email or Username' is required.");
    }

    @Test
    public void testNoUser() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("email", USER_EMAIL_EXIST);
        formTester.setValue("password", USER_PASS_NO);
        formTester.submit();
        this.tester.assertErrorMessages();
    }

    @Test
    public void testUser() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("email", USER_EMAIL_EXIST);
        formTester.setValue("password", USER_PASS_YES);
        formTester.submit();
        tester.assertRenderedPage(Albums.class);
    }
}
