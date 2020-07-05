package com.kt.ibs.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

	@Override
	public void init(final FilterConfig fc) throws ServletException {
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");

		String headers = response.getHeader("Access-Control-Allow-Headers");
        // log.debug("## Current headers are [{}]", headers);
		response.setHeader("Access-Control-Allow-Headers",
				"x-requested-with, Content-Type, Authorization, Origin, Accept");
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
	}

}
