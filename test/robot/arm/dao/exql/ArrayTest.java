package robot.arm.dao.exql;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 *  Simple example to show how to access arrays.
 *
 *  @since 1.0
 */
public class ArrayTest extends TestCase {
    /**
     * An example for array access.
     */
    static void example(Output out) throws Exception {
        /**
         * First step is to retrieve an instance of a JexlEngine;
         * it might be already existing and shared or created anew.
         */
        JexlEngine jexl = new JexlEngine();
        /*
         *  Second make a jexlContext and put stuff in it
         */
        JexlContext jc = new MapContext();

        List<Object> l = new ArrayList<Object>();
        l.add("Hello from location 0");
        Integer two = new Integer(2);
        l.add(two);
        jc.set("array", l);

        Expression e = jexl.createExpression("array[1]");
        Object o = e.evaluate(jc);
        out.print("Object @ location 1 = ", o, two);

        e = jexl.createExpression("array[0].length()");
        o = e.evaluate(jc);

        out.print("The length of the string at location 0 is : ", o, Integer.valueOf(21));
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