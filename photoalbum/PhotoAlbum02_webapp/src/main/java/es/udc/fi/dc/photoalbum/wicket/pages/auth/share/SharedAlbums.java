package es.udc.fi.dc.photoalbum.wicket.pages.auth.share;

import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Constant;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;

/**
 */
@SuppressWarnings("serial")
public class SharedAlbums extends BasePageAuth {

    @SpringBean
    private UserService userService;
    @SpringBean
    private ShareInformationAlbumsService shareInformationService;
    private static final int ITEMS_PER_PAGE = 10;
    private User user;

    /**
     * Constructor for SharedAlbums.
     * 
     * @param parameters
     *            PageParameters
     */
    public SharedAlbums(final PageParameters parameters) {
        super(parameters);
        if (parameters.getNamedKeys().contains("user")) {
            final String email = parameters.get("user").toString();
            User user = new User();
            user.setEmail(email);
            if (userService.getByEmail(user) == null) {
                throw new RestartResponseException(ErrorPage404.class);
            }
            this.user = user;
            add(new AjaxDataView("dataContainer", "navigator",
                    createShareDataView()));
        } else {
            throw new RestartResponseException(ErrorPage404.class);
        }
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<ShareInformationAlbums>
     */
    private DataView<ShareInformationAlbums> createShareDataView() {
        List<ShareInformationAlbums> list = shareInformationService.getShares(
                userService.getByEmail(user),
                userService.getById(((MySession) Session.get()).getuId()));
        if (list.isEmpty()) {
            list = shareInformationService.getShares(
                    userService.getByEmail(user),
                    userService.getById(Constant.getId()));
            if (list.isEmpty()) {
                throw new RestartResponseException(ErrorPage404.class);
            }
        }
        DataView<ShareInformationAlbums> dataView = new DataView<ShareInformationAlbums>(
                "pageable", new ListDataProvider<ShareInformationAlbums>(list)) {
            public void populateItem(final Item<ShareInformationAlbums> item) {
                final ShareInformationAlbums shareInformation = item
                        .getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("user", user.getEmail());
                pars.add("album", shareInformation.getAlbum().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "link", SharedFiles.class, pars);
                bp.add(new Label("name", shareInformation.getAlbum().getName()));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

}
