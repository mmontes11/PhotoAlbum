package es.udc.fi.dc.photoalbum.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.DataView;

/**
 * Creates components for paging using Ajax
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class AjaxDataView extends WebMarkupContainer {

    /**
     * @param wmcId
     *            id of WebMarkupContainer
     * @param apnId
     *            id of AjaxPagingNavigator
     * @param dataView
     *            instance of DataView that will be paging using Ajax
     */
    public AjaxDataView(String wmcId, String apnId, DataView<?> dataView) {
        super(wmcId);
        setOutputMarkupId(true);
        AjaxPagingNavigator navigator = new AjaxPagingNavigator(apnId, dataView) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(AjaxDataView.this);
            }
        };
        add(navigator);
        add(dataView);
    }
}
