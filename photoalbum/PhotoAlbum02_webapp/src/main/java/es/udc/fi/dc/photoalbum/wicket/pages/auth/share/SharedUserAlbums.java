package es.udc.fi.dc.photoalbum.wicket.pages.auth.share;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.ShareInformationAlbums;
import es.udc.fi.dc.photoalbum.spring.ShareInformationAlbumsService;
import es.udc.fi.dc.photoalbum.utils.Constant;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;

/**
 */
@SuppressWarnings("serial")
public class SharedUserAlbums extends BasePageAuth {
    @SpringBean
    private ShareInformationAlbumsService shareInformationService;
    private static final int ITEMS_PER_PAGE = 10;

    /**
     * Constructor for SharedUserAlbums.
     * 
     * @param parameters
     *            PageParameters
     */
    public SharedUserAlbums(final PageParameters parameters) {
        super(parameters);
        add(new AjaxDataView("dataContainer", "navigator", createDataView()));
        add(new AjaxDataView("dataContainerPublic", "navigator",
                createDataViewPublic()));
    }

    /**
     * Method createDataViewPublic.
     * 
     * @return DataView<String>
     */
    private DataView<String> createDataViewPublic() {
        final HashSet<String> set = new HashSet<String>();
        final List<ShareInformationAlbums> list = new ArrayList<ShareInformationAlbums>(
                shareInformationService.getUserShares(Constant.getId()));
        for (ShareInformationAlbums aList : list) {
            set.add(aList.getAlbum().getUser().getEmail());
        }
        final List<String> list1 = new ArrayList<String>(set);
        DataView<String> dataView = new DataView<String>("pageable",
                new ListDataProvider<String>(list1)) {
            public void populateItem(final Item<String> item) {
                final String email = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("user", email);
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "albums", SharedAlbums.class, pars);
                bp.add(new Label("email", email));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createDataView.
     * 
     * @return DataView<String>
     */
    private DataView<String> createDataView() {
        final HashSet<String> set = new HashSet<String>();
        final List<ShareInformationAlbums> list = new ArrayList<ShareInformationAlbums>(
                shareInformationService.getUserShares(((MySession) Session
                        .get()).getuId()));
        for (ShareInformationAlbums aList : list) {
            set.add(aList.getAlbum().getUser().getEmail());
        }
        final List<String> list1 = new ArrayList<String>(set);
        DataView<String> dataView = new DataView<String>("pageable",
                new ListDataProvider<String>(list1)) {
            public void populateItem(final Item<String> item) {
                final String email = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("user", email);
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "albums", SharedAlbums.class, pars);
                bp.add(new Label("email", email));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

}
