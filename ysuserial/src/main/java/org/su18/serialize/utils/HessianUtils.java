package org.su18.serialize.utils;

import com.caucho.burlap.io.BurlapInput;
import com.caucho.burlap.io.BurlapOutput;
import com.caucho.hessian.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author su18
 */
public class HessianUtils {

	/**
	 * Hessian 序列化
	 *
	 * @param object 待序列化对象
	 * @return 返回序列化后的 byte[]
	 */
	public static byte[] hessianSerialize(Object object, String type) {
		AbstractHessianOutput oo     = null;
		byte[]                result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			switch (type) {
				case "hessian2":
					oo = new Hessian2Output(bos);
					break;
				case "burlap":
					oo = new BurlapOutput(bos);
					break;
				default:
					oo = new HessianOutput(bos);

			}

			oo.getSerializerFactory().setAllowNonSerializable(true);

			oo.writeObject(object);
			oo.flush();
			result = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Hessian 反序列化
	 *
	 * @param bytes 待反序列化的 byte[]
	 * @return 返回反序列化后的对象
	 */
	public static Object hessianSerializeToObj(byte[] bytes, String type) {
		Object               result = null;
		AbstractHessianInput input  = null;
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);

			switch (type) {
				case "hessian2":
					input = new Hessian2Input(is);
					break;
				case "burlap":
					input = new BurlapInput(is);
					break;
				default:
					input = new HessianInput(is);
			}

			result = input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
