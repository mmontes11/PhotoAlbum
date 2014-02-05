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
import es.udc.fi.dc.photoalbum.hibernate.AlbumDao;
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class AlbumServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumDao albumDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ShareInformationAlbumsService shareInformationAlbumsService;
    @Autowired
    private TagService tagService;

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
    public void testCreateDeleteGetAlbum() {
        User user = new User(null, "abc", "123", MD5.getHash("pass"));
        userService.create(user);
        Album album = new Album(null, "new", user, null, null);
        albumService.create(album);
        assertNotNull(albumService.getAlbum(album.getName(), user.getId()));
        assertNotNull(albumService.getById(album.getId()));

        albumService.delete(album);

        assertNull(albumService.getAlbum(album.getName(), user.getId()));
        assertNull(albumService.getById(album.getId()));
    }

    @Test
    public void testRenameAlbum() {
        User user = new User(null, "abc", "123", MD5.getHash("pass"));
        userService.create(user);
        Album album = new Album(null, "new", user, null, null);
        albumService.create(album);

        albumService.rename(album, "new");

        assertEquals(albumService.getById(album.getId()).getName(), "new");
    }

    @Test
    public void tesGetAlbums() {
        User user = new User(null, "abc", "123", MD5.getHash("pass"));
        userService.create(user);
        Album album1 = new Album(null, "album1", user, null, null);
        albumService.create(album1);
        Album album2 = new Album(null, "album2", user, null, null);
        albumService.create(album2);

        ArrayList<Album> list = new ArrayList<>();
        list.add(album1);
        list.add(album2);

        ArrayList<Album> resultList = albumService.getAlbums(user.getId());
        assertTrue(resultList.contains(album1));
        assertTrue(resultList.contains(album2));
    }

    @Test
    public void testSearchAlbums() {
        User user = new User(null, "abc", "123", MD5.getHash("pass"));
        userService.create(user);
        Album album1 = new Album(null, "album1", user, new HashSet<File>(),
                null);
        albumService.create(album1);
        Album album2 = new Album(null, "album2", user, new HashSet<File>(),
                null);
        albumService.create(album2);
        Album album3 = new Album(null, "album3", user, new HashSet<File>(),
                null);
        albumService.create(album3);
        ShareInformationAlbums shareAl1 = new ShareInformationAlbums(null,
                album1, userService.getById(Integer.valueOf(1)));
        shareInformationAlbumsService.create(shareAl1);
        ShareInformationAlbums shareAl2 = new ShareInformationAlbums(null,
                album2, userService.getById(Integer.valueOf(1)));
        shareInformationAlbumsService.create(shareAl2);
        ShareInformationAlbums shareAl3 = new ShareInformationAlbums(null,
                album3, userService.getById(Integer.valueOf(1)));
        shareInformationAlbumsService.create(shareAl3);

        List<Album> albums = albumDao.searchPublicAlbums("1", null, null, null,
                null);
        assertTrue(albums.contains(album1));
        assertFalse(albums.contains(album2));
        assertFalse(albums.contains(album3));

        Comment comment1 = new Comment(null, "cool", user);
        Comment comment2 = new Comment(null, "ugly", user);

        CommentAlbum commentAlbum1 = new CommentAlbum(null, comment1, album1);
        commentService.create(commentAlbum1);
        CommentAlbum commentAlbum2 = new CommentAlbum(null, comment2, album2);
        commentService.create(commentAlbum2);

        albums = albumDao.searchPublicAlbums(null, "cO", null, null, null);
        assertTrue(albums.contains(album1));
        assertFalse(albums.contains(album2));
        assertFalse(albums.contains(album3));

        Tag t1 = new Tag(null, "paisajes");
        tagService.create(t1);
        Tag t2 = new Tag(null, "cositas");
        tagService.create(t2);
        TagAlbum ta1 = new TagAlbum(null, t1, album1);
        tagService.create(ta1);
        TagAlbum ta2 = new TagAlbum(null, t2, album2);
        tagService.create(ta2);

        albums = albumDao
                .searchPublicAlbums(null, null, "paisajes", null, null);
        assertFalse(albums.contains(album2));
        assertFalse(albums.contains(album3));

        albums = albumDao.searchPublicAlbums(null, null, "coS", null, null);
        assertFalse(albums.contains(album1));
        assertTrue(albums.contains(album2));
        assertFalse(albums.contains(album3));

    }
}
