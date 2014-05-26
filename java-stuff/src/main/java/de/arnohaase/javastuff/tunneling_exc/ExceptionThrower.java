package de.arnohaase.javastuff.tunneling_exc;

/**
 * @author arno
 */
public class ExceptionThrower {
    public static void handle(Exception exc) {
        if(exc instanceof TunnelingException) {
            throw (TunnelingException) exc;
        }
        throw new TunnelingException(exc);
    }
}
