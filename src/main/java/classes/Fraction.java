package classes;

import annotations.Cache;
import annotations.Mutator;

public class Fraction implements Fractionable{
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(time = "10000")
    public double doubleValue() {
        System.out.println("invoke double value");
        return (double) num/denum;
    }
}

