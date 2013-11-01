package de.arnohaase.javastuff.webservice;

import javax.jws.WebService;

/**
 * @author arno
 */
@WebService(endpointInterface = "de.arnohaase.javastuff.webservice.Greeter")
public class GreeterImpl implements Greeter {
    @Override
    public String greeting(String name) {
        System.out.println("request for " + name + " in thread " + Thread.currentThread());
        return "Hallo, " + name + "!";
    }
}
