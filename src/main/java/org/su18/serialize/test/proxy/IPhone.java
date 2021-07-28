package org.su18.serialize.test.proxy;

/**
 * Phone 的实现类
 *
 * @author su18
 */
public class IPhone implements Phone {

	public IPhone() {
	}

	/**
	 * 实现 Phone 的 call 方法
	 */
	@Override
	public void call() {
		System.out.println("IPhone Called");
	}

	@Override
	public Object callback() {
		return 1;
	}


}
