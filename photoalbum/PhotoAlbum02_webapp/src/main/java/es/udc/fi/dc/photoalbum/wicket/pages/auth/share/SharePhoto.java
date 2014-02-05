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

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.ShareInformationPhotos;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.ShareInformationPhotosService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;

/**
 */
@SuppressWarnings("serial")
public class SharePhoto extends BasePageAuth {

    @SpringBean
    private UserService userService;
    @SpringBean
    private FileService fileService;
    @SpringBean
    private ShareInformationPhotosService shareInformationService;
    private PageParameters parameters;
    private File file;
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for SharePhoto.
     * 
     * @param parameters
     *            PageParameters
     */
    public SharePhoto(PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if (parameters.getNamedKeys().contains("fid")) {
            String photoId = parameters.get("fid").toString();
            this.file = fileService.getById(Integer.valueOf(photoId));
        }
        add(createShareForm());
        DataView<ShareInformationPhotos> dataView = createShareDataView();
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<ShareInformationPhotos>
     */
    private DataView<ShareInformationPhotos> createShareDataView() {
        final List<ShareInformationPhotos> list = new ArrayList<ShareInformationPhotos>(
                shareInformationService.getPhotosShares(file.getId()));
        DataView<ShareInformationPhotos> dataView = new DataView<ShareInformationPhotos>(
                "pageable", new ListDataProvider<ShareInformationPhotos>(list)) {

            public void populateItem(final Item<ShareInformationPhotos> item) {
                final ShareInformationPhotos shareInformation = item
                        .getModelObject();
                item.add(new Label("email", shareInformation.getUser()
                        .getEmail()));
                item.add(new Link<Void>("delete") {
                    public void onClick() {
                        shareInformationService.delete(shareInformation);
                        info(new StringResourceModel("share.deleted", this,
                                null).getString());
                        setResponsePage(new SharePhoto(parameters));
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
                    ShareInformationPhotos shareInformation = new ShareInformationPhotos(
                            null, file, existedUser);

                    System.out.println("EMAIL: "
                            + userService.getById(
                                    ((MySession) Session.get()).getuId())
                                    .getEmail());

                    if (shareInformationService.getShare(file.getId(),
                            existedUser.getId(), (userService
                                    .getById(((MySession) Session.get())
                                            .getuId()).getEmail())) == null) {
                        shareInformationService.create(shareInformation);
                        info(new StringResourceModel("share.sharePhotoSuccess",
                                this, null).getString());
                        setResponsePage(new SharePhoto(getPage()
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
        // shareEmail.add(EmailAddressValidator.getInstance());
        form.add(shareEmail);
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }

}
