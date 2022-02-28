/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import net.noisynarwhal.albrecht.square.Magic;
import net.noisynarwhal.albrecht.square.Matrices;

/**
 *
 * @author lioudt
 */
public class NewClass {

    public static void main(String[] args) throws Exception {
        final File dir = new File("/Users/lioudt/Documents/MagicSquares/");

        final File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });

        for (final File fin : files) {

            int[][] matrix;

            System.out.println("In: " + fin.getAbsolutePath());

            try (final Reader reader = new FileReader(fin)) {
                matrix = Matrices.read(reader);
            }

            matrix = Matrices.standardize(matrix);

            final Magic m = Magic.build(matrix);
            if (Matrices.isMagic(m.getValues())) {

                final File fout = NewClass.saveResults(matrix, dir);

                System.out.println("Out: " + fout.getAbsolutePath());

                if (!fin.getName().equalsIgnoreCase(fout.getName())) {
                    System.err.println("Deleting original: " + fin.getAbsolutePath());
                    Thread.sleep(1000);
                    fin.delete();
                }
            } else {
                System.err.println("\n*** Not magic: " + fin.getAbsolutePath() + " ***\n");
                Thread.sleep(1000);
                fin.delete();
            }
        }

    }

    private static File saveResults(final int[][] standardValues, File saveDir) throws IOException {

        final File saveFile;

        final StringBuilder filename = new StringBuilder();

        final Hasher valuesHash = Hashing.farmHashFingerprint64().newHasher();
        for (final int[] row : standardValues) {
            for (final int v : row) {
                valuesHash.putInt(v);
            }
        }
        final int[] row0 = standardValues[0];
        final int order = row0.length;

        filename.append(Integer.toString(order));
        filename.append('_');
        filename.append(Integer.toString(row0[0]));
        filename.append('-');
        filename.append(Integer.toString(row0[1]));
        filename.append('-');
        filename.append(Integer.toString(row0[2]));
        filename.append('_');
        filename.append(valuesHash.hash().toString());
        filename.append(".txt");

        saveFile = new File(saveDir, filename.toString());
        try (final Writer writer = new FileWriter(saveFile)) {
            Matrices.print(standardValues, writer);
        }

        return saveFile;

    }
}
