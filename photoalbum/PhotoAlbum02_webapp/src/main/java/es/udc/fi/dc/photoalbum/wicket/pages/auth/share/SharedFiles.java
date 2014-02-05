package es.udc.fi.dc.photoalbum.wicket.pages.auth.share;

import java.sql.Blob;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Constant;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.CustomFileListDataProvider;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ModalEdit;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ModalLikes;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;

/**
 */
@SuppressWarnings("serial")
public class SharedFiles extends BasePageAuth {

    @SpringBean
    private UserService userService;
    @SpringBean
    private TagService tagService;
    @SpringBean
    private ShareInformationAlbumsService shareInformationAlbumsService;
    @SpringBean
    private ShareInformationPhotosService shareInformationPhotosService;
    @SpringBean
    private CommentService commentService;
    @SpringBean
    private LikeService likeService;
    private Album album;
    private User ownerUser;
    private User user;
    private PageParameters parameters;
    private String result;
    private static final int ITEMS_PER_PAGE = 10;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 120;
    private List<LikeAlbum> likes;
    private List<LikeAlbum> disLikes;

    /**
     * Constructor for SharedFiles.
     * 
     * @param parameters
     *            PageParameters
     */
    public SharedFiles(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if ((parameters.getNamedKeys().contains("album"))
                && (parameters.getNamedKeys().contains("user"))) {
            int albumId = parameters.get("album").toInt();
            String ownerEmail = parameters.get("user").toString();
            ShareInformationAlbums share = shareInformationAlbumsService
                    .getShare(albumId, ((MySession) Session.get()).getuId(),
                            ownerEmail);
            User user = new User();
            user.setEmail(ownerEmail);
            this.ownerUser = userService.getByEmail(user);
            if (this.ownerUser == null) {
                throw new RestartResponseException(ErrorPage404.class);
            }
            if (share == null) {
                share = shareInformationAlbumsService.getShare(albumId,
                        Constant.getId());
                if (share == null) {
                    throw new RestartResponseException(ErrorPage404.class);
                }
            }
            this.album = share.getAlbum();
            this.likes = likeService.getLikesAlbum(album.getId(), 1);
            this.disLikes = likeService.getLikesAlbum(album.getId(), 0);
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }
        this.user = userService.getById(((MySession) Session.get()).getuId());
        // Likes
        BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                "link", SharedFiles.class, parameters);
        like.add(new Link<Void>("like") {
            public void onClick() {
                LikeAlbum likeAlbum = likeService.getLikeAlbumByUserAlbum(
                        user.getId(), album.getId());

                if (likeAlbum == null) {
                    likeService.create(new LikeAlbum(null, new Likefield(null,
                            user, 1), album));
                    setResponsePage(new SharedFiles(parameters));
                    info(new StringResourceModel("like.send", this, null)
                            .getString());
                } else {
                    if (likeAlbum.getLike().getMegusta() == 1) {
                        error(new StringResourceModel("like.alreadyexist",
                                this, null).getString());
                    } else {
                        likeService.updateLikeDislike(likeAlbum.getLike()
                                .getId(), 1);
                        setResponsePage(new SharedFiles(parameters));
                        info(new StringResourceModel("like.send", this, null)
                                .getString());
                    }
                }
            }
        });
        add(like);

        final ArrayList<User> usersLikes = new ArrayList<User>();
        for (LikeAlbum likeAlbum : likes) {
            usersLikes.add(likeAlbum.getLike().getUser());
        }

