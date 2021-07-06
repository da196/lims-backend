package tz.go.tcra.lims.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.uaa.service.UserDetailsServiceImpl;
import tz.go.tcra.lims.utils.JwtUtility;
import tz.go.tcra.lims.utils.exception.AuthException;
import tz.go.tcra.lims.utils.exception.ForbiddenException;

@Component
@Slf4j
public class JwtConfig extends OncePerRequestFilter {

	@Autowired
	private JwtUtility jwtUtil;

	@Autowired
	private UserDetailsServiceImpl service;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		try {
			String authorizationHeader = httpServletRequest.getHeader("Authorization");

			String token = null;
			String userName = null;
			String uri = null;
			String url = null;
			String method = null;
			String query = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

				token = authorizationHeader.substring(7);
				userName = jwtUtil.extractUsername(token);
				url = httpServletRequest.getRequestURL().toString();
				uri = httpServletRequest.getRequestURI();
				method = httpServletRequest.getMethod();
				query = httpServletRequest.getQueryString();

//				System.out.println("Requested URI=   " + uri);
//				System.out.println("Requested URL=  " + url);
//				System.out.println("Requested METHOD =  " + method);
//				System.out.println("Requested Query =  " + query);
//				System.out.println("Requested username =  " + userName);

			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = service.loadUserByUsername(userName);

				if (!jwtUtil.validateToken(token, userDetails)) {

					throw new ForbiddenException("Unauthorized Access");
				}

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}

			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new AuthException(e.getLocalizedMessage());
		}
	}
}
