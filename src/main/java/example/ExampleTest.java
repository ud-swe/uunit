package example;

import uunit.Assert;
import uunit.Test;
import uunit.BasicTestRunner;

import java.io.IOException;

// CHECKSTYLE:OFF
public class ExampleTest {

    public ExampleTest() {}

    @Test
    public void a() {
        Assert.assertTrue(1 != 1, "This always happens");
    } // failure

    @Test
    public void b() {
        Assert.assertTrue(1 == 1,"This should never happen");
    } // success

    @Test(expected = IOException.class)
    public void c() throws Exception {
    } // failure

    @Test(expected = IOException.class)
    public void d() throws Exception {
        throw new IOException("An I/O exception occurred");
    } // success

    @Test(expected = IOException.class)
    public void e() {
        throw new RuntimeException("Oops!");
    } // error

    public void f() {}

    public static void main(String[] args) throws Exception {
        new BasicTestRunner(ExampleTest.class).runTestMethods();
    }

}
