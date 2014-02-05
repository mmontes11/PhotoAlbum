package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
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
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;

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
    public void CreateDeleteComments() {
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

        Comment comment1 = new Comment(null, "bonito album", user);
        Comment comment2 = new Comment(null, "bonita foto", user);

        CommentAlbum commentAlbum = new CommentAlbum(null, comment1, album);
        commentService.create(commentAlbum);

        CommentFile commentFile = new CommentFile(null, comment2, file1);
        commentService.create(commentFile);

        assertEquals(comment1, commentService.getCommentById(comment1.getId()));
        assertEquals(comment2, commentService.getCommentById(comment2.getId()));
        assertEquals(commentAlbum,
                commentService.geCommentAlbumById(commentAlbum.getId()));
        assertEquals(commentFile,
                commentService.getCommentFileById(commentFile.getId()));

        commentService.delete(commentAlbum);
        commentService.delete(commentFile);

        assertEquals(null, commentService.getCommentById(comment1.getId()));
        assertEquals(null, commentService.getCommentById(comment2.getId()));
        assertEquals(null,
                commentService.geCommentAlbumById(commentAlbum.getId()));
        assertEquals(null,
                commentService.getCommentFileById(commentFile.getId()));

    }

    @Test
    public void UpdateCommentTest() {
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

        Comment comment1 = new Comment(null, "bonito album", user);

        CommentAlbum commentAlbum = new CommentAlbum(null, comment1, album);
        commentService.create(commentAlbum);

        String newTextComment = "vaya mierda de album";

        commentService.updateCommentText(comment1.getId(), newTextComment);

        assertEquals(commentService.getCommentById(comment1.getId())
                .getCommentText(), newTextComment);
    }

    @Test
    public void CommentAlbumGetsTest() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("12345"));
        userService.create(user2);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);

        Comment comment1 = new Comment(null, "que bonito es mi album", user);
        Comment comment2 = new Comment(null, "joder con el puto album", user2);
        Comment comment3 = new Comment(null, "vaya mierda de album", user2);

        CommentAlbum commentAlbum = new CommentAlbum(null, comment1, album);
        commentService.create(commentAlbum);
        CommentAlbum commentAlbum2 = new CommentAlbum(null, comment2, album);
        commentService.create(commentAlbum2);
        CommentAlbum commentAlbum3 = new CommentAlbum(null, comment3, album);
        commentService.create(commentAlbum3);

        List<CommentAlbum> resultList = commentService
                .getCommentAlbumsByAlbum(album.getId());
        List<CommentAlbum> commentAlbumList = new ArrayList<CommentAlbum>();
        commentAlbumList.add(commentAlbum);
        commentAlbumList.add(commentAlbum2);
        commentAlbumList.add(commentAlbum3);
        assertTrue(resultList.containsAll(commentAlbumList));

        resultList = commentService.getCommentAlbumsByUser(user2.getId());
        commentAlbumList.clear();
        commentAlbumList.add(commentAlbum2);
        commentAlbumList.add(commentAlbum3);
        assertTrue(resultList.containsAll(commentAlbumList));

        resultList = commentService.getCommentAlbumsByAlbumUser(album.getId(),
                user2.getId());
        commentAlbumList.clear();
        commentAlbumList.add(commentAlbum2);
        commentAlbumList.add(commentAlbum3);
        assertTrue(resultList.containsAll(commentAlbumList));
    }

    @Test
    public void CommentFileGetsTest() {
        User user = new User(null, "username", "email@email.es",
                MD5.getHash("12345"));
        userService.create(user);
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("12345"));
        userService.create(user2);

        Album album = new Album(null, "album", user, null, null);
        albumService.create(album);
        File file = new File(null, "file", new byte[] {}, new byte[] {}, album);
        fileService.create(file);

        Comment comment1 = new Comment(null, "que bonita es mi foto", user);
        Comment comment2 = new Comment(null, "joder con la puta foto", user2);
        Comment comment3 = new Comment(null, "vaya mierda de foto", user2);

        CommentFile commentFile = new CommentFile(null, comment1, file);
        commentService.create(commentFile);
        CommentFile commentFile2 = new CommentFile(null, comment2, file);
        commentService.create(commentFile2);
        CommentFile commentFile3 = new CommentFile(null, comment3, file);
        commentService.create(commentFile3);

        List<CommentFile> resultList = commentService
                .getCommentFilesByFile(file.getId());
        List<CommentFile> commentFileList = new ArrayList<CommentFile>();
        commentFileList.add(commentFile);
        commentFileList.add(commentFile2);
        commentFileList.add(commentFile3);
        assertTrue(resultList.containsAll(commentFileList));

        resultList = commentService.getCommentFilesByUser(user2.getId());
        commentFileList.clear();
        commentFileList.add(commentFile2);
        commentFileList.add(commentFile3);
        assertTrue(resultList.containsAll(commentFileList));

        resultList = commentService.getCommentFileByFileUser(file.getId(),
                user2.getId());
        commentFileList.clear();
        commentFileList.add(commentFile2);
        commentFileList.add(commentFile3);
        assertTrue(resultList.containsAll(commentFileList));
    }
}
