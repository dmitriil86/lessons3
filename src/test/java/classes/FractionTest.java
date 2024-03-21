package classes;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class FractionTest {


    @Test
    void Cache() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Fraction fraction = new Fraction(20,40);

        ProxyFractionable fractionable = new ProxyFractionable(fraction);
        Fractionable pFractionable = Utils.cache(fraction, fractionable);
        Class aClass = fractionable.getClass();

        Field field = aClass.getDeclaredField("caches");
        field.setAccessible(true);

        pFractionable.doubleValue();
        ConcurrentMap<String, ProxyFractionable.Info> resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(1, resultCache.size());

        sleep(2000);
        pFractionable.doubleValue();
        resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(1, resultCache.size());

        sleep(8000);
        pFractionable.doubleValue();
        resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(1, resultCache.size());

        sleep(11000);
        resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(0, resultCache.size());

        pFractionable.setNum(10);
        pFractionable.doubleValue();

        pFractionable.setDenum(10);
        pFractionable.doubleValue();

        resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(2, resultCache.size());

        sleep(11000);
        resultCache = (ConcurrentMap<String, ProxyFractionable.Info>) field.get(fractionable);
        assertEquals(0, resultCache.size());



    }

}