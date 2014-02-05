package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import java.sql.Blob;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentAlbum;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.LikeAlbum;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.ResizeImage;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.FileListDataProvider;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.models.AlbumModel;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharePhoto;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.CreateTagAlbum;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.TagPhoto;

/**
 */
@SuppressWarnings("serial")
public class Upload extends BasePageAuth {

    @SpringBean
    private FileService fileService;
    @SpringBean
    private ShareInformationAlbumsService shareInformationService;
    @SpringBean
    private TagService tagService;
    @SpringBean
    private AlbumService albumService;
    @SpringBean
    private UserService userService;
    @SpringBean
    private CommentService commentService;
    @SpringBean
    private LikeService likeService;

    private final PageParameters parameters;
    private AlbumModel am;
    private Album album;
    private User user;
    private static final int ITEMS_PER_PAGE = 10;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 120;
    private static final int MAX_UPLOAD = 10000;
    private static final int SIZE = 200;
    private FeedbackPanel feedback;
    private String result;
    private List<LikeAlbum> likes;
    private List<LikeAlbum> disLikes;

    /**
     * Constructor for Upload.
     * 
     * @param parameters
     *            PageParameters
     */
    public Upload(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if (parameters.getNamedKeys().contains("album")) {
            String name = parameters.get("album").toString();
            this.album = albumService.getAlbum(name,
                    ((MySession) Session.get()).getuId());
            this.likes = likeService.getLikesAlbum(album.getId(), 1);
            this.disLikes = likeService.getLikesAlbum(album.getId(), 0);
            add(new Label("album", name));
            AlbumModel am = new AlbumModel(name);
            this.am = am;
            if (am.getObject() == null) {
                throw new RestartResponseException(ErrorPage404.class);
            }
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }
        this.user = userService.getById(((MySession) Session.get()).getuId());
        FeedbackPanel feedback = new FeedbackPanel("uploadFeedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        this.feedback = feedback;
        add(createUplooadForm());
        add(new AjaxDataView("dataContainer", "navigator", createFileDataView()));
        // Likes
        BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                "link", Upload.class, parameters);
        like.add(new Link<Void>("like") {
            public void onClick() {
                LikeAlbum likeAlbum = likeService.getLikeAlbumByUserAlbum(
                        user.getId(), album.getId());

                if (likeAlbum == null) {
                    likeService.create(new LikeAlbum(null, new Likefield(null,
                            user, 1), album));
                    setResponsePage(new Upload(parameters));
                    info(new StringResourceModel("like.send", this, null)
                            .getString());
                } else {
                    if (likeAlbum.getLike().getMegusta() == 1) {
                        error(new StringResourceModel("like.alreadyexist",
                                this, null).getString());
                    } else {
                        likeService.updateLikeDislike(likeAlbum.getLike()
                                .getId(), 1);
                        setResponsePage(new Upload(parameters));
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
                        setResponsePage(new Upload(parameters));
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
                "link2", Upload.class, parameters);
        dislike.add(new Link<Void>("dislike") {
            public void onClick() {
                LikeAlbum dislikeAlbum = likeService.getLikeAlbumByUserAlbum(
                        user.getId(), album.getId());

                if (dislikeAlbum == null) {
                    likeService.create(new LikeAlbum(null, new Likefield(null,
                            user, 0), album));
                    setResponsePage(new Upload(parameters));
                    info(new StringResourceModel("dislike.send", this, null)
                            .getString());
                } else {
                    if (dislikeAlbum.getLike().getMegusta() == 0) {
                        error(new StringResourceModel("like.alreadyexist",
                                this, null).getString());
                    } else {
                        likeService.updateLikeDislike(dislikeAlbum.getLike()
                                .getId(), 0);
                        setResponsePage(new Upload(parameters));
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
                        setResponsePage(new Upload(parameters));
                    }
                });
        AjaxLink<Void> listDisLikes = new AjaxLink<Void>("dislikes") {
            public void onClick(AjaxRequestTarget target) {
                modalDisLikes.show(target);
            }
        };
        listDisLikes.add(new Label("numDisLikes", disLikes.size()));
        add(listDisLikes);

        Form<Void> form = new Form<Void>("form");
        final TextArea<String> name = new TextArea<String>("comment",
                new PropertyModel<String>(this, "result"));
        form.add(name);
        form.add(createButtonCommentOk());
        add(form);
        add(new AjaxDataView("dataContainerComment", "commentNavigator",
                createCommentsDataView()));

        DataView<ShareInformationAlbums> ShareDataView = createShareDataView();
        add(ShareDataView);
        add(new PagingNavigator("shareNavigator", ShareDataView));
        DataView<TagAlbum> TagsDataView = createTagsDataView();
        add(TagsDataView);
        add(new PagingNavigator("tagsNavigator", TagsDataView));
    }

    /**
     * Method createFileDataView.
     * 
     * @return DataView<File>
     */
    private DataView<File> createFileDataView() {
        int count = fileService.getCountAlbumFiles(am.getObject().getId())
                .intValue();
        DataView<File> dataView = new DataView<File>("pageable",
                new FileListDataProvider(count, am.getObject().getId())) {
            public void populateItem(final Item<File> item) {
                PageParameters pars = new PageParameters();
                pars.add("album", am.getObject().getName());
                pars.add("fid", Integer.toString(item.getModelObject().getId()));
                BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>(
                        "big", Image.class, pars);
                bpl.add(new NonCachingImage("img", new BlobImageResource() {
                    protected Blob getBlob(Attributes arg0) {
                        return BlobFromFile.getSmall(item.getModelObject());
                    }
                }));
                item.add(bpl);
                BookmarkablePageLink<Void> bp2 = new BookmarkablePageLink<Void>(
                        "share", SharePhoto.class, pars);
                item.add(bp2);
                BookmarkablePageLink<Void> bp3 = new BookmarkablePageLink<Void>(
                        "tag", TagPhoto.class, pars);
                item.add(bp3);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createUplooadForm.
     * 
     * @return Form<Void>
     */
    private Form<Void> createUplooadForm() {
        final FileUploadField fileUploadField;
        fileUploadField = new FileUploadField("fileInput");

        Form<Void> form = new Form<Void>("upload") {
            @Override
            protected void onSubmit() {
                final List<FileUpload> uploads = fileUploadField
                        .getFileUploads();
                if (uploads != null) {
                    for (FileUpload upload : uploads) {
                        try {
                            byte[] bFile = upload.getBytes();
                            System.out.println(upload.getContentType());
                            if (upload.getClientFileName().matches(
                                    "(.+(\\.(?i)(jpg|jpeg|bmp|png))$)")) {
                                File file = new File(null,
                                        upload.getClientFileName(), bFile,
                                        ResizeImage.resize(bFile, SIZE,
                                                upload.getContentType()),
                                        am.getObject());
                                fileService.create(file);
                                Upload.this.info("saved file: "
                                        + upload.getClientFileName());
                                setResponsePage(new Upload(getPageParameters()));
                            } else {
                                Upload.this.error(new StringResourceModel(
                                        "upload.wrongFormat", this, null)
                                        .getString()
                                        + upload.getClientFileName());
                            }
                        } catch (Exception e) {
                            Logger.getLogger(Upload.class.getName()).log(
                                    Level.WARNING, e.toString(), e);
                            Upload.this.error(new StringResourceModel(
                                    "upload.wrongFormat", this, null)
                                    .getString());
                        }
                    }
                } else {
                    error(new StringResourceModel("upload.noFiles", this, null)
                            .getString());
                }
            }
        };
        form.add(fileUploadField);
        form.setMultiPart(true);
        form.setMaxSize(Bytes.kilobytes(MAX_UPLOAD));
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<ShareInformationAlbums>
     */
    private DataView<ShareInformationAlbums> createShareDataView() {
        final List<ShareInformationAlbums> list = new ArrayList<ShareInformationAlbums>(
                shareInformationService.getAlbumShares(am.getObject().getId()));
        DataView<ShareInformationAlbums> dataView = new DataView<ShareInformationAlbums>(
                "sharePageable", new ListDataProvider<ShareInformationAlbums>(
                        list)) {

            public void populateItem(final Item<ShareInformationAlbums> item) {
                final ShareInformationAlbums shareInformation = item
                        .getModelObject();
                item.add(new Label("email", shareInformation.getUser()
                        .getEmail()));
                item.add(new Link<Void>("shareDelete") {
                    public void onClick() {
                        shareInformationService.delete(shareInformation);
                        info(new StringResourceModel("share.deleted", this,
                                null).getString());
                        setResponsePage(new Upload(parameters));
                    }
                });
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
                item.add(new Link<Void>("tagsDelete") {
                    public void onClick() {
                        tagService.delete(tagAlbum);
                        info(new StringResourceModel("tag.deleted", this, null)
                                .getString());
                        setResponsePage(new CreateTagAlbum(parameters));
                    }
                });
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
                commentService.getCommentAlbumsByAlbum(am.getObject().getId()));
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
                        setResponsePage(new Upload(parameters));
                    }
                });
                AjaxLink<Void> edit = new AjaxLink<Void>("rename") {
                    public void onClick(AjaxRequestTarget target) {
                        modal.show(target);
                    }
                };
                item.add(edit);

                if (!commentService
                        .getCommentById(commentAlbum.getComment().getId())
                        .getUser().getId().equals(user.getId())) {
                    edit.setVisible(false);
                }

                item.add(new AjaxLink<Void>("commentDelete") {
                    public void onClick(AjaxRequestTarget target) {
                        commentService.delete(commentAlbum);
                        info(new StringResourceModel("comment.deleted", this,
                                null).getString());
                        setResponsePage(new Upload(parameters));
                    }
                });

                // ************************************************************
                // Likes
                BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                        "link", Upload.class, parameters);
                like.add(new Link<Void>("like") {
                    public void onClick() {
                        LikeComment likeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentAlbum.getComment().getId());

                        if (likeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 1), commentAlbum
                                            .getComment()));
                            setResponsePage(new Upload(parameters));
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
                                setResponsePage(new Upload(parameters));
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
                                setResponsePage(new Upload(parameters));
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
                        "link2", Upload.class, parameters);
                dislike.add(new Link<Void>("dislike") {
                    public void onClick() {
                        LikeComment dislikeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentAlbum.getComment().getId());

                        if (dislikeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 0), commentAlbum
                                            .getComment()));
                            setResponsePage(new Upload(parameters));
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
                                setResponsePage(new Upload(parameters));
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
                                setResponsePage(new Upload(parameters));
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
                setResponsePage(new Upload(parameters));
            }
        };
    }

}
