package de.arnohaase.javastuff.webservice;

import javax.jws.WebService;

/**
 * @author arno
 */
@WebService(name = "GreeterService")
public interface Greeter {
    String greeting (String name);
}
