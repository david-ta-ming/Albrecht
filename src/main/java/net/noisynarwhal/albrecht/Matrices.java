/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author lioudt
 */
public class Matrices {

    /**
     * Copy the given matrix
     *
     * @param values
     * @return
     */
    public static int[][] copy(int[][] values) {

        final int[][] copy = new int[values.length][];

        for (int r = 0; r < values.length; r++) {

            final int[] rowSrc = values[r];
            final int[] rowCopy = new int[rowSrc.length];

            System.arraycopy(rowSrc, 0, rowCopy, 0, rowSrc.length);

            copy[r] = rowCopy;
        }

        return copy;
    }

    /**
     *
     * @param square
     * @return
     */
    public static int[][] transpose(int[][] square) {

        final int[][] sq = Matrices.copy(square);

        for (int i = 0; i < sq.length; i++) {
            for (int j = i; j < sq.length; j++) {

                if (i != j) {
                    final int v = sq[i][j];
                    sq[i][j] = sq[j][i];
                    sq[j][i] = v;
                }
            }
        }

        return sq;
    }

    /**
     *
     * @param square
     * @return
     */
    public static int[][] mirror(int[][] square) {

        final int[][] sq = Matrices.copy(square);

        for (final int[] row : sq) {

            int a = 0;
            int b = row.length - 1;
            while (a < b) {
                final int v = row[a];
                row[a] = row[b];
                row[b] = v;
                a++;
                b--;
            }

        }

        return sq;
    }

    /**
     *
     * @param square
     * @return
     */
    public static int[][] rotate(int[][] square) {

        return Matrices.mirror(Matrices.transpose(square));
    }

    /**
     *
     * @param values
     * @param out
     * @throws IOException
     */
    public static void print(int[][] values, Writer out) throws IOException {

        int maxVal = values[0][0];
        for (final int[] row : values) {
            for (int c = 0; c < row.length; c++) {

                final int v = row[c];
                maxVal = v > maxVal ? v : maxVal;

            }
        }
        /**
         * For visual formatting, length of max value plus 1 as separator
         */
        final int padLen = (int) (Math.log10(maxVal) + 1) + 1;
        final String padFormat = "%1$" + padLen + "s";

        try (final BufferedWriter writer = (out instanceof BufferedWriter) ? (BufferedWriter) out : new BufferedWriter(out)) {

            /**
             * Write first line then write subsequent lines preceded by newline
             */
            {
                final int[] row = values[0];
                for (int c = 0; c < row.length; c++) {
                    final int v = row[c];
                    final String s = String.format(padFormat, Integer.toString(v));
                    writer.append(s);
                }
            }

            for (int r = 1; r < values.length; r++) {

                writer.newLine();

                final int[] row = values[r];

                for (int c = 0; c < row.length; c++) {
                    final int v = row[c];
                    final String s = String.format(padFormat, Integer.toString(v));
                    writer.append(s);
                }

            }

            writer.flush();
        }
    }

    /**
     *
     * @param values
     * @return
     */
    public static String print(int[][] values) {

        try {
            final StringWriter writer = new StringWriter();
            Matrices.print(values, writer);

            /**
             * Remove last newline
             */
            return writer.toString();
        } catch (IOException ex) {
            /**
             * SHiould not happen with StringWriter
             */
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param values1
     * @param values2
     * @return
     */
    public static boolean valuesEqual(int[][] values1, int[][] values2) {

        if (values1.length != values2.length) {
            return false;
        }

        for (int r = 0; r < values1.length; r++) {
            for (int c = 0; c < values1.length; c++) {
                if (values1[r][c] != values2[r][c]) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     *
     * @param form
     * @return
     */
    public static int[][] standardize(int[][] form) {

        final SortedSet<int[][]> forms = new TreeSet<>(new Comparator<int[][]>() {
            @Override
            public int compare(int[][] mtrx1, int[][] mtrx2) {
                int rank = 0;
                for (int r = 0; r < mtrx1.length && rank == 0; r++) {
                    for (int c = 0; c < mtrx1.length && rank == 0; c++) {
                        final int v1 = mtrx1[r][c];
                        final int v2 = mtrx2[r][c];

                        rank = v1 - v2;
                    }

                }
                return rank;
            }
        });

        forms.add(form);

        for (int i = 0; i < 3; i++) {
            form = Matrices.rotate(form);
            forms.add(form);
        }

        form = Matrices.mirror(form);
        forms.add(form);

        for (int i = 0; i < 3; i++) {
            form = Matrices.rotate(form);
            forms.add(form);
        }

        return forms.first();
    }
}
