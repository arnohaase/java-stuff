package de.arnohaase.javastuff.ternary_npe_promotion;

/**
 * @author arno
 */
public class TernaryNpePromotion {
    public static void main (String[] args) {
        Integer i = null;
        Long j = 2L;

        Object o = System.currentTimeMillis () > 0 ? i : j;
    }
}
