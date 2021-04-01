package com.kovalevaelena371;

import com.kovalevaelena371.annotation.After;
import com.kovalevaelena371.annotation.Before;
import com.kovalevaelena371.annotation.Test;

import static com.kovalevaelena371.assertions.Assertions.assertEquals;
import static com.kovalevaelena371.assertions.Assertions.assertTrue;


public class Test1 {

    @Before
    public void firstBeforeMethod() {
        System.out.println("firstBeforeMethod form Test1");
    }

    @Before
    public void secondBeforeMethod() {
        System.out.println("secondBeforeMethod form Test1");
    }

    @Test
    public void firstTest() {
        TestedClass1 testedClass = new TestedClass1();
        assertEquals(testedClass.One(), testedClass.Two());
    }

    @Test(expected = NullPointerException.class)
    public void secondTest() {
        throw new NullPointerException();
    }

    @Test
    public void thirdTest() {
        TestedClass1 testedClass = new TestedClass1();
        assertTrue(testedClass.One() == testedClass.Two());
    }

    @After
    public void firstAfterMethod() {
        System.out.println("firstAfterMethod form Test1");
    }

    @After
    public void secondAfterMethod() {
        System.out.println("secondAfterMethod from Test1");
    }
}
