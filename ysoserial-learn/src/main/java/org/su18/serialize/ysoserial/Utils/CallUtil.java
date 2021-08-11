package org.su18.serialize.ysoserial.Utils;

/**
 * @author su18
 */
public class CallUtil {


	/**
	 * 获取的上一个调用类的类名
	 *
	 * @return 返回类名
	 */
	public static String getCallClassName() {
		// 获取当前线程调用栈
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length > 2) {
			StackTraceElement element = elements[elements.length - 1];
			return element.getClassName();
		}

		return null;
	}

}
