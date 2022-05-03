package org.line.com;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple springPointApplication.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static void main(String[] args) {
        A a = new B();
        System.out.println(a.name);
        a.run();
    }
}
