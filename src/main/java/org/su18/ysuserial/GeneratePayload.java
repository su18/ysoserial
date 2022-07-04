package org.su18.ysuserial;

import java.io.PrintStream;
import java.util.*;

import org.su18.ysuserial.payloads.ObjectPayload;
import org.su18.ysuserial.payloads.ObjectPayload.Utils;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;

@SuppressWarnings("rawtypes")
public class GeneratePayload {

    private static final int INTERNAL_ERROR_CODE = 70;

    private static final int USAGE_CODE = 64;

    public static void main(final String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(USAGE_CODE);
        }
        final String payloadType = args[0];
        final String command     = args[1];

        final Class<? extends ObjectPayload> payloadClass = Utils.getPayloadClass(payloadType);
        if (payloadClass == null) {
            System.err.println("Invalid payload type '" + payloadType + "'");
            printUsage();
            System.exit(USAGE_CODE);
            return; // make null analysis happy
        }

        try {
            final ObjectPayload payload = payloadClass.newInstance();
            final Object        object  = payload.getObject(command);
            PrintStream         out     = System.out;
            Serializer.serialize(object, out);
            ObjectPayload.Utils.releasePayload(payload, object);
        } catch (Throwable e) {
            System.err.println("Error while generating or serializing payload");
            e.printStackTrace();
            System.exit(INTERNAL_ERROR_CODE);
        }
        System.exit(0);
    }

    private static void printUsage() {

        System.err.println("            _.-^^---....,,--\n" +
            "       _--                  --_\n" +
            "      <                        >)\n" +
            "      |       Y Su Serial ?     |\n" +
            "       \\._                   _./\n" +
            "          ```--. . , ; .--'''\n" +
            "                | |   |\n" +
            "             .-=||  | |=-.\n" +
            "             `-=#$%&%$#=-'\n" +
            "                | ;  :|\n" +
            "       _____.,-#%&$@%#&#~,._____\n" +
            "     _____.,[ 暖风熏得游人醉 ],._____\n" +
            "     _____.,[ 直把杭州作汴州 ],._____"
        );
        System.err.println("[root]#~  A Mind-Blowing Tool Collected By [ su18@javaweb.org ]");
        System.err.println("[root]#~  Shout Out to Yzmm / Shxjia / Y4er / N1nty / C0ny1 / Phith0n / Kezibei");
        System.err.println("[root]#~  AND OF COURSE TO THE All MIGHTY @frohoff  ");
        System.err.println("[root]#~  Usage: java -jar ysuserial-0.1-su18-all.jar [payload] '[command]'");
        System.err.println("[root]#~  Available payload types:");

        final List<Class<? extends ObjectPayload>> payloadClasses =
            new ArrayList<Class<? extends ObjectPayload>>(ObjectPayload.Utils.getPayloadClasses());
        Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize

        final List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[]{"Payload", "Authors", "Dependencies"});
        rows.add(new String[]{"-------", "-------", "------------"});
        for (Class<? extends ObjectPayload> payloadClass : payloadClasses) {
            rows.add(new String[]{
                payloadClass.getSimpleName(),
                Strings.join(Arrays.asList(Authors.Utils.getAuthors(payloadClass)), ", ", "@", ""),
                Strings.join(Arrays.asList(Dependencies.Utils.getDependenciesSimple(payloadClass)), ", ", "", "")
            });
        }

        final List<String> lines = Strings.formatTable(rows);

        for (String line : lines) {
            System.err.println("     " + line);
        }
    }
}
