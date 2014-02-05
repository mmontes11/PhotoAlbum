package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.FileDao;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileDao fileDao;

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
    public void CreateDeleteLikes() {
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

        // Create
        Comment comment1 = new Comment(null, "bonito album", user);
        CommentAlbum commentAl1 = new CommentAlbum(null, comment1, album);
        commentService.create(commentAl1);
        Likefield likeL = new Likefield(null, user, 1);

        LikeComment likeComment1 = new LikeComment(null, likeL, comment1);
        likeService.create(likeComment1);
        assertEquals(likeComment1,
                likeService.getLikeCommentByCommentId(comment1.getId()).get(0));

        LikeFile likeFile1 = new LikeFile(null, likeL, file1);
        likeService.create(likeFile1);
        assertEquals(likeFile1, likeService.getLikeFileByFileId(file1.getId())
                .get(0));

        LikeAlbum likeAlbum1 = new LikeAlbum(null, likeL, album);
        likeService.create(likeAlbum1);
        assertEquals(likeAlbum1,
                likeService.getLikeAlbumsByAlbumId(album.getId()).get(0));

        // Delete
        likeService.delete(likeComment1);
        likeService.delete(likeFile1);
        likeService.delete(likeAlbum1);

        assertTrue(likeService.getLikeCommentByCommentId(comment1.getId())
                .isEmpty());
        assertTrue(likeService.getLikeFileByFileId(file1.getId()).isEmpty());
        assertTrue(likeService.getLikeAlbumsByAlbumId(album.getId()).isEmpty());
    }

    @Test
    public void LikeAlbumGetters() {
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

        Likefield like = new Likefield(null, user, 1);

        LikeAlbum likeAlbum = new LikeAlbum(null, like, album);
        likeService.create(likeAlbum);

        List<LikeAlbum> likesAlbum = likeService.getLikeAlbumsByAlbumId(album
                .getId());
        assertTrue(likesAlbum.contains(likeAlbum));

        likesAlbum = likeService.getLikesAlbum(album.getId(), 1);
        assertTrue(likesAlbum.contains(likeAlbum));

        LikeAlbum l = likeService.getLikeAlbumByUserAlbum(user.getId(),
                album.getId());
        assertEquals(likeAlbum, l);
    }

    @Test
    public void LikeCommentGetters() {
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

        Likefield like = new Likefield(null, user, 1);

        Comment comment = new Comment(null, "bonito album", user);
        CommentAlbum commentAl = new CommentAlbum(null, comment, album);
        commentService.create(commentAl);

        LikeComment likeComment = new LikeComment(null, like, comment);
        likeService.create(likeComment);

        List<LikeComment> likesComment = likeService
                .getLikeCommentByCommentId(comment.getId());
        assertTrue(likesComment.contains(likeComment));

        likesComment = likeService.getLikesComment(comment.getId(), 1);

        LikeComment l = likeService.getLikeCommentByUserComment(user.getId(),
                comment.getId());
        assertEquals(likeComment, l);
    }

    @Test
    public void LikeFileGetters() {
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

        Likefield like = new Likefield(null, user, 1);

        LikeFile likeFile1 = new LikeFile(null, like, file1);
        likeService.create(likeFile1);

        List<LikeFile> likesFile = likeService.getLikeFileByFileId(file1
                .getId());
        assertTrue(likesFile.contains(likeFile1));

        likesFile = likeService.getLikesFile(file1.getId(), 1);
        assertTrue(likesFile.contains(likeFile1));

        LikeFile l = likeService.getLikeFileByUserFile(user.getId(),
                file1.getId());
        assertEquals(likeFile1, l);
    }

    @Test
    public void UpdateLikeTest() {
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

        Likefield like = new Likefield(null, user, 1);

        LikeAlbum likeAlbum = new LikeAlbum(null, like, album);
        likeService.create(likeAlbum);

        likeService.updateLikeDislike(like.getId(), 0);

        likeAlbum = likeService.getLikeAlbumByUserAlbum(user.getId(),
                album.getId());

        assertEquals(likeAlbum.getLike().getMegusta(), 0);
    }
}
