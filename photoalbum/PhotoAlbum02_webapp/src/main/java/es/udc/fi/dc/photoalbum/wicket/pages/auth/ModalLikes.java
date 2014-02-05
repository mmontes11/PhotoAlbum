package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import es.udc.fi.dc.photoalbum.hibernate.User;

/**
 */
@SuppressWarnings("serial")
public class ModalLikes extends WebPage {

    private ModalWindow window;
    private FeedbackPanel feedback;
    private ArrayList<User> users = new ArrayList<User>();
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for ModalLikes.
     * 
     * @param users
     *            ArrayList<User>
     * @param window
     *            ModalWindow
     */
    public ModalLikes(final ArrayList<User> users, final ModalWindow window) {
        this.window = window;
        this.users = users;
        Form<Void> form = new Form<Void>("form");
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        this.feedback = feedback;

        DataView<User> dataView = createLikeDataView();
        add(dataView);
        add(new PagingNavigator("navigator", dataView));

        add(form);
    }

    /**
     * Method createLikeDataView.
     * 
     * @return DataView<User>
     */
    private DataView<User> createLikeDataView() {
        final List<User> list = new ArrayList<User>(users);
        DataView<User> UserDataView = new DataView<User>("pageable",
                new ListDataProvider<User>(list)) {
            public void populateItem(final Item<User> item) {
                final User user = item.getModelObject();

                item.add(new Label("user", user.getUsername()));

            }
        };
        UserDataView.setItemsPerPage(ITEMS_PER_PAGE);
        return UserDataView;
    }
}
