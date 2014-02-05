package es.udc.fi.dc.photoalbum.wicket.pages.auth.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
import org.springframework.dao.DataIntegrityViolationException;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.hibernate.Tag;
import es.udc.fi.dc.photoalbum.hibernate.TagAlbum;
import es.udc.fi.dc.photoalbum.spring.AlbumService;
import es.udc.fi.dc.photoalbum.spring.TagService;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.BasePageAuth;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;

/**
 */
@SuppressWarnings("serial")
public class CreateTagAlbum extends BasePageAuth {
    @SpringBean
    private UserService userService;
    @SpringBean
    private TagService tagService;
    @SpringBean
    private AlbumService albumService;
    private PageParameters parameters;
    private Album album;
    private static final int ITEMS_PER_PAGE = 20;

    /**
     * Constructor for CreateTagAlbum.
     * 
     * @param parameters
     *            PageParameters
     */
    public CreateTagAlbum(final PageParameters parameters) {
        super(parameters);
        this.parameters = parameters;
        if (parameters.getNamedKeys().contains("album")) {
            String name = parameters.get("album").toString();
            add(new Label("album", name));
            this.album = albumService.getAlbum(name,
                    ((MySession) Session.get()).getuId());
        }

        add(createShareForm());
        DataView<TagAlbum> dataView = createShareDataView();
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    /**
     * Method createShareDataView.
     * 
     * @return DataView<TagAlbum>
     */
    private DataView<TagAlbum> createShareDataView() {
        final List<TagAlbum> list = new ArrayList<TagAlbum>(
                tagService.getTagsByAbumId(this.album.getId()));
        DataView<TagAlbum> dataView = new DataView<TagAlbum>("pageable",
                new ListDataProvider<TagAlbum>(list)) {

            public void populateItem(final Item<TagAlbum> item) {
                final TagAlbum tagAlbum = item.getModelObject();
                PageParameters pars = new PageParameters();
                pars.add("tag", tagAlbum.getTag().getId());
                BookmarkablePageLink<Void> bp = new BookmarkablePageLink<Void>(
                        "tagsLink", Search.class, pars);
                bp.add(new Label("name", tagAlbum.getTag().getName()));
                item.add(bp);
                item.add(new Link<Void>("delete") {
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
     * Method createShareForm.
     * 
     * @return Form<Tag>
     */
    private Form<Tag> createShareForm() {
        Form<Tag> form = new Form<Tag>("form") {
            @Override
            protected void onSubmit() {
                Tag tag = getModelObject();

                String tagName = tag.getName().toLowerCase();
                Tag existedTag = null;
                if (Validator.isUsernameValid(tagName)) {
                    existedTag = tagService.getTagbyName(tagName);
                } else {
                    error(new StringResourceModel("validator.characterError",
                            this, null).getString());
                    return;
                }

                if (existedTag == null) {
                    Tag newTag = new Tag(null, tagName);
                    tagService.create(newTag);
                    tagService.create(new TagAlbum(null, newTag, album));
                } else {
                    try {
                        tagService
                                .create(new TagAlbum(null, existedTag, album));
                    } catch (DataIntegrityViolationException e) {
                        Logger.getLogger(CreateTagAlbum.class.getName()).log(
                                Level.WARNING, e.toString(), e);
                        error(new StringResourceModel("tag.alreadyCreated",
                                this, null).getString());
                    }
                }
                setResponsePage(new CreateTagAlbum(parameters));
            }
        };
        Tag tag = new Tag();
        form.setDefaultModel(new Model<Tag>(tag));
        RequiredTextField<String> tagName = new RequiredTextField<String>(
                "TagName", new PropertyModel<String>(tag, "name"));
        tagName.setLabel(new StringResourceModel("tag.nameField", this, null));
        form.add(tagName);
        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        return form;
    }
}
