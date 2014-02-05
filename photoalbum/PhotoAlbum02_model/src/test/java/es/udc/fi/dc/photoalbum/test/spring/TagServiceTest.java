package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TagServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShareInformationPhotosService shareInformationService;

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
    public void CreateDeleteTagAlbums() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        File file1 = new File(null, "file", new byte[] {}, new byte[] {}, album);
        fileService.create(file1);
        HashSet<File> hs = new HashSet<File>();
        hs.add(file1);
        album.setFiles(hs);
        albumService.create(album);

        Tag t = new Tag(null, "tag");
        tagService.create(t);

        TagAlbum ta = new TagAlbum(null, t, album);
        tagService.create(ta);

        assertEquals(t, tagService.getTagById(t.getId()));
        assertEquals(t, tagService.getTagbyName("tag"));
        assertEquals(ta, tagService.getTagAlbum(ta.getId()));
        assertEquals(ta, tagService.getTagsByAbumId(album.getId()).get(0));

        TagFile tf = tagService.getTagsByFileId(file1.getId()).get(0);
        assertNotNull(tf);
        assertEquals(tf, tagService.getTagFile(tf.getId()));

        tagService.delete(ta);

        assertEquals(null, tagService.getTagById(t.getId()));
        assertEquals(null, tagService.getTagbyName("tag"));
        assertEquals(null, tagService.getTagAlbum(ta.getId()));
        assertTrue(tagService.getTagsByAbumId(album.getId()).isEmpty());

        assertTrue(tagService.getTagsByFileId(file1.getId()).isEmpty());
    }

    @Test
    public void AutoDeleteTagWithoutReferencesTest() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);

        Tag t2 = new Tag(null, "tag2");
        tagService.create(t2);

        Album album2 = new Album(null, "album2", user, null, null);
        albumService.create(album2);

        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file2);

        HashSet<File> hs2 = new HashSet<File>();
        hs2.add(file2);
        album2.setFiles(hs2);
        albumService.create(album2);

        TagFile tf2 = new TagFile(null, t2, file2);
        tagService.create(tf2);

        assertEquals(t2, tagService.getTagsByFileId(file2.getId()).get(0)
                .getTag());
        tagService.delete(tf2);

        assertEquals(null, tagService.getTagById(t2.getId()));
        assertEquals(null, tagService.getTagbyName("tag2"));

    }

    @Test
    public void GetAlbumFilesByTag() {
        // USER 1
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        Album album2 = new Album(null, "album2", user, null, null);
        albumService.create(album2);
        File file1 = new File(null, "file1", new byte[] {}, new byte[] {},
                album);
        fileService.create(file1);
        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album);
        fileService.create(file2);
        File file3 = new File(null, "file3", new byte[] {}, new byte[] {},
                album);
        fileService.create(file3);
        File file4 = new File(null, "file4", new byte[] {}, new byte[] {},
                album);
        fileService.create(file4);
        File file5 = new File(null, "file5", new byte[] {}, new byte[] {},
                album2);
        fileService.create(file5);
        HashSet<File> hs = new HashSet<File>();
        hs.add(file1);
        hs.add(file2);
        hs.add(file3);
        hs.add(file4);
        album.setFiles(hs);
        albumService.create(album);

        HashSet<File> hs2 = new HashSet<File>();
        hs2.add(file5);
        album2.setFiles(hs);
        albumService.create(album2);

        // USER2
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("12345"));
        userService.create(user2);

        ShareInformationPhotos sip = new ShareInformationPhotos(null, file1,
                user2);
        shareInformationService.create(sip);
        ShareInformationPhotos sip2 = new ShareInformationPhotos(null, file2,
                user2);
        shareInformationService.create(sip2);
        ShareInformationPhotos sip3 = new ShareInformationPhotos(null, file3,
                user2);
        shareInformationService.create(sip3);

        Tag t = new Tag(null, "tag");
        tagService.create(t);
        TagAlbum ta = new TagAlbum(null, t, album);
        tagService.create(ta);
        // tagService.delete(tagService.getTagFile(file4.getId()));
        TagFile tf5 = new TagFile(null, t, file5);
        tagService.create(tf5);

        // Album2 tiene la etiqueta pero user2 no tiene acceso
        ArrayList<Album> albumes = tagService.getAlbumes(t.getId(),
                user2.getId());
        assertTrue(albumes.contains(album));
        assertFalse(albumes.contains(album2));

        // Le damos permisos a File5
        ShareInformationPhotos sip5 = new ShareInformationPhotos(null, file5,
                user2);
        shareInformationService.create(sip5);

        // File4 no tiene la etiqueta, File5 no está en album pero tiene la
        // etiqueta
        ArrayList<File> files = tagService.getFiles(t.getId(), album.getId(),
                user2.getId());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));
        assertFalse(files.contains(file4));
        assertFalse(files.contains(file5));

        // File4 no tiene la etiqueta pero File5 sí, ese a estar en otro albumn
        files = tagService.getAllFiles(t.getId(), user2.getId());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));
        assertFalse(files.contains(file4));
        assertTrue(files.contains(file5));
    }
}
