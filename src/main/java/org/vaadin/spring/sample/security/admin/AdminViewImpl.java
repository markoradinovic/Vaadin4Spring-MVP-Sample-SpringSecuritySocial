package org.vaadin.spring.sample.security.admin;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.mvp.view.AbstractMvpView;
import org.vaadin.spring.sample.security.admin.AdminPresenter.AdminView;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScope
@VaadinComponent
public class AdminViewImpl extends AbstractMvpView implements AdminView {

	private CssLayout layout;
	
	private VerticalLayout content;
	
	@Override
	public void postConstruct() {	
		super.postConstruct();
		layout = new CssLayout();		
		setCompositionRoot(layout);		
		
		content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		layout.addComponent(content);
		
		final Label label = new Label("This is Admin only view");
		content.addComponent(label);
	}
	
	
}
