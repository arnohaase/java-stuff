package a;


public class ExceptionsLangsam {
    private int counter = 0;
    
    public static void main(String[] args) {
        final ExceptionsLangsam el = new ExceptionsLangsam();
        
        final long start = System.currentTimeMillis(); 
        for (int i=0; i<10*1000*1000; i++) {
            try {
                el.calc ();
            }
            catch (Exception exc) {
            }
        }
        System.out.println(el.counter);
        final long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
    }
    
    public void calc () {
        for (int i=0; i<1000; i++) {
            counter++;
            if (i == 500) {
//                new RuntimeException ();
                throw new RuntimeException () {
                    @Override
                    public Throwable fillInStackTrace() {
                        return this;
                    }  
                };
//                return;
            }
        }
    }
}


// 10*1000*1000 Wiederholungen (try - catch macht keinen Unterschied) 

//JDK 1.5 (static / non-static macht dagegen Unterschied von 5%!)
// komplett ohne Exceptions:              2.817 ms
// mit "new RuntimeException ()":        10.595 ms
// mit "throw new RuntimeException ()":  10.338 ms
// mit "throw new ... fillInStackTrace":  2.867 ms

//JDK 1.6_27
// komplett ohne Exceptions:              2.984 ms
// mit "new RuntimeException ()":         8.762 ms
// mit "throw new RuntimeException ()":   8.773 ms
// mit "throw new ... fillInStackTrace":  3.176 ms

//JDK 1.7.0
// komplett ohne Exceptions:              2.709 ms
// mit "new RuntimeException ()":         8.349 ms
// mit "throw new RuntimeException ()":   8.113 ms
// mit "throw new ... fillInStackTrace":  2.829 ms
