/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht.square;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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

        final int[][] matrix = new int[][]{{1, 2}, {3, 4}};
        final int[][] expected = new int[][]{{1, 3}, {2, 4}};

        final int[][] result = Matrices.transpose(matrix);

        Assert.assertArrayEquals(expected, result);
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
    public void testRead() {
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

    /**
     * Test of switchValues method, of class Matrices.
     */
    @Test
    public void testSwitchValues() {
        System.out.println("switchValues");

    }

    /**
     * Test of switchCols method, of class Matrices.
     */
    @Test
    public void testSwitchCols() {
        System.out.println("switchCols");

    }

    /**
     * Test of switchRows method, of class Matrices.
     */
    @Test
    public void testSwitchRows() {
        System.out.println("switchRows");

    }

    /**
     * Test of isBiMagic method, of class Matrices.
     */
    @Test
    public void testIsBiMagic() {
        System.out.println("isBiMagic");
        {
            int[][] matrix = new int[][]{
                {17, 36, 55, 124, 62, 114},
                {58, 40, 129, 50, 111, 20},
                {108, 135, 34, 44, 38, 49},
                {87, 98, 92, 102, 1, 28},
                {116, 25, 86, 7, 96, 78},
                {22, 74, 12, 81, 100, 119}
            };
            boolean expResult = true;
            boolean result = Matrices.isBiMagic(matrix);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{
                {23, 30, 14, 37, 44, 10, 17},
                {27, 45, 8, 18, 9, 46, 22},
                {1, 19, 16, 41, 28, 34, 36},
                {32, 20, 49, 13, 3, 47, 11},
                {21, 25, 7, 26, 43, 5, 48},
                {31, 24, 39, 2, 15, 29, 35},
                {40, 12, 42, 38, 33, 4, 6}
            };
            boolean expResult = false;
            boolean result = Matrices.isBiMagic(matrix);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{
                {1, 2, 37, 43, 40, 52, 47, 38},
                {62, 15, 12, 13, 27, 32, 41, 58},
                {14, 35, 30, 28, 24, 63, 7, 59},
                {25, 54, 53, 48, 5, 22, 44, 9},
                {34, 31, 51, 39, 60, 3, 6, 36},
                {49, 50, 10, 21, 20, 45, 57, 8},
                {29, 18, 56, 4, 61, 17, 42, 33},
                {46, 55, 11, 64, 23, 26, 16, 19}
            };

            boolean expResult = true;
            boolean result = Matrices.isBiMagic(matrix);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of isConsecutiveUnique method, of class Matrices.
     */
    @Test
    public void testIsConsecutiveUnique() {
        System.out.println("isConsecutiveUnique");
        {
            int[][] matrix = new int[][]{
                {1, 2, 37, 43, 40, 52, 47, 38},
                {62, 15, 12, 13, 27, 32, 41, 58},
                {14, 35, 30, 28, 24, 63, 7, 59},
                {25, 54, 53, 48, 5, 22, 44, 9},
                {34, 31, 51, 39, 60, 3, 6, 36},
                {49, 50, 10, 21, 20, 45, 57, 8},
                {29, 18, 56, 4, 61, 17, 42, 33},
                {46, 55, 11, 64, 23, 26, 16, 19}
            };
            boolean expResult = true;
            boolean result = Matrices.isConsecutiveUnique(matrix, 1);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
            boolean expResult = true;
            boolean result = Matrices.isConsecutiveUnique(matrix, 0);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            boolean expResult = false;
            boolean result = Matrices.isConsecutiveUnique(matrix, 0);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{{1, 1, 3}, {4, 5, 6}, {7, 8, 9}};
            boolean expResult = false;
            boolean result = Matrices.isConsecutiveUnique(matrix, 1);
            assertEquals(expResult, result);
        }
        {
            int[][] matrix = new int[][]{{0, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            boolean expResult = false;
            boolean result = Matrices.isConsecutiveUnique(matrix, 0);
            assertEquals(expResult, result);
        }

    }

}
