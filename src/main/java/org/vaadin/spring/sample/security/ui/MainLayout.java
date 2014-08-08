package org.vaadin.spring.sample.security.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.mvp.MvpPresenterView;
import org.vaadin.spring.navigator.SpringViewProvider;
import org.vaadin.spring.security.Security;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@UIScope
@VaadinComponent
@SuppressWarnings("serial")
public class MainLayout extends VerticalLayout implements ViewDisplay, ClickListener, ViewChangeListener {
	
	private Panel viewContainer;
	
	private HorizontalLayout navbar;
	
	private Button btnHome;
	private Button btnSocial;
	private Button btnLogout;
	
	@Autowired
	Security security;
	
	@Autowired
	SpringViewProvider springViewProvider;
	
	@PostConstruct
	public void postConstuct() {
		setSizeFull();
		
		navbar = new HorizontalLayout();
		navbar.setWidth("100%");
		navbar.setMargin(true);
		navbar.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		addComponent(navbar);
		
		final Label brand = new Label("Vaadin4Spring Security Social Demo");
		brand.addStyleName(ValoTheme.LABEL_H2);
		brand.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		navbar.addComponent(brand);
		navbar.setComponentAlignment(brand, Alignment.MIDDLE_LEFT);
		navbar.setExpandRatio(brand, 1);
		
		btnHome = new Button("Home", FontAwesome.HOME);
		btnHome.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnHome.setData(ViewToken.HOME);
		btnHome.addClickListener(this);
		navbar.addComponent(btnHome);
		
		btnSocial = new Button("Social connections", FontAwesome.SHARE_SQUARE);
		btnSocial.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnSocial.setData(ViewToken.SOCIAL);
		btnSocial.addClickListener(this);
		navbar.addComponent(btnSocial);
						
		btnLogout = new Button("Logout", FontAwesome.SIGN_OUT);
		btnLogout.setData("-");
		btnLogout.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		navbar.addComponent(btnLogout);
		btnLogout.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getSession().close();
				Page.getCurrent().setLocation("/logout");
				
			}
		});
		
		viewContainer = new Panel();
		viewContainer.setSizeFull();
		addComponent(viewContainer);
		setExpandRatio(viewContainer, 1);
		
		
	}
	
	@Override
	public void showView(View view) {
		
		if (view instanceof MvpPresenterView) {
			viewContainer.setContent(((MvpPresenterView) view).getViewComponent());
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		
		/* This method in class should be public
		 * private boolean isViewBeanNameValidForCurrentUI(String beanName)
		 * 
		 *  workaround
		 *  
		 *  This should be implemented in custom Navigator class.
		 */				
		if ( springViewProvider.getView((String) event.getButton().getData()) == null ) {
			/*
			 * For this demo, notify user that has no access.
			 * Easily, this can be modified to hide adminLink 
			 */
			//notify user
			Notification.show("Access is alowed only for admin users.", Type.ERROR_MESSAGE);			
		} else {
			UI.getCurrent().getNavigator().navigateTo((String) event.getButton().getData());
		}
		
									
	}

	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		return true;
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {
		for (int i=0; i<navbar.getComponentCount(); i++) {
			
			if (navbar.getComponent(i) instanceof Button) {
				final Button btn = (Button) navbar.getComponent(i);
				btn.removeStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				
				String view = (String) btn.getData();
				
				if (event.getViewName().equals(view)) {
					btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				}
			}
		}
		
		
		
	}

}
