package com.example.tgiapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * Autoriza o cliente acessar o AuthorizationServer
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("angular") //nome do cliente
			.secret("@ngul@r0") //senha do cliente
			.scopes("read", "write") //limitar acesso do cliente
			.authorizedGrantTypes("password", "refresh_token") //granttype - usuario e senha
			.accessTokenValiditySeconds(1800) //tempo de expiração do token
			.refreshTokenValiditySeconds(3600 * 24) //token atualiazado com expiração de um dia
		.and()
			.withClient("mobile") //nome do cliente
			.secret("m0b1l30") //senha do cliente
			.scopes("read") //limitar acesso do cliente
			.authorizedGrantTypes("password", "refresh_token") //granttype - usuario e senha
			.accessTokenValiditySeconds(1800) //tempo de expiração do token
			.refreshTokenValiditySeconds(3600 * 24); //token atualiazado com expiração de um dia
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false)
			.authenticationManager(this.authenticationManager);
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("tgi");
		return accessTokenConverter;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
