package org.springframework.boot.autoconfigure.social;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Social connectivity with Google.
 *
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, GoogleConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.google.", value = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class GoogleAutoConfiguration {
	
	@Configuration
	@EnableSocial
	@ConditionalOnWebApplication
	protected static class GoogleAutoConfigurationAdapter extends
			SocialAutoConfigurerAdapter {

		@Override
		protected String getPropertyPrefix() {
			return "spring.social.google.";
		}

		@Override
		protected ConnectionFactory<?> createConnectionFactory(
				RelaxedPropertyResolver properties) {
			GoogleConnectionFactory factory = new GoogleConnectionFactory(
					properties.getRequiredProperty("app-id"),
					properties.getRequiredProperty("app-secret"));

			return factory;
		}

		
		@Bean
		@ConditionalOnMissingBean(Google.class)
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		public Google google(ConnectionRepository repository) {
			Connection<Google> connection = repository.findPrimaryConnection(Google.class);
			return connection != null ? connection.getApi() : new GoogleTemplate();
		}
						

		@Bean(name = { "connect/googleConnect", "connect/googleConnected" })
		@ConditionalOnProperty(prefix = "spring.social.", value = "auto-connection-views")
		public View googleConnectView() {
			return new GenericConnectionStatusView("google", "Google");
		}

	}

}
