package es.udc.fi.dc.photoalbum.wicket.pages.auth.search;

import java.sql.Blob;
import java.util.ArrayList;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.FileService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.AjaxDataView;
import es.udc.fi.dc.photoalbum.wicket.BlobFromFile;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;

/**
 */
@SuppressWarnings("serial")
public class Search extends BasePageAuth {

    @SpringBean
    private TagService tagService;
    @SpringBean
    private UserService userService;
    @SpringBean
    private AlbumService albumService;
    @SpringBean
    private FileService fileService;
    private User user;
    private static final int ITEMS_PER_PAGE = 10;

    /**
     * Constructor for Search.
     * 
     * @param parameters
     *            PageParameters
     */
    public Search(PageParameters parameters) {
        super(parameters);
        this.user = userService.getById(((MySession) Session.get()).getuId());
        add(searchTagForm());

        StringValue tagId = StringValue.valueOf(-1);
        if (parameters != null && !parameters.isEmpty()) {
            tagId = parameters.get("tag");
        }

        add(new AjaxDataView("dataContainerAlbum", "navigator",
                createAlbumDataView(tagId.toInt())));
        add(new AjaxDataView("dataContainerFile", "navigator",
                createFileDataView(tagId.toInt())));

    }

    /**
     * Method searchTagForm.
     * 
     * @return Form<Tag>
     */
    private Form<Tag> searchTagForm() {
        Form<Tag> form = new Form<Tag>("form") {
            @Override
            protected void onSubmit() {
                Tag t = getModelObject();
                Tag tag = tagService.getTagbyName(t.getName());
                if (tag != null && tagService.getTagById(tag.getId()) != null) {
                    PageParameters p = new PageParameters();
                    p.add("tag", tag.getId());
                    setResponsePage(new Search(p));
                } else {
                    setResponsePage(new Search(null));
                    error(new StringResourceModel("search.notFound", this, null)
                            .getString());
                }
            }
        };
        Tag tag = new Tag();
        form.setDefaultModel(new Model<Tag>(tag));
        RequiredTextField<String> tagName = new RequiredTextField<String>(
                "TagName", new PropertyModel<String>(tag, "name"));
        tagName.setLabel(new StringResourceModel("search.tagNameField", this,
                null));
        form.add(tagName);
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }

    /**
     * Method createAlbumDataView.
     * 
     * @param tagId
     *            int
     * @return DataView<Album>
     */
    private DataView<Album> createAlbumDataView(final int tagId) {
        ArrayList<Album> albumes = tagService.getAlbumes(tagId, user.getId());

        DataView<Album> dataView = new DataView<Album>("pageable",
                new ListDataProvider<Album>(albumes)) {
            public void populateItem(final Item<Album> item) {
                final Album album = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagId);
                pars.add("user", album.getUser().getEmail());
                pars.add("album", album.getName());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "link", SearchFiles.class, pars);
                bp.add(new Label("name", album.getName()));
                item.add(bp);
            }
        };
        dataView.setItemsPerPage(ITEMS_PER_PAGE);
        return dataView;
    }

    /**
     * Method createFileDataView.
     * 
     * @param tagId
     *            int
     * @return DataView<File>
     */
    private DataView<File> createFileDataView(final int tagId) {
        ArrayList<File> list = tagService.getAllFiles(tagId, user.getId());

        DataView<File> dataView = new DataView<File>("pageable",
                new ListDataProvider<File>(list)) {
            public void populateItem(final Item<File> item) {
                final File file = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("fid", Integer.toString(file.getId()));
                pars.add("tag", tagId);
                BookmarkablePageLink<Void> bpl = new BookmarkablePageLink<Void>(
                        "big", SearchBig.class, pars);
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

}
