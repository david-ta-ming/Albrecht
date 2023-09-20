/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package net.noisynarwhal.albrecht.square;

import java.util.Random;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lioudt
 */
public class EvolutionsTest {

    public EvolutionsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evolve method, of class Evolutions.
     */
    @Test
    public void testEvolve_int() {
        System.out.println("evolve");
        for (int i = 0; i < 5; i++) {
            int order = 15;

            System.out.println("");
            System.out.println("Order: " + Integer.toString(order));

            Magic result = Evolutions.evolve(order);

            System.out.println(Matrices.print(result.getValues()));
            System.out.println("");

            assertTrue(Matrices.isMagic(result.getValues()));
        }
    }

}
