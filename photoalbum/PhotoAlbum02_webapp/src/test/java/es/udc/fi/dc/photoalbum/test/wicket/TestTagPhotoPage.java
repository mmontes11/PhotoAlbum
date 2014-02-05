package es.udc.fi.dc.photoalbum.test.wicket;

import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.ALBUM_NAME_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.FILE_NAME_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.TAG_NAME_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_EMAIL_EXIST;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_PASS_YES;
import static es.udc.fi.dc.photoalbum.test.wicket.ConstantsForTests.USER_USERNAME_EXIST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.WicketApp;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.TagPhoto;

/**
 */
public class TestTagPhotoPage {
    private WicketApp wicketApp;
    private WicketTester tester;
    private User user = new User(1, USER_USERNAME_EXIST, USER_EMAIL_EXIST,
            USER_PASS_YES);

    private Album album = new Album(1, ALBUM_NAME_EXIST, user,
            new HashSet<File>(), null);
    private File file = new File(1, FILE_NAME_EXIST, new byte[20],
            new byte[10], album);
    private Tag tag = new Tag(1, TAG_NAME_EXIST);
    private Set<File> set = new HashSet<File>();

    {
        this.wicketApp = new WicketApp() {
            @Override
            protected void init() {
                ApplicationContextMock context = new ApplicationContextMock();
                AlbumService mockAlbum = new AlbumService() {
                    public void rename(Album album, String newName) {
                    }

                    public Album getAlbum(String name, int userId) {
                        return album;
                    }

                    public void delete(Album album) {
                    }

                    public void create(Album album) {
                    }

                    public Album getById(Integer id) {
                        return album;
                    }

                    public ArrayList<Album> getAlbums(Integer id) {
                        ArrayList<Album> list = new ArrayList<Album>();
                        list.add(album);
                        return list;
                    }
                };
                FileService mockFile = new FileService() {
                    public void create(File file) {
                    }

                    public void delete(File file) {
                    }

                    public File getFileOwn(int id, String name, int userId) {
                        User user = new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                        album = new Album(1, ALBUM_NAME_EXIST, user, null, null);
                        File file = new File(1, "1", new byte[1], new byte[1],
                                album);
                        set.add(file);
                        album.setFiles(set);
                        return file;
                    }

                    public File getFileShared(int id, String name, int userId) {
                        return null;
                    }

                    public void changeAlbum(File file, Album album) {
                    }

                    public File getById(Integer id) {
                        return file;
                    }

                    public ArrayList<File> getAlbumFiles(int albumId) {
                        ArrayList<File> list = new ArrayList<File>();
                        User user = new User(1, USER_USERNAME_EXIST,
                                USER_EMAIL_EXIST, USER_PASS_YES);
                        album = new Album(1, ALBUM_NAME_EXIST, user, null, null);
                        File file = new File(1, "1", new byte[1], new byte[1],
                                album);
                        set.add(file);
                        album.setFiles(set);
                        list.add(file);
                        return list;
                    }

                    public ArrayList<File> getAlbumFilesPaging(int albumId,
                            int first, int count) {
                        return null;
                    }

                    public Long getCountAlbumFiles(int albumId) {
                        return 0L;
                    }

                    @Override
                    public File find(int id) throws InstanceNotFoundException {
                        return null;
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
                        return user;
                    }

                    public User getById(Integer id) {
                        return user;
                    }

                    @Override
                    public User getByEmail(User userEmail) {
                        return user;
                    }

                    @Override
                    public User getByUsername(User user) {
                        return user;
                    }
                };
                TagService mockTagFile = new TagService() {

                    @Override
                    public void create(Tag newTag) {

                    }

                    @Override
                    public void create(TagAlbum tag) {

                    }

                    @Override
                    public void create(TagFile tag) {

                    }

                    @Override
                    public void delete(TagAlbum tag) {

                    }

                    @Override
                    public void delete(TagFile tag) {

                    }

                    @Override
                    public Tag getTagById(int id) {
                        return tag;
                    }

                    @Override
                    public Tag getTagbyName(String name) {
                        return tag;
                    }

                    @Override
                    public TagAlbum getTagAlbum(int tagAlbumId) {
                        return null;
                    }

                    @Override
                    public TagFile getTagFile(int tagFileId) {
                        return null;
                    }

                    @Override
                    public ArrayList<TagFile> getTagsByFileId(int fileId) {
                        ArrayList<TagFile> list = new ArrayList<TagFile>();
                        return (list);
                    }

                    @Override
                    public ArrayList<TagAlbum> getTagsByAbumId(int albumId) {
                        return new ArrayList<TagAlbum>();
                    }

                    @Override
                    public ArrayList<Album> getAlbumes(int tagId, int userId) {
                        return null;
                    }

                    @Override
                    public ArrayList<File> getFiles(int tagId, int albumId,
                            int userId) {
                        ArrayList<File> list = new ArrayList<File>();
                        list.add(file);
                        return list;
                    }

                    @Override
                    public ArrayList<File> getAllFiles(int tagId, int userId) {
                        ArrayList<File> list = new ArrayList<File>();
                        list.add(file);
                        return list;
                    }

                };

                ShareInformationPhotosService mockShareFile = new ShareInformationPhotosService() {

                    @Override
                    public void create(ShareInformationPhotos sip) {

                    }

                    @Override
                    public void delete(ShareInformationPhotos sip) {

                    }

                    @Override
                    public ShareInformationPhotos getShare(int photoId,
                            int userSharedToId, String userSharedEmail) {
                        return null;
                    }

                    @Override
                    public ArrayList<ShareInformationPhotos> getPhotosShares(
                            int photoId) {
                        ArrayList<ShareInformationPhotos> list = new ArrayList<ShareInformationPhotos>();
                        return list;
                    }

                    @Override
                    public ArrayList<ShareInformationPhotos> getPhotosShares(
                            int albumId, int userId) {
                        return null;
                    }

                    @Override
                    public ShareInformationPhotos getShareInformation(
                            int photoid, int userId) {
                        return null;
                    }

                };

                ShareInformationAlbumsService mockShareAlbum = new ShareInformationAlbumsService() {

                    @Override
                    public ArrayList<ShareInformationAlbums> getUserShares(
                            int userId) {
                        return null;
                    }

                    @Override
                    public List<ShareInformationAlbums> getShares(
                            User userShared, User userSharedTo) {
                        return null;
                    }

                    @Override
                    public ShareInformationAlbums getShare(int albumId,
                            int userSharedToId) {
                        return null;
                    }

                    @Override
                    public ShareInformationAlbums getShare(int albumId,
                            int userSharedToId, String userSharedEmail) {
                        return null;
                    }

                    @Override
                    public ArrayList<ShareInformationAlbums> getAlbumShares(
                            int albumId) {
                        return new ArrayList<ShareInformationAlbums>();
                    }

                    @Override
                    public void delete(ShareInformationAlbums shareInformation) {

                    }

                    @Override
                    public void create(ShareInformationAlbums shareInformation) {

                    }

                    @Override
                    public boolean alreadyShared(Album album, int userSharedToId) {
                        return false;
                    }
                };

                CommentService mockComment = new CommentService() {

                    @Override
                    public Comment getCommentById(int commentId) {
                        return null;
                    }

                    @Override
                    public void updateCommentText(int commentId, String newtext) {

                    }

                    @Override
                    public void create(CommentAlbum commentAlbum) {

                    }

                    @Override
                    public void delete(CommentAlbum commentAlbum) {

                    }

                    @Override
                    public CommentAlbum geCommentAlbumById(int commentAlbumId) {
                        return null;
                    }

                    @Override
                    public List<CommentAlbum> getCommentAlbumsByAlbum(
                            int albumId) {
                        return new ArrayList<CommentAlbum>();
                    }

                    @Override
                    public List<CommentAlbum> getCommentAlbumsByUser(int userId) {
                        return new ArrayList<CommentAlbum>();
                    }

                    @Override
                    public List<CommentAlbum> getCommentAlbumsByAlbumUser(
                            int albumId, int userId) {
                        return new ArrayList<CommentAlbum>();
                    }

                    @Override
                    public void create(CommentFile commentFile) {

                    }

                    @Override
                    public void delete(CommentFile commentFile) {

                    }

                    @Override
                    public CommentFile getCommentFileById(int commentFileId) {
                        return null;
                    }

                    @Override
                    public List<CommentFile> getCommentFilesByFile(int fileId) {
                        return new ArrayList<CommentFile>();
                    }

                    @Override
                    public List<CommentFile> getCommentFilesByUser(int userId) {
                        return null;
                    }

                    @Override
                    public List<CommentFile> getCommentFileByFileUser(
                            int fileId, int userId) {
                        return null;
                    }
                };

                LikeService mockLike = new LikeService() {

                    @Override
                    public void updateLikeDislike(int likeId, int megusta) {
                    }

                    @Override
                    public void create(LikeAlbum like) {

                    }

                    @Override
                    public void delete(LikeAlbum like) {

                    }

                    @Override
                    public List<LikeAlbum> getLikeAlbumsByAlbumId(int albumId) {
                        return null;
                    }

                    @Override
                    public List<LikeAlbum> getLikesAlbum(int albumId,
                            int megusta) {
                        return new ArrayList<LikeAlbum>();
                    }

                    @Override
                    public LikeAlbum getLikeAlbumByUserAlbum(int userId,
                            int albumId) {
                        return null;
                    }

                    @Override
                    public void create(LikeComment like) {

                    }

                    @Override
                    public void delete(LikeComment like) {

                    }

                    @Override
                    public List<LikeComment> getLikeCommentByCommentId(
                            int commentId) {
                        return null;
                    }

                    @Override
                    public List<LikeComment> getLikesComment(int commentId,
                            int megusta) {
                        return null;
                    }

                    @Override
                    public LikeComment getLikeCommentByUserComment(int userId,
                            int commentId) {
                        return null;
                    }

                    @Override
                    public void create(LikeFile like) {

                    }

                    @Override
                    public void delete(LikeFile like) {

                    }

                    @Override
                    public List<LikeFile> getLikeFileByFileId(int fileId) {
                        return null;
                    }

                    @Override
                    public List<LikeFile> getLikesFile(int fileId, int megusta) {
                        return new ArrayList<LikeFile>();
                    }

                    @Override
                    public LikeFile getLikeFileByUserFile(int userId, int fileId) {
                        return null;
                    }
                };

                context.putBean("userBean", mock);
                context.putBean("fileBean", mockFile);
                context.putBean("albumBean", mockAlbum);
                context.putBean("tagFileBean", mockTagFile);
                context.putBean("shareFileBean", mockShareFile);
                context.putBean("shareAlbumBean", mockShareAlbum);
                context.putBean("commentBean", mockComment);
                context.putBean("likeBean", mockLike);
                getComponentInstantiationListeners().add(
                        new SpringComponentInjector(this, context));
            }
        };
    }

    @Before
    public void setUp() {
        this.tester = new WicketTester(this.wicketApp);
        ((MySession) Session.get()).setuId(1);
        PageParameters pars = new PageParameters();
        // pars.add("album", ALBUM_NAME_EXIST);
        pars.add("fid", FILE_NAME_EXIST);
        Page page = new TagPhoto(pars);
        this.tester.startPage(page);
        tester.assertVisible("signout");
        this.tester.getSession().setLocale(Locale.US);
    }

    @Test
    public void testRendered() {
        tester.assertRenderedPage(TagPhoto.class);
    }
}
