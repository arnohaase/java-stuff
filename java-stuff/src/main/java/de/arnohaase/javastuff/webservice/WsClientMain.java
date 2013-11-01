package de.arnohaase.javastuff.webservice;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * @author arno
 */
public class WsClientMain {
    public static void main(String[] args) throws Exception {
        final Service service = Service.create(new URL("http://localhost:8888/greeter?wsdl"), new QName("http://webservice.javastuff.arnohaase.de/", "GreeterImplService"));
        final Greeter greeter = service.getPort(Greeter.class);
        System.out.println(greeter.greeting("Welt"));
        System.out.println(greeter.greeting("ihr alle"));
    }
}
