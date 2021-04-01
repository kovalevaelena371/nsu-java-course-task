package com.kovalevaelena371;

import com.kovalevaelena371.annotation.After;
import com.kovalevaelena371.annotation.Before;
import com.kovalevaelena371.annotation.Test;

import static com.kovalevaelena371.assertions.Assertions.assertEquals;
import static com.kovalevaelena371.assertions.Assertions.assertTrue;


public class Test2 {

    @Before
    public void firstBeforeMethod() {
        System.out.println("firstBeforeMethod form Test2");
    }

    @Before
    public void secondBeforeMethod() {
        System.out.println("secondBeforeMethod form Test2");
    }

    @Test
    public void firstTest() {
        TestedClass2 testedClass = new TestedClass2();
        assertEquals(testedClass.Four(), testedClass.Three());
    }


    @Test(expected = NullPointerException.class)
    public void secondTest() {
        var testArray = new int[10];
        int i = testArray[-1]; // throw ArrayIndexOutOfBoundsException
    }

    @Test
    public void thirdTest() {
        TestedClass2 testedClass = new TestedClass2();
        assertTrue(testedClass.Four() == testedClass.Three());
    }

    @After
    public void firstAfterMethod() {
        System.out.println("firstAfterMethod form Test2");
    }

    @After
    public void secondAfterMethod() {
        System.out.println("secondAfterMethod from Test2");
    }
}