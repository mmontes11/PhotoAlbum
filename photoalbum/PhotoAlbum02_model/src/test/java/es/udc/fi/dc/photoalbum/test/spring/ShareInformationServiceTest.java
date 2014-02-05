package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ShareInformationServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShareInformationAlbumsService shareInformationAlbumsService;
    @Autowired
    private ShareInformationPhotosService shareInformationPhotosService;

    /**
     * Method setUpJndi.
     * 
     * @throws NamingException
     */
    @BeforeClass
    public static void setUpJndi() throws NamingException {
        PoolProperties p = new PoolProperties();
        // p.setUrl("jdbc:h2:~/H2/PhotoAlbum;INIT=RUNSCRIPT FROM 'classpath:db_create.sql'");
        p.setUrl("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:db_create.sql';DB_CLOSE_DELAY=-1");
        p.setDriverClassName("org.h2.Driver");
        p.setUsername("herramientas");
        p.setPassword("desarrollo2013");
        DataSource ds = new DataSource();
        ds.setPoolProperties(p);
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder
                .emptyActivatedContextBuilder();
        builder.bind("java:comp/env/jdbc/PhotoAlbum", ds);
    }

    @Test
    public void testShareAlbum() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("45678"));
        userService.create(user2);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        File file1 = new File(null, "file", new byte[] {}, new byte[] {}, album);
        fileService.create(file1);
        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album);
        fileService.create(file2);
        File file3 = new File(null, "file3", new byte[] {}, new byte[] {},
                album);
        fileService.create(file3);

        Album album2 = new Album(null, "album2", user2, null, null);
        albumService.create(album2);
        File file4 = new File(null, "file4", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file4);
        File file5 = new File(null, "file5", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file5);
        File file6 = new File(null, "file6", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file6);

        HashSet<File> listaFiles1 = new HashSet<File>();
        HashSet<File> listaFiles2 = new HashSet<File>();
        listaFiles1.add(file1);
        listaFiles1.add(file2);
        listaFiles1.add(file3);
        album.setFiles(listaFiles1);
        albumService.create(album);
        listaFiles2.add(file4);
        listaFiles2.add(file5);
        listaFiles2.add(file6);
        album2.setFiles(listaFiles2);
        albumService.create(album2);

        // Albums
        ShareInformationAlbums shareAl1 = new ShareInformationAlbums(null,
                album, user2);

        shareInformationAlbumsService.create(shareAl1);
        assertTrue(shareInformationAlbumsService.alreadyShared(album,
                user2.getId()));

        assertTrue(shareInformationAlbumsService.getUserShares(user2.getId())
                .contains(shareAl1));

        assertEquals(
                shareInformationAlbumsService.getShare(album.getId(),
                        user2.getId(), user.getEmail()), shareAl1);

        assertTrue(shareInformationAlbumsService.getShares(user, user2)
                .contains(shareAl1));

        assertTrue(shareInformationAlbumsService.getAlbumShares(album.getId())
                .contains(shareAl1));

        shareInformationAlbumsService.delete(shareAl1);
        assertFalse(shareInformationAlbumsService.alreadyShared(album,
                user2.getId()));

    }

    @Test
    public void testSharePhotos() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("45678"));
        userService.create(user2);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        File file1 = new File(null, "file", new byte[] {}, new byte[] {}, album);
        fileService.create(file1);
        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album);
        fileService.create(file2);
        File file3 = new File(null, "file3", new byte[] {}, new byte[] {},
                album);
        fileService.create(file3);

        Album album2 = new Album(null, "album2", user2, null, null);
        albumService.create(album2);
        File file4 = new File(null, "file4", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file4);
        File file5 = new File(null, "file5", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file5);
        File file6 = new File(null, "file6", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file6);

        HashSet<File> listaFiles1 = new HashSet<File>();
        HashSet<File> listaFiles2 = new HashSet<File>();
        listaFiles1.add(file1);
        listaFiles1.add(file2);
        listaFiles1.add(file3);
        album.setFiles(listaFiles1);
        albumService.create(album);
        listaFiles2.add(file4);
        listaFiles2.add(file5);
        listaFiles2.add(file6);
        album2.setFiles(listaFiles2);
        albumService.create(album2);

        // Photos
        ShareInformationPhotos sharePh1 = new ShareInformationPhotos(null,
                file1, user2);
        shareInformationPhotosService.create(sharePh1);

        assertEquals(
                shareInformationPhotosService.getShare(file1.getId(),
                        user2.getId(), user.getEmail()), sharePh1);

        assertTrue(shareInformationPhotosService.getPhotosShares(file1.getId())
                .contains(sharePh1));

        assertTrue(shareInformationPhotosService.getPhotosShares(album.getId(),
                user2.getId()).contains(sharePh1));

        assertEquals(shareInformationPhotosService.getShareInformation(
                file1.getId(), user2.getId()), sharePh1);

        shareInformationPhotosService.delete(sharePh1);

        assertNull(shareInformationPhotosService.getShare(file1.getId(),
                user2.getId(), user.getEmail()));

        assertTrue(shareInformationPhotosService.getPhotosShares(file1.getId())
                .isEmpty());

        assertTrue(shareInformationPhotosService.getPhotosShares(album.getId(),
                user2.getId()).isEmpty());

        assertNull(shareInformationPhotosService.getShareInformation(
                file1.getId(), user2.getId()));

    }

}
