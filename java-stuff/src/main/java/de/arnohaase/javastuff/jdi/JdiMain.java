package de.arnohaase.javastuff.jdi;

//import com.sun.jdi.*;
//import com.sun.jdi.connect.Connector;
//import com.sun.jdi.connect.LaunchingConnector;

import java.util.Map;

/**
 * @author arno
 */
public class JdiMain {
//    public static void main(String[] args) throws Exception {
//        final VirtualMachine vm = launchAndAttach();
//
//        System.out.println("Threads: ");
//        for(ThreadReference thread: vm.allThreads()) {
//            System.out.println("  " + thread);
//        }
//
//        System.out.println("Classes: ");
//        for(ReferenceType tpe: vm.allClasses()) {
//            System.out.println("  " + tpe + ": " + tpe.instances(1000).size());
//        }
//
//        System.out.println("strings");
//        for(ReferenceType tpe: vm.classesByName("java.lang.String")) {
//            for(ObjectReference ref: tpe.instances(1000)) {
//                System.out.println("  " + ref);
//            }
//        }
//
//        vm.dispose();
//    }
//
//    static VirtualMachine launchAndAttach() throws Exception {
////        for(Connector conn: Bootstrap.virtualMachineManager().allConnectors()) {
////            System.out.println(conn.name());
////            System.out.println(conn.description());
////            System.out.println(conn.defaultArguments());
////            System.out.println("--------------------");
////        }
//
//        for(LaunchingConnector conn: Bootstrap.virtualMachineManager().launchingConnectors()) {
//
//            if("com.sun.jdi.CommandLineLaunch".equals(conn.name())) {
//                final Map<String, Connector.Argument> argMap = conn.defaultArguments();
//                argMap.get("main").setValue(DebuggeeMain.class.getName());
////                argMap.get("suspend").setValue("false");
//
//                return conn.launch(argMap);
//            }
//        }
//        return null;
//    }
}
