import classes.*;

import static java.lang.Thread.sleep;

public class lesson3 {

    public static void main(String[] args) throws InterruptedException {
        Fraction fraction = new Fraction(10,20);

        Fractionable pFractionable = Utils.cache(fraction, new ProxyFractionable(fraction));

        pFractionable.doubleValue();
        System.out.println("1");
        sleep(5000);
        pFractionable.doubleValue();
        System.out.println("2");
        sleep(10000);
        pFractionable.doubleValue();
        System.out.println("3");
        pFractionable.setNum(20);
        pFractionable.doubleValue();
        sleep(12000);
        pFractionable.doubleValue();

    }
}
