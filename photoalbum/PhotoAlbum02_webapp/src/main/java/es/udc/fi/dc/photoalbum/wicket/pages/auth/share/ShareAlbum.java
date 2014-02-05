package es.udc.fi.dc.photoalbum.wicket.pages.auth.share;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;

/**
 */
@SuppressWarnings("serial")
public class ShareAlbum extends BasePageAuth {

    @SpringBean
    private AlbumService albumService;
    @SpringBean
    private ShareInformationAlbumsService shareInformationService;
    @SpringBean
    private UserService userService;
    private Album album;
    private PageParameters parameters;
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for ShareAlbum.
     * 
     * @param parameters
     *            PageParameters
     */
    public ShareAlbum(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if (parameters.getNamedKeys().contains("album")) {
            String name = parameters.get("album").toString();
            add(new Label("album", name));
            this.album = albumService.getAlbum(name,
                    ((MySession) Session.get()).getuId());
        }

        add(createShareForm());
        DataView<ShareInformationAlbums> dataView = createShareDataView();
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<ShareInformationAlbums>
     */
    private DataView<ShareInformationAlbums> createShareDataView() {
        final List<ShareInformationAlbums> list = new ArrayList<ShareInformationAlbums>(
                shareInformationService.getAlbumShares(this.album.getId()));
        DataView<ShareInformationAlbums> dataView = new DataView<ShareInformationAlbums>(
                "pageable", new ListDataProvider<ShareInformationAlbums>(list)) {

            public void populateItem(final Item<ShareInformationAlbums> item) {
                final ShareInformationAlbums shareInformation = item
                        .getModelObject();
                item.add(new Label("email", shareInformation.getUser()
                        .getEmail()));
                item.add(new Link<Void>("delete") {
                    public void onClick() {
                        shareInformationService.delete(shareInformation);
                        info(new StringResourceModel("share.deleted", this,
                                null).getString());
                        setResponsePage(new ShareAlbum(parameters));
                    }
                });
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createShareForm.
     * 
     * @return Form<User>
     */
    private Form<User> createShareForm() {
        Form<User> form = new Form<User>("form") {
            @Override
            protected void onSubmit() {
                User user = getModelObject();

                String userOrEmail = user.getUsername().toLowerCase();
                User existedUser = null;
                if (Validator.isUsernameValid(userOrEmail)) {
                    user.setUsername(userOrEmail);
                    user.setEmail("");
                    existedUser = userService.getByUsername(user);
                } else if (Validator.isEmailValid(userOrEmail)) {
                    user.setUsername("");
                    user.setEmail(userOrEmail);
                    existedUser = userService.getByEmail(user);
                } else {
                    error(new StringResourceModel("validator.error", this, null)
                            .getString());
                    return;
                }

                if (existedUser == null) {
                    error(new StringResourceModel("share.noUser", this, null)
                            .getString());
                } else if (existedUser.getEmail().equals(
                        userService.getById(
                                ((MySession) Session.get()).getuId())
                                .getEmail())) {
                    error(new StringResourceModel("share.yourself", this, null)
                            .getString());
                } else {
                    ShareInformationAlbums shareInformation = new ShareInformationAlbums(
                            null, album, existedUser);
                    if (!shareInformationService.alreadyShared(album,
                            existedUser.getId())) {
                        shareInformationService.create(shareInformation);
                        info(new StringResourceModel("share.shareAlbumSuccess",
                                this, null).getString());
                        setResponsePage(new ShareAlbum(getPage()
                                .getPageParameters()));
                    } else {
                        error(new StringResourceModel("share.alreadyExist",
                                this, null).getString());
                    }
                }
            }
        };
        User user = new User();
        form.setDefaultModel(new Model<User>(user));
        RequiredTextField<String> shareEmail = new RequiredTextField<String>(
                "shareEmailOrUsername", new PropertyModel<String>(user,
                        "username"));
        shareEmail.setLabel(new StringResourceModel("share.emailField", this,
                null));
        form.add(shareEmail);
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }

}
