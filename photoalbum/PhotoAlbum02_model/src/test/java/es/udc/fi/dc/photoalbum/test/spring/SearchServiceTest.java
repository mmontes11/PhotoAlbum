package es.udc.fi.dc.photoalbum.test.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
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
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.SearchService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.FileDTO;
import es.udc.fi.dc.photoalbum.utils.MD5;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShareInformationPhotosService shareInformationPhotoService;

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
    public void searchTest() {
        User user1 = new User(null, "username1", "email1@email.es",
                MD5.getHash("12345"));
        userService.create(user1);
        User user2 = new User(null, "username2", "email2@email.es",
                MD5.getHash("12345"));
        userService.create(user2);
        User user3 = new User(null, "username3", "email3@email.es",
                MD5.getHash("12345"));
        userService.create(user3);
        User user4 = new User(null, "username4", "email4@email.es",
                MD5.getHash("12345"));
        userService.create(user4);
        User user5 = new User(null, "username5", "email5@email.es",
                MD5.getHash("12345"));
        userService.create(user5);

        Album album = new Album(null, "album", user1, null, null);
        albumService.create(album);

        Likefield like1 = new Likefield(null, user1, 1);
        Likefield like2 = new Likefield(null, user2, 1);
        Likefield like3 = new Likefield(null, user3, 1);
        Likefield like4 = new Likefield(null, user4, 1);
        Likefield like5 = new Likefield(null, user5, 1);

        File file1 = new File(null, "file1", new byte[] {}, new byte[] {},
                album);
        fileService.create(file1);
        // Compartir con public
        shareInformationPhotoService.create(new ShareInformationPhotos(null,
                file1, userService.getById(Integer.valueOf(1))));

        File file2 = new File(null, "file2", new byte[] {}, new byte[] {},
                album);
        fileService.create(file2);
        shareInformationPhotoService.create(new ShareInformationPhotos(null,
                file2, userService.getById(Integer.valueOf(1))));

        File file3 = new File(null, "file3", new byte[] {}, new byte[] {},
                album);
        fileService.create(file3);
        shareInformationPhotoService.create(new ShareInformationPhotos(null,
                file3, userService.getById(Integer.valueOf(1))));

        File file4 = new File(null, "file4", new byte[] {}, new byte[] {},
                album);
        fileService.create(file4);
        shareInformationPhotoService.create(new ShareInformationPhotos(null,
                file4, userService.getById(Integer.valueOf(1))));

        File file5 = new File(null, "file5", new byte[] {}, new byte[] {},
                album);
        fileService.create(file5);
        shareInformationPhotoService.create(new ShareInformationPhotos(null,
                file5, userService.getById(Integer.valueOf(1))));

        List<FileDTO> pics = searchService.search(Integer.valueOf(5), null,
                null, null, 0, 0, 0, null, null);
        assertTrue(pics.isEmpty());

        // Likes File 1
        likeService.create(new LikeFile(null, like1, file1));

        // Likes File 2
        likeService.create(new LikeFile(null, like1, file2));
        likeService.create(new LikeFile(null, like2, file2));
        likeService.create(new LikeFile(null, like3, file2));
        likeService.create(new LikeFile(null, like4, file2));

        // Likes File 3
        likeService.create(new LikeFile(null, like1, file3));
        likeService.create(new LikeFile(null, like2, file3));
        likeService.create(new LikeFile(null, like3, file3));

        // Likes File 4
        likeService.create(new LikeFile(null, like1, file4));
        likeService.create(new LikeFile(null, like2, file4));
        likeService.create(new LikeFile(null, like3, file4));

        // File 5 has no likes (4ever alone)
        pics = searchService.search(Integer.valueOf(5), null, null, null, 0, 0,
                0, null, null);
        assertEquals(pics.size(), 4);
        assertEquals(pics.get(0).getId(), file2.getId());
        assertEquals(pics.get(1).getId(), file3.getId());
        assertEquals(pics.get(2).getId(), file4.getId());
        assertEquals(pics.get(3).getId(), file1.getId());

        // Likes File 5
        likeService.create(new LikeFile(null, like5, file5));

        pics = searchService
                .search(null, null, null, null, 0, 0, 0, null, null);
        assertEquals(pics.size(), 5);
        assertEquals(pics.get(0).getId(), file2.getId());
        assertEquals(pics.get(1).getId(), file3.getId());
        assertEquals(pics.get(2).getId(), file4.getId());
        assertEquals(pics.get(3).getId(), file1.getId());
        assertEquals(pics.get(4).getId(), file5.getId());

        pics = searchService.search(Integer.valueOf(5), "1", null, null, 0, 0,
                0, null, null);
        assertEquals(pics.size(), 1);
        assertEquals(pics.get(0).getId(), file1.getId());

        // Order by date
        pics = searchService
                .search(null, null, null, null, 0, 0, 1, null, null);
        assertEquals(pics.size(), 5);
        assertEquals(pics.get(0).getId(), file5.getId());
        assertEquals(pics.get(1).getId(), file4.getId());
        assertEquals(pics.get(2).getId(), file3.getId());
        assertEquals(pics.get(3).getId(), file2.getId());
        assertEquals(pics.get(4).getId(), file1.getId());

        // Between dates
        Calendar date = Calendar.getInstance();
        Calendar dateBefore = Calendar.getInstance();
        Calendar dateAfter = Calendar.getInstance();
        date.set(2013, 12, 21);
        dateBefore.set(2013, 12, 1);
        dateAfter.set(2013, 12, 27);

        file1.setFileDate(date);
        fileService.create(file1);
        file2.setFileDate(dateAfter);
        fileService.create(file2);
        file3.setFileDate(date);
        fileService.create(file3);
        file4.setFileDate(dateBefore);
        fileService.create(file4);
        file5.setFileDate(date);
        fileService.create(file5);

        Calendar initDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        initDate.set(2013, 12, 7);
        endDate.set(2013, 12, 25);

        // Between two dates
        pics = searchService.search(10, null, null, null, 0, 0, 0, initDate,
                endDate);
        assertEquals(pics.size(), 3);
        assertEquals(pics.get(0).getId(), file3.getId());
        assertEquals(pics.get(1).getId(), file1.getId());
        assertEquals(pics.get(2).getId(), file5.getId());

        // After a date
        pics = searchService.search(10, null, null, null, 0, 0, 0, initDate,
                null);
        assertEquals(pics.size(), 4);
        assertEquals(pics.get(0).getId(), file2.getId());
        assertEquals(pics.get(1).getId(), file3.getId());
        assertEquals(pics.get(2).getId(), file1.getId());
        assertEquals(pics.get(3).getId(), file5.getId());

        // Before a date
        pics = searchService.search(10, null, null, null, 0, 0, 0, null,
                endDate);
        assertEquals(pics.size(), 4);
        assertEquals(pics.get(0).getId(), file3.getId());
        assertEquals(pics.get(1).getId(), file4.getId());
        assertEquals(pics.get(2).getId(), file1.getId());
        assertEquals(pics.get(3).getId(), file5.getId());

    }
}
