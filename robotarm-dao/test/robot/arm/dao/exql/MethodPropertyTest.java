package robot.arm.dao.exql;
import junit.framework.TestCase;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 *  Simple example to show how to access method and properties.
 *
 *  @since 1.0
 */
public class MethodPropertyTest extends TestCase {
    /**
     * An example for method access.
     */
    public static void example(final Output out) throws Exception {
        /**
         * First step is to retrieve an instance of a JexlEngine;
         * it might be already existing and shared or created anew.
         */
        JexlEngine jexl = new JexlEngine();
        /*
         *  Second make a jexlContext and put stuff in it
         */
        JexlContext jc = new MapContext();

        /**
         * The Java equivalents of foo and number for comparison and checking
         */
        Foo foo = new Foo();
        Integer number = new Integer(10);

        jc.set("foo", foo);
        jc.set("number", number);

        /*
         *  access a method w/o args
         */
        Expression e = jexl.createExpression("foo.getFoo()");
        Object o = e.evaluate(jc);
        out.print("value returned by the method getFoo() is : ", o, foo.getFoo());

        /*
         *  access a method w/ args
         */
        e = jexl.createExpression("foo.convert(1)");
        o = e.evaluate(jc);
        out.print("value of " + e.getExpression() + " is : ", o, foo.convert(1));

        e = jexl.createExpression("foo.convert(1+7)");
        o = e.evaluate(jc);
        out.print("value of " + e.getExpression() + " is : ", o, foo.convert(1+7));

        e = jexl.createExpression("foo.convert(1+number)");
        o = e.evaluate(jc);
        out.print("value of " + e.getExpression() + " is : ", o, foo.convert(1+number.intValue()));

        /*
         * access a property
         */
        e = jexl.createExpression("foo.bar");
        o = e.evaluate(jc);
        out.print("value returned for the property 'bar' is : ", o, foo.get("bar"));

    }

    /**
     * Helper example class.
     */
    public static class Foo {
        /**
         * Gets foo.
         * @return a string.
         */
        public String getFoo() {
            return "This is from getFoo()";
        }

        /**
         * Gets an arbitrary property.
         * @param arg property name.
         * @return arg prefixed with 'This is the property '.
         */
        public String get(String arg) {
            return "This is the property " + arg;
        }

        /**
         * Gets a string from the argument.
         * @param i a long.
         * @return The argument prefixed with 'The value is : '
         */
        public String convert(long i) {
            return "The value is : " + i;
        }
    }


    /**
     * Unit test entry point.
     * @throws Exception
     */
    public void testExample() throws Exception {
        example(Output.JUNIT);
    }

    /**
     * Command line entry point.
     * @param args command line arguments
     * @throws Exception cos jexl does.
     */
    public static void main(String[] args) throws Exception {
        example(Output.SYSTEM);
    }
}