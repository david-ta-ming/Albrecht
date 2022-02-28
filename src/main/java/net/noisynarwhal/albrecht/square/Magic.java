/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisynarwhal.albrecht.square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author lioudt
 */
public class Magic implements Comparable<Magic> {

    private static final int EXCHANGE_RANDOM_SPACE = 75;
    private static final AtomicLong SERIAL_COUNTER = new AtomicLong(0);

    private final Random RANDOM = ThreadLocalRandom.current();
    private final int order;
    private final int[][] values;
    private final int score;
    private final int maxScore;
    private final int hashCode;
    private final boolean isSemiMagic;
    private final boolean isMagic;
    private final long serial = SERIAL_COUNTER.getAndIncrement();

    /**
     *
     * @param order
     * @return
     */
    public static Magic generate(final int order) {

        final int[][] values = new int[order][order];

        final List<Integer> l = new ArrayList<>();
        for (int i = 1; i <= order * order; i++) {
            l.add(i);
        }

        Collections.shuffle(l, ThreadLocalRandom.current());

        final Iterator<Integer> it = l.iterator();

        for (int r = 0; r < order; r++) {
            for (int c = 0; c < order; c++) {
                values[r][c] = it.next();
                it.remove();
            }

        }

        return new Magic(values);
    }

    public static Magic generate(int[][] values) {

        return new Magic(Matrices.copy(values));
    }

    private Magic(int[][] values) {

        this.values = values;
        this.order = values.length;

        int hash = 7;
        int scoreSum = 0;
        final int magicSum = this.order * (this.order * this.order + 1) / 2;

        /**
         * Score of left-to-right diagonal
         */
        int sumlr = 0;
        /**
         * Score of right-to-left diagonal
         */
        int sumrl = 0;

        for (int i = 0; i < this.order; i++) {
            /**
             * Score of rows
             */
            int sumRow = 0;
            /**
             * Score of cols
             */
            int sumCol = 0;

            for (int j = 0; j < this.order; j++) {
                sumRow += this.values[i][j];
                sumCol += this.values[j][i];

                hash = (hash << 5) | (hash >>> 27);
                hash += this.values[i][j];
            }

            if (magicSum == sumRow) {
                scoreSum++;
            }
            if (magicSum == sumCol) {
                scoreSum++;
            }

            sumlr += this.values[i][i];
            sumrl += this.values[i][this.order - 1 - i];

        }

        this.maxScore = this.order + this.order + 2;

        this.isSemiMagic = scoreSum == this.maxScore - 2;

        if (magicSum == sumlr) {
            scoreSum++;
        }
        if (magicSum == sumrl) {
            scoreSum++;
        }

        this.hashCode = hash;

        this.isMagic = scoreSum == this.maxScore;

        if (scoreSum == this.maxScore - 1) {
            this.score = - 1;
        } else {
            this.score = scoreSum;
        }

    }

    /**
     *
     * @return
     */
    public Magic newChild() {

        final int[][] childValues = Matrices.copy(this.values);
        
        final int exchangeType = this.isSemiMagic ? RANDOM.nextInt(2) : RANDOM.nextInt(EXCHANGE_RANDOM_SPACE);

        switch (exchangeType) {

            case 0: {
                /**
                 * Row exchange
                 */
                int r1;
                int r2;

                do {
                    r1 = RANDOM.nextInt(this.order);
                    r2 = RANDOM.nextInt(this.order);
                } while (r1 == r2);

                final int[] valsR2 = new int[this.order];
                System.arraycopy(childValues[r2], 0, valsR2, 0, this.order);

                childValues[r2] = childValues[r1];
                childValues[r1] = valsR2;

                break;
            }

            case 1: {
                /**
                 * Col exchange
                 */
                int c1;
                int c2;

                do {
                    c1 = RANDOM.nextInt(this.order);
                    c2 = RANDOM.nextInt(this.order);
                } while (c1 == c2);

                for (int r = 0; r < this.order; r++) {

                    final int v2 = childValues[r][c2];

                    childValues[r][c2] = childValues[r][c1];
                    childValues[r][c1] = v2;

                }

                break;
            }

            default: {
                /**
                 * Value exchange
                 */
                int r1;
                int c1;
                int r2;
                int c2;
                do {
                    r1 = RANDOM.nextInt(this.order);
                    c1 = RANDOM.nextInt(this.order);

                    r2 = RANDOM.nextInt(this.order);
                    c2 = RANDOM.nextInt(this.order);

                } while (r1 == r2 && c1 == c2);

                final int v2 = childValues[r2][c2];

                childValues[r2][c2] = childValues[r1][c1];
                childValues[r1][c1] = v2;
            }
        }

        final Magic child = new Magic(childValues);

        return child;
    }

    /**
     *
     * @return
     */
    public int[][] getValues() {
        return Matrices.copy(this.values);
    }

    /**
     *
     * @return
     */
    public int getOrder() {
        return this.order;
    }

    /**
     *
     * @return
     */
    public int getScore() {
        return this.score;
    }

    /**
     *
     * @return
     */
    public int getMaxScore() {
        return this.maxScore;
    }

    /**
     *
     * @return
     */
    public boolean isMagic() {
        return this.isMagic;
    }

    /**
     *
     * @return
     */
    public long getSerial() {
        return this.serial;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        final boolean equals;

        if (this == obj) {
            equals = true;
        } else if (obj == null) {
            equals = false;
        } else if (getClass() != obj.getClass()) {
            equals = false;
        } else {
            final Magic other = (Magic) obj;
            equals = Matrices.valuesEqual(this.values, other.values);
        }

        return equals;
    }

    @Override
    public int compareTo(Magic other) {

        final int rank;

        if (this == other) {
            rank = 0;
        } else if (this.score != other.score) {
            rank = other.score - this.score;
        } else {
            rank = (other.serial < this.serial) ? -1 : 1;
        }

        return rank;
    }

}
