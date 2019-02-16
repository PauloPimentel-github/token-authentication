package com.example.tgiapi.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RefreshTokenProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	/**
	 * Recupera o valor do access token
	 * Com isto é possível setar o valor em um cookie seguro
	 */
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		//faz a conversão para HttpServletRequest
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest(); 
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		//recupera o valor do cookie
		String refreshToken = body.getRefreshToken().getValue();
		addRefreshTokenInCookie(refreshToken, req, resp);
		removeRefreshTokenBody(token);
		return body;
	}

	/**
	 * Remove o refreshtoken do corpo da requisição
	 * @param token
	 */
	private void removeRefreshTokenBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	/**
	 * Adiciona o token em um cookie seguro
	 * @param refreshToken
	 * @param req
	 * @param resp
	 */
	private void addRefreshTokenInCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(false); // TODO: Mudar para true em produção
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000);
		resp.addCookie(refreshTokenCookie);
	}

}