        final ModalWindow modalLikes = new ModalWindow("modalLikes");
        add(modalLikes);
        modalLikes.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                return new ModalLikes(usersLikes, modalLikes);
            }
        });
        modalLikes.setTitle(new StringResourceModel("edit.edit", this, null));
        modalLikes.setResizable(false);
        modalLikes.setInitialWidth(WINDOW_WIDTH);
        modalLikes.setInitialHeight(WINDOW_HEIGHT);
        modalLikes
                .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                    public void onClose(AjaxRequestTarget target) {
                        setResponsePage(new SharedFiles(parameters));
                    }
                });
        AjaxLink<Void> listLikes = new AjaxLink<Void>("likes") {
            public void onClick(AjaxRequestTarget target) {
                modalLikes.show(target);
            }
        };
        listLikes.add(new Label("numLikes", likes.size()));
        add(listLikes);

        // Dislikes
        BookmarkablePageLink<Void> dislike = new BookmarkablePageLink<Void>(
                "link2", SharedFiles.class, parameters);
        dislike.add(new Link<Void>("dislike") {
            public void onClick() {
                LikeAlbum dislikeAlbum = likeService.getLikeAlbumByUserAlbum(
                        user.getId(), album.getId());

                if (dislikeAlbum == null) {
                    likeService.create(new LikeAlbum(null, new Likefield(null,
                            user, 0), album));
                    setResponsePage(new SharedFiles(parameters));
                    info(new StringResourceModel("dislike.send", this, null)
                            .getString());
                } else {
                    if (dislikeAlbum.getLike().getMegusta() == 0) {
                        error(new StringResourceModel("like.alreadyexist",
                                this, null).getString());
                    } else {
                        likeService.updateLikeDislike(dislikeAlbum.getLike()
                                .getId(), 0);
                        setResponsePage(new SharedFiles(parameters));
                        info(new StringResourceModel("dislike.send", this, null)
                                .getString());
                    }
                }
            }
        });
        add(dislike);

        final ArrayList<User> usersDisLikes = new ArrayList<User>();
        for (LikeAlbum likeAlbum : disLikes) {
            usersDisLikes.add(likeAlbum.getLike().getUser());
        }

        final ModalWindow modalDisLikes = new ModalWindow("modalDisLikes");
        add(modalDisLikes);
        modalDisLikes.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                return new ModalLikes(usersDisLikes, modalDisLikes);
            }
        });
        modalDisLikes
                .setTitle(new StringResourceModel("edit.edit", this, null));
        modalDisLikes.setResizable(false);
        modalDisLikes.setInitialWidth(WINDOW_WIDTH);
        modalDisLikes.setInitialHeight(WINDOW_HEIGHT);
        modalDisLikes
                .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                    public void onClose(AjaxRequestTarget target) {
                        setResponsePage(new SharedFiles(parameters));
                    }
                });
        AjaxLink<Void> listDisLikes = new AjaxLink<Void>("dislikes") {
            public void onClick(AjaxRequestTarget target) {
                modalDisLikes.show(target);
            }
        };
        listDisLikes.add(new Label("numDisLikes", disLikes.size()));
        add(listDisLikes);

        // Formulario
        Form<Void> form = new Form<Void>("form");
        final TextArea<String> name = new TextArea<String>("comment",
                new PropertyModel<String>(this, "result"));
        form.add(name);
        form.add(createButtonCommentOk());
        add(form);
        add(new AjaxDataView("dataContainerComment", "commentNavigator",
                createCommentsDataView()));

        add(new BookmarkablePageLink<Void>("linkBack", SharedAlbums.class,
                new PageParameters(parameters).remove("album")));
        add(new AjaxDataView("dataContainer", "navigator", createDataView()));
        DataView<TagAlbum> TagsDataView = createTagsDataView();
        add(TagsDataView);
        add(new PagingNavigator("tagsNavigator", TagsDataView));
    }

    /**
     * Method createDataView.
     * 
     * @return DataView<File>
     */
    private DataView<File> createDataView() {
        ArrayList<File> files = new ArrayList<File>();
        for (ShareInformationPhotos sh : shareInformationPhotosService
                .getPhotosShares(album.getId(),
                        ((MySession) Session.get()).getuId())) {
            files.add(sh.getFile());
        }
        for (ShareInformationPhotos sh : shareInformationPhotosService
                .getPhotosShares(album.getId(), (Constant.getId()))) {
            files.add(sh.getFile());
        }
        DataView<File> dataView = new DataView<File>("pageable",
                new CustomFileListDataProvider(files)) {
            public void populateItem(final Item<File> item) {
                PageParameters pars = new PageParameters();
                pars.add("album", album.getName());
                pars.add("fid", Integer.toString(item.getModelObject().getId()));
                BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>(
                        "big", SharedBig.class, pars);
                bpl.add(new NonCachingImage("img", new BlobImageResource() {
                    protected Blob getBlob(Attributes arg0) {
                        return BlobFromFile.getSmall(item.getModelObject());
                    }
                }));
                item.add(bpl);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createTagsDataView.
     * 
     * @return DataView<TagAlbum>
     */
    private DataView<TagAlbum> createTagsDataView() {
        final List<TagAlbum> list = new ArrayList<TagAlbum>(
                tagService.getTagsByAbumId(this.album.getId()));
        DataView<TagAlbum> dataView = new DataView<TagAlbum>("tagsPageable",
                new ListDataProvider<TagAlbum>(list)) {

            public void populateItem(final Item<TagAlbum> item) {
                final TagAlbum tagAlbum = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagAlbum.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagAlbum.getTag().getName()));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createCommentsDataView.
     * 
     * @return DataView<CommentAlbum>
     */
    private DataView<CommentAlbum> createCommentsDataView() {
        final List<CommentAlbum> list = new ArrayList<CommentAlbum>(
                commentService.getCommentAlbumsByAlbum(album.getId()));
        DataView<CommentAlbum> dataView = new DataView<CommentAlbum>(
                "commentPageable", new ListDataProvider<CommentAlbum>(list)) {

            public void populateItem(final Item<CommentAlbum> item) {
                final CommentAlbum commentAlbum = item.getModelObject();
                item.add(new Label("comment", commentAlbum.getComment()
                        .getCommentText()));
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "HH:mm:ss, dd/MM/yyyy");
                String dateComment = sdf.format(new Date(commentAlbum
                        .getComment().getCommentDate().getTimeInMillis()));
                item.add(new Label("commentDate", dateComment));
                item.add(new Label("userComment", commentAlbum.getComment()
                        .getUser().getUsername()));

                final ModalWindow modal = new ModalWindow("modal");
                item.add(modal);
                modal.setPageCreator(new ModalWindow.PageCreator() {
                    public Page createPage() {
                        return new ModalEdit(
                                item.getModelObject().getComment(), modal);
                    }
                });
                modal.setTitle(new StringResourceModel("albums.rename", this,
                        null));
                modal.setResizable(false);
                modal.setInitialWidth(WINDOW_WIDTH);
                modal.setInitialHeight(WINDOW_HEIGHT);
                modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                    public void onClose(AjaxRequestTarget target) {
                        setResponsePage(new SharedFiles(parameters));
                    }
                });
                AjaxLink<Void> edit = new AjaxLink<Void>("rename") {
                    public void onClick(AjaxRequestTarget target) {
                        modal.show(target);
                    }
                };
                item.add(edit);

                AjaxLink<Void> delete = new AjaxLink<Void>("commentDelete") {
                    public void onClick(AjaxRequestTarget target) {
                        commentService.delete(commentAlbum);
                        info(new StringResourceModel("comment.deleted", this,
                                null).getString());
                        setResponsePage(new SharedFiles(parameters));
                    }
                };
                item.add(delete);

                if (!commentService
                        .getCommentById(commentAlbum.getComment().getId())
                        .getUser().getId().equals(user.getId())) {
                    edit.setVisible(false);
                    delete.setVisible(false);
                }
                // ************************************************************
                // Likes
                BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                        "link", SharedFiles.class, parameters);
                like.add(new Link<Void>("like") {
                    public void onClick() {
                        LikeComment likeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentAlbum.getComment().getId());

                        if (likeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 1), commentAlbum
                                            .getComment()));
                            setResponsePage(new SharedFiles(parameters));
                            info(new StringResourceModel("like.send", this,
                                    null).getString());
                        } else {

                            if (likeComment.getLike().getMegusta() == 1) {
                                error(new StringResourceModel(
                                        "like.alreadyexist", this, null)
                                        .getString());
                            } else {
                                likeService.updateLikeDislike(likeComment
                                        .getLike().getId(), 1);
                                setResponsePage(new SharedFiles(parameters));
                                info(new StringResourceModel("like.send", this,
                                        null).getString());
                            }
                        }
                    }
                });
                item.add(like);

                final ArrayList<User> usersLikes = new ArrayList<User>();
                List<LikeComment> likesComment = likeService.getLikesComment(
                        commentAlbum.getComment().getId(), 1);
                for (LikeComment likeComment : likesComment) {
                    usersLikes.add(likeComment.getLike().getUser());
                }

                final ModalWindow modalLikes = new ModalWindow("modalLikes");
                item.add(modalLikes);
                modalLikes.setPageCreator(new ModalWindow.PageCreator() {
                    public Page createPage() {
                        return new ModalLikes(usersLikes, modalLikes);
                    }
                });
                modalLikes.setTitle(new StringResourceModel("edit.edit", this,
                        null));
                modalLikes.setResizable(false);
                modalLikes.setInitialWidth(WINDOW_WIDTH);
                modalLikes.setInitialHeight(WINDOW_HEIGHT);
                modalLikes
                        .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                            public void onClose(AjaxRequestTarget target) {
                                setResponsePage(new SharedFiles(parameters));
                            }
                        });
                AjaxLink<Void> listLikes = new AjaxLink<Void>("likes") {
                    public void onClick(AjaxRequestTarget target) {
                        modalLikes.show(target);
                    }
                };
                listLikes.add(new Label("numLikes", likesComment.size()));
                item.add(listLikes);

                // Dislikes
                BookmarkablePageLink<Void> dislike = new BookmarkablePageLink<Void>(
                        "link2", SharedFiles.class, parameters);
                dislike.add(new Link<Void>("dislike") {
                    public void onClick() {
                        LikeComment dislikeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentAlbum.getComment().getId());

                        if (dislikeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 0), commentAlbum
                                            .getComment()));
                            setResponsePage(new SharedFiles(parameters));
                            info(new StringResourceModel("like.send", this,
                                    null).getString());
                        } else {
                            if (dislikeComment.getLike().getMegusta() == 0) {
                                error(new StringResourceModel(
                                        "like.alreadyexist", this, null)
                                        .getString());
                            } else {
                                likeService.updateLikeDislike(dislikeComment
                                        .getLike().getId(), 0);
                                setResponsePage(new SharedFiles(parameters));
                                info(new StringResourceModel("like.send", this,
                                        null).getString());
                            }
                        }
                    }
                });
                item.add(dislike);

                final ArrayList<User> usersDisLikes = new ArrayList<User>();
                List<LikeComment> dislikesComment = likeService
                        .getLikesComment(commentAlbum.getComment().getId(), 0);
                for (LikeComment likeComment : dislikesComment) {
                    usersDisLikes.add(likeComment.getLike().getUser());
                }

                final ModalWindow modalDisLikes = new ModalWindow(
                        "modalDisLikes");
                item.add(modalDisLikes);
                modalDisLikes.setPageCreator(new ModalWindow.PageCreator() {
                    public Page createPage() {
                        return new ModalLikes(usersDisLikes, modalDisLikes);
                    }
                });
                modalDisLikes.setTitle(new StringResourceModel("edit.edit",
                        this, null));
                modalDisLikes.setResizable(false);
                modalDisLikes.setInitialWidth(WINDOW_WIDTH);
                modalDisLikes.setInitialHeight(WINDOW_HEIGHT);
                modalDisLikes
                        .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                            public void onClose(AjaxRequestTarget target) {
                                setResponsePage(new SharedFiles(parameters));
                            }
                        });
                AjaxLink<Void> listDisLikes = new AjaxLink<Void>("dislikes") {
                    public void onClick(AjaxRequestTarget target) {
                        modalDisLikes.show(target);
                    }
                };
                listDisLikes
                        .add(new Label("numDisLikes", usersDisLikes.size()));
                item.add(listDisLikes);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createButtonCommentOk.
     * 
     * @return AjaxButton
     */
    private AjaxButton createButtonCommentOk() {
        return new AjaxButton("buttonCommentOk") {
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Comment comment = new Comment(null, result, user);
                CommentAlbum commentAlbum = new CommentAlbum(null, comment,
                        album);
                commentService.create(commentAlbum);
                info(new StringResourceModel("comment.submitted", this, null)
                        .getString());
                setResponsePage(new SharedFiles(parameters));
            }
        };
    }

}
