/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht.square;

import java.io.Reader;
import java.io.Writer;
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
public class MatricesTest {

    public MatricesTest() {
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
     * Test of copy method, of class Matrices.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
    }

    /**
     * Test of transpose method, of class Matrices.
     */
    @Test
    public void testTranspose() {
        System.out.println("transpose");
    }

    /**
     * Test of mirror method, of class Matrices.
     */
    @Test
    public void testMirror() {
        System.out.println("mirror");
    }

    /**
     * Test of rotate method, of class Matrices.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
    }

    /**
     * Test of print method, of class Matrices.
     */
    @Test
    public void testPrint_intArrArr_Writer() {
        System.out.println("print");
    }

    /**
     * Test of print method, of class Matrices.
     */
    @Test
    public void testPrint_intArrArr() {
        System.out.println("print");
    }

    /**
     * Test of valuesEqual method, of class Matrices.
     */
    @Test
    public void testValuesEqual() {
        System.out.println("valuesEqual");
    }

    /**
     * Test of standardize method, of class Matrices.
     */
    @Test
    public void testStandardize() {
        System.out.println("standardize");
    }

    /**
     * Test of read method, of class Matrices.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
    }

    /**
     * Test of isMagic method, of class Matrices.
     */
    @Test
    public void testIsMagic() {
        System.out.println("isMagic");
                {
            int[][] matrix = new int[][]{
                {1, 44, 71, 13, 12, 46, 76, 41, 50},
                {65, 51, 2, 11, 80, 43, 25, 47, 45},
                {19, 63, 68, 35, 49, 1, 34, 30, 70},
                {59, 26, 73, 31, 81, 42, 23, 7, 27},
                {39, 57, 72, 40, 33, 37, 5, 28, 58},
                {74, 32, 22, 69, 17, 53, 8, 79, 15},
                {10, 29, 9, 56, 67, 18, 77, 55, 48},
                {66, 64, 14, 62, 24, 54, 61, 4, 20},
                {21, 3, 38, 52, 6, 75, 60, 78, 36}};
            boolean expResult = false;
            boolean result = Matrices.isMagic(matrix);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{
                {16, 44, 71, 13, 12, 46, 76, 41, 50},
                {65, 51, 2, 11, 80, 43, 25, 47, 45},
                {19, 63, 68, 35, 49, 1, 34, 30, 70},
                {59, 26, 73, 31, 81, 42, 23, 7, 27},
                {39, 57, 72, 40, 33, 37, 5, 28, 58},
                {74, 32, 22, 69, 17, 53, 8, 79, 15},
                {10, 29, 9, 56, 67, 18, 77, 55, 48},
                {66, 64, 14, 62, 24, 54, 61, 4, 20},
                {21, 3, 38, 52, 6, 75, 60, 78, 36}};
            boolean expResult = true;
            boolean result = Matrices.isMagic(matrix);
            assertEquals(expResult, result);
        }
    }

}
