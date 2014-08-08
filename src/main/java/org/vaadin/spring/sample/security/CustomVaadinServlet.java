package org.vaadin.spring.sample.security;

import javax.servlet.ServletException;

import org.jsoup.nodes.Element;
import org.vaadin.spring.servlet.SpringAwareVaadinServlet;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

@SuppressWarnings("serial")
public class CustomVaadinServlet extends SpringAwareVaadinServlet {
	
	@Override
	protected void servletInitialized() throws ServletException {	
		super.servletInitialized();
		getService().addSessionInitListener(new SessionInitListener() {
			
			private static final long serialVersionUID = -6951911585827609232L;

			@Override
			public void sessionInit(SessionInitEvent event) throws ServiceException {
				
				event.getSession().addBootstrapListener(new BootstrapListener() {
					
					private static final long serialVersionUID = -7924761905948932337L;

					@Override
					public void modifyBootstrapPage(BootstrapPageResponse response) {
						response.getDocument().body().attr("lang", "sr");
						response.getDocument().head().prependElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1");	
						
						Element head = response.getDocument().getElementsByTag("head").get(0);

						Element jquery = response.getDocument().createElement("script");						
						jquery.attr("src", "https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js");
						head.appendChild(jquery);
						
						Element formSender = response.getDocument().createElement("script");
						formSender.attr("src", "/VAADIN/form_sender.js");
						head.appendChild(formSender);
					}
					

					@Override
					public void modifyBootstrapFragment(
							BootstrapFragmentResponse response) {
						
					}
				});
				
			}
		});
	}

}
