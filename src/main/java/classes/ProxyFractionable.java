package classes;

import annotations.Cache;
import annotations.Mutator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Thread.sleep;

public class ProxyFractionable implements InvocationHandler {

    private Fractionable fractionable;

    class Info
    {
        Double value;
        Long time;
        Instant startTime;
    }

    private ConcurrentMap<String, Info> caches = new ConcurrentHashMap<String, Info>();

    private long timeCached = 0;

    private Thread thread = new Thread(()-> {
        try {
            pr();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });
    private void pr() throws InterruptedException {
        do {
            caches.forEach((k,v)->{
                long timeElapse = Duration.between(v.startTime, Instant.now()).toMillis();
               // System.out.println("mils = "+timeElapse);
                if (timeElapse>=v.time)
                {
                    caches.remove(k);
                    System.out.println("remove "+k);
                }
            });
        }while (true);

    }

    public ProxyFractionable(Fractionable fractionable) {
        this.fractionable = fractionable;
        thread.start();

    }

    private String getKey(Fractionable fractionable) throws NoSuchFieldException, IllegalAccessException {
        Field fieldNum = fractionable.getClass().getDeclaredField("num");
        fieldNum.setAccessible(true);
        Integer num = fieldNum.getInt(fractionable);

        Field fieldDenum = fractionable.getClass().getDeclaredField("denum");
        fieldDenum.setAccessible(true);
        Integer denum = fieldDenum.getInt(fractionable);

        return num +"_"+ denum;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method m = fractionable.getClass().getMethod(method.getName(),method.getParameterTypes());

        if (m.isAnnotationPresent(Cache.class)) {

            String key = getKey(fractionable);

            timeCached = Long.valueOf(m.getAnnotation(Cache.class).time());

            if (!caches.containsKey(key))
            {
                Info info = new Info();
                info.time = timeCached;

                Double value = (Double) method.invoke(fractionable, args);
                info.value = value;
                info.startTime = Instant.now();
                caches.put(key, info);
                return value;
            }
            else
            {
                Info temp = caches.get(key);
                temp.startTime = Instant.now();
                caches.put(key, temp);
                return caches.get(key).value;
            }

        }

        if (m.isAnnotationPresent(Mutator.class)) {
            return method.invoke(fractionable, args);
        }

        return method.invoke(fractionable, args);
    }
}
