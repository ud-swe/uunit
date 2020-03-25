package uunit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  Class for running unit tests with support for expected exceptions.
 */
public class ExpectedExceptionHandlingTestRunner extends TestRunner {

    /**
     * Creates a {@code ExpectedExceptionHandlingTestRunner} object for
     * executing the test methods of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */

    public ExpectedExceptionHandlingTestRunner(Class<?> testClass) {
        super(testClass);
    }

    public void runTestMethods() {
        try {
            int numberOfTests = 0, numberOfFailures = 0, numberOfErrors = 0;
            for (Method method : getAnnotatedMethods(Test.class)) {
                System.out.println(method);
                Object instance = testClass.getDeclaredConstructor().newInstance();
                Test testAnnotation = method.getAnnotation(Test.class);
                try {
                    method.invoke(instance);
                    if (! testAnnotation.expected().equals(Test.None.class)) {
                        // exception is not thrown
                        throw new InvocationTargetException(new AssertionError(testAnnotation.expected().getName() + " is not thrown"));
                    }
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    cause.printStackTrace(System.out);
                    if (cause instanceof AssertionError) {
                        numberOfFailures++;
                    } else {
                        if (testAnnotation.expected().equals(Test.None.class)) {
                            numberOfErrors++;
                        } else {
                            if (cause.getClass().equals(testAnnotation.expected())) {
                                // OK
                            } else {
                                numberOfErrors++;;
                            }
                        }
                    }
                }
                numberOfTests++;
            }
            System.out.printf("Tests run: %d\n", numberOfTests);
            System.out.printf("Failures: %d\n", numberOfFailures);
            System.out.printf("Errors: %d\n", numberOfErrors);
        } catch (ReflectiveOperationException e) {
            throw new InvalidTestClassException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Class testClass = Class.forName(args[0]);
        new ExpectedExceptionHandlingTestRunner(testClass).runTestMethods();
    }

}
