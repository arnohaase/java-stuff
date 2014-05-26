package de.arnohaase.javastuff.param_name_logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author arno
 */
public class ParamNameLoggerMain {
    public static void main(String[] args) throws NoSuchMethodException {
        // JDK-Klassen haben keine Namens-Informationen

        // File -> Settings -> Compiler -> Java Compiler: -parameters
        final A l = Tracer.withLogWrapper(A.class, new B());

        l.doSomething("1", 2);
    }
}

class Tracer {
    static <T> T withLogWrapper(Class<T> iface, T o) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {iface}, new LoggingInvocationHandler(o));
    }
}

class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;

    LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final StringBuilder log = new StringBuilder("invoking " + target.getClass().getSimpleName() + "." + method.getName() + "(");

        for(int i=0; i<args.length; i++) {
            if(i>0) {
                log.append(", ");
            }

            log.append(method.getParameters()[i].getName() + "=");
            log.append(args[i]);
        }

        log.append(")");
        System.out.println(log);

        return method.invoke(target, args);
    }
}

interface A {
    void doSomething(String x, int y);
}

class B implements A {
    @Override
    public void doSomething(String x, int y) {
        System.out.println("  --> " + x + " and " + y);
    }
}
