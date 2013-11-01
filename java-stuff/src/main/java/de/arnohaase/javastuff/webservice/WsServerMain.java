package de.arnohaase.javastuff.webservice;

import javax.xml.ws.Endpoint;

/**
 * @author arno
 */
public class WsServerMain {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/greeter", new GreeterImpl());
        System.out.println("Webservice started");
    }
}
