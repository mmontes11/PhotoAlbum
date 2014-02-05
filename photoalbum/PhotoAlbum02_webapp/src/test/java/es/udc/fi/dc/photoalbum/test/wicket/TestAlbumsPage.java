package es.udc.fi.dc.photoalbum.test.wicket;

import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.ALBUM_NAME_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.ALBUM_NAME_NOT_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_EMAIL_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_PASS_YES;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_USERNAME_EXIST;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.wicket.Session;
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
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Albums;

/**
 */
public class TestAlbumsPage {

    private WicketApp wicketApp;
    private WicketTester tester;

    {
        this.wicketApp = new WicketApp() {
            @Override
            protected void init() {
                ApplicationContextMock context = new ApplicationContextMock();
                AlbumService mockAlbum = new AlbumService() {
                    public void rename(Album album, String newName) {
                    }

                    public Album getAlbum(String name, int userId) {
                        return null;
                    }

                    public void delete(Album album) {
                        album.getUser().getAlbums().remove(album);
                    }

                    public void create(Album album) {
                        album.getUser().getAlbums().add(album);

                        if (album.getName().equals("casca")) {
                            throw new RuntimeException();
                        }

                    }

                    public Album getById(Integer id) {
                        return null;
                    }

                    public ArrayList<Album> getAlbums(Integer id) {
                        return new ArrayList<Album>();
                    }
                };
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
                    public User getByEmail(User userEmail) {
                        return new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                    }

                    @Override
                    public User getByUsername(User user) {
                        return new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
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
        ((MySession) Session.get()).setuId(1);
        this.tester.startPage(Albums.class);
        this.tester.getSession().setLocale(new Locale("en", "EN"));
    }

    @Test
    public void testRendered() {
        tester.assertRenderedPage(Albums.class);
        tester.assertVisible("signout");
    }

    @Test
    public void testCreateNull() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("AlbumName", "");
        formTester.submit();
        this.tester.assertErrorMessages("'Album name' is required.");
    }

    @Test
    public void testCreateAlbumExist() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("AlbumName", ALBUM_NAME_EXIST);
        formTester.submit();
        this.tester.assertErrorMessages();
    }

    @Test
    public void testCreateAlbum() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("AlbumName", ALBUM_NAME_NOT_EXIST);
        formTester.submit();
        this.tester.assertNoErrorMessage();
        tester.assertRenderedPage(Albums.class);
    }

    @Test
    public void testCreateAlbumWithError() {
        FormTester formTester = this.tester.newFormTester("form");
        formTester.setValue("AlbumName", "casca");
        formTester.submit();
        this.tester.assertErrorMessages("Album with this name already exist");
    }
}
