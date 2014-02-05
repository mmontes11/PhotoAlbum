package es.udc.fi.dc.photoalbum.wicket.pages.auth;

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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
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

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.hibernate.CommentFile;
import es.udc.fi.dc.photoalbum.hibernate.LikeComment;
import es.udc.fi.dc.photoalbum.hibernate.LikeFile;
import es.udc.fi.dc.photoalbum.hibernate.Likefield;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.TagFile;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.CommentService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.LikeService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.NavigateForm;
import es.udc.fi.dc.photoalbum.wicket.models.AlbumModel;
import es.udc.fi.dc.photoalbum.wicket.models.AlbumsModel;
import es.udc.fi.dc.photoalbum.wicket.models.FileOwnModel;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharePhoto;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.TagPhoto;

/**
 */
@SuppressWarnings("serial")
public class Image extends BasePageAuth {

    @SpringBean
    private FileService fileService;
    private FileOwnModel fileOwnModel;
    private PageParameters parameters;
    private Album selected;
    private int photoId;
    private User user;
    @SpringBean
    private ShareInformationPhotosService shareInformationService;
    @SpringBean
    private UserService userService;
    @SpringBean
    private TagService tagService;
    @SpringBean
    private CommentService commentService;
    @SpringBean
    private LikeService likeService;
    private static final int ITEMS_PER_PAGE = 20;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 120;
    private String result;
    private List<LikeFile> likes;
    private List<LikeFile> disLikes;

    /**
     * Constructor for Image.
     * 
     * @param parameters
     *            PageParameters
     */
    public Image(final PageParameters parameters) {
        super(parameters);
        if (parameters.getNamedKeys().contains("fid")
                && parameters.getNamedKeys().contains("album")) {
            this.photoId = parameters.get("fid").toInt();
            this.likes = likeService.getLikesFile(photoId, 1);
            this.disLikes = likeService.getLikesFile(photoId, 0);
            String name = parameters.get("album").toString();
            AlbumModel am = new AlbumModel(name);
            FileOwnModel fileOwnModel = new FileOwnModel(this.photoId, name,
                    ((MySession) Session.get()).getuId());
            this.fileOwnModel = fileOwnModel;
            if (fileOwnModel.getObject() == null) {
                throw new RestartResponseException(ErrorPage404.class);
            }
            this.parameters = parameters;
            add(new NavigateForm<Void>("formNavigate", am.getObject().getId(),
                    fileOwnModel.getObject().getId(), Image.class));
            add(createNonCachingImage());
            add(createFormDelete());
            add(createFormMove());
            add(new BookmarkablePageLink<Void>("linkBack", Upload.class,
                    (new PageParameters()).add("album", name)));
            add(new BookmarkablePageLink<Void>("share", SharePhoto.class,
                    (new PageParameters()).add("fid", this.photoId)));
            add(new BookmarkablePageLink<Void>("tag", TagPhoto.class,
                    (new PageParameters()).add("fid", this.photoId)));
            DataView<ShareInformationPhotos> dataView = createShareDataView();
            add(dataView);
            add(new PagingNavigator("navigator", dataView));
            DataView<TagFile> tagDataView = createTagDataView();
            add(tagDataView);
            add(new PagingNavigator("tagNavigator", dataView));

            this.user = userService.getById(((MySession) Session.get())
                    .getuId());

            // Likes
            BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                    "link", Image.class, parameters);
            like.add(new Link<Void>("like") {
                public void onClick() {
                    LikeFile likeFile = likeService.getLikeFileByUserFile(
                            user.getId(), photoId);

                    if (likeFile == null) {
                        likeService.create(new LikeFile(null, new Likefield(
                                null, user, 1), fileService.getById(photoId)));
                        setResponsePage(new Image(parameters));
                        info(new StringResourceModel("like.send", this, null)
                                .getString());
                    } else {
                        if (likeFile.getLike().getMegusta() == 1) {
                            error(new StringResourceModel("like.alreadyexist",
                                    this, null).getString());
                        } else {
                            likeService.updateLikeDislike(likeFile.getLike()
                                    .getId(), 1);
                            setResponsePage(new Image(parameters));
                            info(new StringResourceModel("like.send", this,
                                    null).getString());
                        }
                    }
                }
            });
            add(like);

            final ArrayList<User> usersLikes = new ArrayList<User>();
            for (LikeFile likeFile : likes) {
                usersLikes.add(likeFile.getLike().getUser());
            }

            final ModalWindow modalLikes = new ModalWindow("modalLikes");
            add(modalLikes);
            modalLikes.setPageCreator(new ModalWindow.PageCreator() {
                public Page createPage() {
                    return new ModalLikes(usersLikes, modalLikes);
                }
            });
            modalLikes
                    .setTitle(new StringResourceModel("edit.edit", this, null));
            modalLikes.setResizable(false);
            modalLikes.setInitialWidth(WINDOW_WIDTH);
            modalLikes.setInitialHeight(WINDOW_HEIGHT);
            modalLikes
                    .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                        public void onClose(AjaxRequestTarget target) {
                            setResponsePage(new Image(parameters));
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
                    "link2", Image.class, parameters);
            dislike.add(new Link<Void>("dislike") {
                public void onClick() {
                    LikeFile dislikeFile = likeService.getLikeFileByUserFile(
                            user.getId(), photoId);

                    if (dislikeFile == null) {
                        likeService.create(new LikeFile(null, new Likefield(
                                null, user, 0), fileService.getById(photoId)));
                        setResponsePage(new Image(parameters));
                        info(new StringResourceModel("dislike.send", this, null)
                                .getString());
                    } else {
                        if (dislikeFile.getLike().getMegusta() == 0) {
                            error(new StringResourceModel("like.alreadyexist",
                                    this, null).getString());
                        } else {
                            likeService.updateLikeDislike(dislikeFile.getLike()
                                    .getId(), 0);
                            setResponsePage(new Image(parameters));
                            info(new StringResourceModel("dislike.send", this,
                                    null).getString());
                        }
                    }
                }
            });
            add(dislike);

            final ArrayList<User> usersDisLikes = new ArrayList<User>();
            for (LikeFile likeFile : disLikes) {
                usersDisLikes.add(likeFile.getLike().getUser());
            }

            final ModalWindow modalDisLikes = new ModalWindow("modalDisLikes");
            add(modalDisLikes);
            modalDisLikes.setPageCreator(new ModalWindow.PageCreator() {
                public Page createPage() {
                    return new ModalLikes(usersDisLikes, modalDisLikes);
                }
            });
            modalDisLikes.setTitle(new StringResourceModel("edit.edit", this,
                    null));
            modalDisLikes.setResizable(false);
            modalDisLikes.setInitialWidth(WINDOW_WIDTH);
            modalDisLikes.setInitialHeight(WINDOW_HEIGHT);
            modalDisLikes
                    .setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                        public void onClose(AjaxRequestTarget target) {
                            setResponsePage(new Image(parameters));
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
            final TextArea<String> commentName = new TextArea<String>(
                    "comment", new PropertyModel<String>(this, "result"));
            form.add(commentName);
            form.add(createButtonCommentOk());
            add(form);
            add(new AjaxDataView("dataContainerComment", "commentNavigator",
                    createCommentsDataView()));
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }
    }

