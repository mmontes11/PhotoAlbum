package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.FileDao;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
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
public class FileServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagService tagService;
    @Autowired
    private FileDao fileDao;
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
    public void testFiles() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
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
        Album album2 = new Album(null, "album2", user, null, null);
        albumService.create(album2);

        assertNotNull(fileService.getById(file1.getId()));
        assertNotNull(fileService.getById(file2.getId()));
        assertNotNull(fileService.getById(file3.getId()));

        fileService.delete(file3);

        assertNull(fileService.getById(file3.getId()));

        ArrayList<File> files = fileService.getAlbumFiles(album.getId());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(!files.contains(file3));

        for (int i = 3; i <= 30; i++) {
            fileService.create(new File(null, "file" + String.valueOf(i),
                    new byte[] {}, new byte[] {}, album));
        }

        assertEquals(Long.valueOf(30L),
                fileService.getCountAlbumFiles(album.getId()));
        files = fileService.getAlbumFilesPaging(album.getId(), 0, 10);
        assertEquals(10, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(!files.contains(file3));

        fileService.changeAlbum(file1, album2);
        assertEquals(file1.getAlbum(), album2);
    }

    @Test
    public void testUserFiles() {
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
        HashSet<File> listaFiles2 = new HashSet<File>();
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

        listaFiles2.add(file4);
        listaFiles2.add(file5);
        listaFiles2.add(file6);
        album2.setFiles(listaFiles2);
        albumService.create(album2);

        shareInformationAlbumsService.create(new ShareInformationAlbums(null,
                album2, user));

        assertEquals(
                file1,
                fileService.getFileOwn(file1.getId(), album.getName(),
                        user.getId()));
        assertEquals(
                file4,
                fileService.getFileShared(file4.getId(), album2.getName(),
                        user.getId()));
    }

    @Test
    public void searchPublicFiles() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        File file1 = new File(null, "file1", new byte[] {}, new byte[] {},
                album);
        fileService.create(file1);
        shareInformationPhotosService.create(new ShareInformationPhotos(null,
                file1, userService.getById(Integer.valueOf(1))));
        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album);
        fileService.create(file2);
        shareInformationPhotosService.create(new ShareInformationPhotos(null,
                file2, userService.getById(Integer.valueOf(1))));
        File file3 = new File(null, "file3", new byte[] {}, new byte[] {},
                album);
        fileService.create(file3);
        shareInformationPhotosService.create(new ShareInformationPhotos(null,
                file3, userService.getById(Integer.valueOf(1))));
        HashSet<File> hs = new HashSet<File>();
        hs.add(file1);
        hs.add(file2);
        hs.add(file3);
        album.setFiles(hs);
        albumService.create(album);

        List<File> files = fileDao.searchPublicFiles("file", null, null, null,
                null);

        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));

        files = fileDao.searchPublicFiles("1", null, null, null, null);
        assertTrue(files.contains(file1));
        assertFalse(files.contains(file2));
        assertFalse(files.contains(file3));

        Comment comment1 = new Comment(null, "cool", user);
        Comment comment2 = new Comment(null, "ugly", user);
        CommentFile commentFile1 = new CommentFile(null, comment1, file1);
        CommentFile commentFile2 = new CommentFile(null, comment2, file1);
        commentService.create(commentFile1);
        commentService.create(commentFile2);

        files = fileDao.searchPublicFiles(null, "cO", null, null, null);
        assertTrue(files.contains(file1));
        assertFalse(files.contains(file2));
        assertFalse(files.contains(file3));

        Tag t1 = new Tag(null, "paisajes");
        tagService.create(t1);
        Tag t2 = new Tag(null, "cosas");
        tagService.create(t2);
        TagFile tf1 = new TagFile(null, t1, file1);
        tagService.create(tf1);
        TagFile tf2 = new TagFile(null, t2, file2);
        tagService.create(tf2);

        files = fileDao.searchPublicFiles(null, null, "pAi", null, null);

        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));

        files = fileDao.searchPublicFiles(null, null, "Cos", null, null);

        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));
        assertTrue(files.contains(file3));

    }
}
