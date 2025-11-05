package strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Alexis Englebert
 *
 * Context: Oh no, there’s a bomb in your kitchen!
 * You must defuse it before it explodes. To do so, you need to solve a puzzle.
 * You have a grid with n rows and m columns. Each cell contains a color.
 * Each row has two arrows, one pointing left and the other pointing right.
 * When you press the right arrow, all the colors in the row are shifted by one to the right.
 * When you press the left arrow, they are shifted by one to the left.
 *
 * The rows wrap around (are cyclic), so if a color moves off one side of a row,
 * it re-enters from the other side.
 *
 * Your goal is to find two rows that are identical after entering a sequence of arrows.
 * If you find two identical rows, you have defused the bomb.
 * If you can’t find two identical rows, the bomb explodes!
 *
 * You must return the indices of the two identical rows in increasing order.
 * If there are multiple pairs, return the one with the smallest first index.
 * If two pairs have the same first index,
 * return the one with the smallest second index.
 * If you don’t find two identical rows, return [-1, -1].
 *
 * Time complexity O(n · m)
 *
 * Exemple:
 * n = 4, m = 5
 * RGRBB
 * RBBGB
 * BBBRB
 * GRBBR
 *
 * The answer is the pair (1, 4) because after one shift to the right on the 4th row,
 * they are equals (GRBBR -> RGRBB)
 */

public class Bombe {
   // BEGIN STRIP
    static final long MOD = (1L << 60);
    static final long A = 91138;

    static long safeMod(long a) {
        return (a % Bombe.MOD + Bombe.MOD) % Bombe.MOD;
    }

    // END STRIP

    /**
     * @param n     The number of columns in the grid
     * @param m     The number of rows in the grid
     * @param grid  The input grid
     * @return      A list of 2 elements representing the indices of the two rows, or [-1, -1]
     *              if no rows are the same.
     */
    public static int[] solve(int n, int m, char [][] grid) {
        //TODO
        // STUDENT return -1;
        // BEGIN STRIP
        long[] baseExponent = new long[m];
        baseExponent[0] = 1;

        for (int i = 1; i < m; i++) {
            baseExponent[i] = safeMod(baseExponent[i - 1] * A);
        }

        long[] hahes = new long[n];

        Map<Long, Pair<Integer, Integer>> cnt = new HashMap<>();

        for (int i = 0; i < n; i++) {
            long hash = 0;

            for (int j = 0; j < m; j++) {
                hash += grid[i][j] * baseExponent[j];
                hash = safeMod(hash);
            }
            hahes[i] = hash;
            if(cnt.containsKey(hash)) {
                cnt.get(hash).first = Math.min(i+1, cnt.get(hash).first);
            }else {
                cnt.put(hahes[i], new Pair<>(0, i + 1));
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                hahes[i] -= (grid[i][m - j - 1] * baseExponent[m - 1]) % MOD;
                hahes[i] %= MOD;
                hahes[i] *= A;
                hahes[i] %= MOD;
                hahes[i] += grid[i][m - j - 1];
                hahes[i] %= MOD;

                cnt.computeIfAbsent(hahes[i], k -> new Pair<>(0, 0));
                Pair<Integer, Integer> pair = cnt.get(hahes[i]);

                if (pair.first == 0 && (i+1) != pair.second) {
                    pair.first = i + 1;
                } else if (pair.second == 0 && (i+1) != pair.first) {
                    pair.second = i + 1;
                } else if((i+1) < pair.first && (i+1) != pair.second) {
                    pair.first = i+1;
                } else if((i+1) < pair.second && (i+1) != pair.first) {
                    pair.second = i+1;
                }

                int temp = pair.first;
                pair.first = Math.min(pair.first, pair.second);
                pair.second = Math.max(temp, pair.second);
                cnt.replace(hahes[i], pair);
            }
        }

        Pair<Integer, Integer> mini = new Pair<>(10_000_000_00, 10_000_000_00);

        for (Map.Entry<Long, Pair<Integer, Integer>> entry : cnt.entrySet()) {
            Pair<Integer, Integer> value = entry.getValue();

            if (value.first == 0 || value.second == 0) continue;
            if (value.compareTo(mini) < 0) mini = value;
        }

        if (mini.first == 10_000_000_00 && mini.second == 10_000_000_00) {
            return new int[]{-1, -1};
        } else {
            System.out.println(mini.first + " " + mini.second);
            return new int[]{mini.first, mini.second};
        }
        // END STRIP
    }

    // BEGIN STRIP
    static class Pair<T extends Comparable<T>, U extends Comparable<U>> implements Comparable<Pair<T, U>> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(Pair<T, U> other) {
            int cmp = this.first.compareTo(other.first);
            if (cmp != 0) {
                return cmp;
            }
            return this.second.compareTo(other.second);
        }
    }
    // END STRIP
}


