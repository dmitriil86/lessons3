package classes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Utils {

    public static <T> T cache(Object obj, InvocationHandler invocationHandler)
    {
        ClassLoader classLoader = obj.getClass().getClassLoader();

        Class[] interfaces = obj.getClass().getInterfaces();

        T proxy = (T) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);

        return proxy;
    }
}
