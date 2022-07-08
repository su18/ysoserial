package org.su18.ysuserial.payloads;


/**
 * @author mbechler
 */
public interface ReleaseableObjectPayload<T> extends ObjectPayload<T> {

	void release(T obj) throws Exception;
}