    /**
     * Method createNonCachingImage.
     * 
     * @return NonCachingImage
     */
    private NonCachingImage createNonCachingImage() {
        return new NonCachingImage("img", new BlobImageResource() {
            protected Blob getBlob(Attributes arg0) {
                return BlobFromFile.getBig(fileOwnModel.getObject());
            }
        });
    }

    /**
     * Method createFormDelete.
     * 
     * @return Form<Void>
     */
    private Form<Void> createFormDelete() {
        return new Form<Void>("formDelete") {
            @Override
            public void onSubmit() {
                fileService.delete(fileOwnModel.getObject());
                info(new StringResourceModel("image.deleted", this, null)
                        .getString());
                setResponsePage(new Upload(parameters.remove("fid")));
            }
        };
    }

    /**
     * Method createFormMove.
     * 
     * @return Form<Void>
     */
    private Form<Void> createFormMove() {
        Form<Void> form = new Form<Void>("formMove") {
            @Override
            public void onSubmit() {
                fileService.changeAlbum(fileOwnModel.getObject(), selected);
                info(new StringResourceModel("image.moved", this, null)
                        .getString());
                setResponsePage(new Upload(parameters.remove("fid")));
            }
        };
        DropDownChoice<Album> listAlbums = new DropDownChoice<Album>("albums",
                new PropertyModel<Album>(this, "selected"), new AlbumsModel(
                        fileOwnModel.getObject().getAlbum()),
                new ChoiceRenderer<Album>("name", "id"));
        listAlbums.setRequired(true);
        listAlbums.setLabel(new StringResourceModel("image.moveAlbum", this,
                null));
        form.add(listAlbums);
        form.add(new FeedbackPanel("feedback"));
        return form;
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<ShareInformationPhotos>
     */
    private DataView<ShareInformationPhotos> createShareDataView() {
        final List<ShareInformationPhotos> list = new ArrayList<ShareInformationPhotos>(
                shareInformationService.getPhotosShares(photoId));
        DataView<ShareInformationPhotos> dataView = new DataView<ShareInformationPhotos>(
                "pageable", new ListDataProvider<ShareInformationPhotos>(list)) {
            public void populateItem(final Item<ShareInformationPhotos> item) {
                final ShareInformationPhotos shareInformation = item
                        .getModelObject();
                item.add(new Label("user", shareInformation.getUser()
                        .getUsername()));
                item.add(new Label("email", shareInformation.getUser()
                        .getEmail()));
                item.add(new Link<Void>("delete") {
                    public void onClick() {
                        shareInformationService.delete(shareInformation);
                        info(new StringResourceModel("share.deleted", this,
                                null).getString());
                        setResponsePage(new Image(parameters));
                    }
                });
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;

    }

    /**
     * Method createTagDataView.
     * 
     * @return DataView<TagFile>
     */
    private DataView<TagFile> createTagDataView() {
        final List<TagFile> list = new ArrayList<TagFile>(
                tagService.getTagsByFileId(photoId));
        DataView<TagFile> TagDataView = new DataView<TagFile>("tagPageable",
                new ListDataProvider<TagFile>(list)) {
            public void populateItem(final Item<TagFile> item) {
                final TagFile tagFile = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagFile.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagFile.getTag().getName()));
                item.add(bp);
                item.add(new Label("name", tagFile.getTag().getName()));
                item.add(new Link<Void>("deleteTag") {
                    public void onClick() {
                        tagService.delete(tagFile);
                        info(new StringResourceModel("tag.deleted", this, null)
                                .getString());
                        setResponsePage(new Image(parameters));
                    }
                });
            }
        };
        TagDataView.setItemsPerPage(ITEMS_PER_PAGE);
        return TagDataView;
    }

    /**
     * Method createCommentsDataView.
     * 
     * @return DataView<CommentFile>
     */
    private DataView<CommentFile> createCommentsDataView() {
        final List<CommentFile> list = new ArrayList<CommentFile>(
                commentService.getCommentFilesByFile(photoId));
        DataView<CommentFile> dataView = new DataView<CommentFile>(
                "commentPageable", new ListDataProvider<CommentFile>(list)) {

            public void populateItem(final Item<CommentFile> item) {
                final CommentFile commentFile = item.getModelObject();
                item.add(new Label("comment", commentFile.getComment()
                        .getCommentText()));
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "HH:mm:ss, dd/MM/yyyy");
                String dateComment = sdf.format(new Date(commentFile
                        .getComment().getCommentDate().getTimeInMillis()));
                item.add(new Label("commentDate", dateComment));
                item.add(new Label("userComment", commentFile.getComment()
                        .getUser().getUsername()));

                final ModalWindow modal = new ModalWindow("modal");
                item.add(modal);
                modal.setPageCreator(new ModalWindow.PageCreator() {
                    public Page createPage() {
                        return new ModalEdit(
                                item.getModelObject().getComment(), modal);
                    }
                });
                modal.setTitle(new StringResourceModel("edit.edit", this, null));
                modal.setResizable(false);
                modal.setInitialWidth(WINDOW_WIDTH);
                modal.setInitialHeight(WINDOW_HEIGHT);
                modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                    public void onClose(AjaxRequestTarget target) {
                        setResponsePage(new Image(parameters));
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
                        commentService.delete(commentFile);
                        info(new StringResourceModel("comment.deleted", this,
                                null).getString());
                        setResponsePage(new Image(parameters));
                    }
                };
                item.add(delete);

                if (!commentService
                        .getCommentById(commentFile.getComment().getId())
                        .getUser().getId().equals(user.getId())) {
                    edit.setVisible(false);
                }

                // ************************************************************
                // Likes
                BookmarkablePageLink<Void> like = new BookmarkablePageLink<Void>(
                        "link", Image.class, parameters);
                like.add(new Link<Void>("like") {
                    public void onClick() {
                        LikeComment likeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentFile.getComment().getId());

                        if (likeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 1), commentFile
                                            .getComment()));
                            setResponsePage(new Image(parameters));
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
                                setResponsePage(new Image(parameters));
                                info(new StringResourceModel("like.send", this,
                                        null).getString());
                            }
                        }
                    }
                });
                item.add(like);

                final ArrayList<User> usersLikes = new ArrayList<User>();
                List<LikeComment> likesComment = likeService.getLikesComment(
                        commentFile.getComment().getId(), 1);
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
                                setResponsePage(new Image(parameters));
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
                        "link2", Image.class, parameters);
                dislike.add(new Link<Void>("dislike") {
                    public void onClick() {
                        LikeComment dislikeComment = likeService
                                .getLikeCommentByUserComment(user.getId(),
                                        commentFile.getComment().getId());

                        if (dislikeComment == null) {
                            likeService.create(new LikeComment(null,
                                    new Likefield(null, user, 0), commentFile
                                            .getComment()));
                            setResponsePage(new Image(parameters));
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
                                setResponsePage(new Image(parameters));
                                info(new StringResourceModel("like.send", this,
                                        null).getString());
                            }
                        }
                    }
                });
                item.add(dislike);

                final ArrayList<User> usersDisLikes = new ArrayList<User>();
                List<LikeComment> dislikesComment = likeService
                        .getLikesComment(commentFile.getComment().getId(), 0);
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
                                setResponsePage(new Image(parameters));
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
                CommentFile commentFile = new CommentFile(null, comment,
                        fileOwnModel.getObject());
                commentService.create(commentFile);
                info(new StringResourceModel("comment.submitted", this, null)
                        .getString());
                setResponsePage(new Image(parameters));
            }
        };
    }

}
