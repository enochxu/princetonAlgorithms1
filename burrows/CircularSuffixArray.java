import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.In;

public class CircularSuffixArray {

    private final int len;
    private final String str;
    private final CircularSuffix[] circularInds;
    
    private class CircularSuffix implements Comparable<CircularSuffix> {
        public final int ind;

        CircularSuffix(int ind) {
            this.ind = ind;
        }

        public int compareTo(CircularSuffix that) {
            return compare(this.ind, that.ind, 0);
        }

        private int compare(int ind1, int ind2, int count) {
            int comp = str.charAt(ind1) - str.charAt(ind2);
            if (count == str.length() || comp != 0) {
                return comp;
            } else {
                int nextInd1 = ind1 + 1;
                int nextInd2 = ind2 + 1;
                if (nextInd1 == str.length())
                    nextInd1 = 0;
                if (nextInd2 == str.length())
                    nextInd2 = 0;
                count++;
                return compare(nextInd1, nextInd2, count);
            } 
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("String is null.");
        
        str = new String(s);
        len = str.length();
        circularInds = new CircularSuffix[str.length()];
        for (int i = 0; i < str.length(); i++) {
            circularInds[i] = new CircularSuffix(i);
        }
        Quick.sort(circularInds);
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= circularInds.length)
            throw new IllegalArgumentException("Index out of bounds.");
        return circularInds[i].ind;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        CircularSuffixArray test = new CircularSuffixArray(in.readString());
        // CircularSuffixArray test = new CircularSuffixArray(args[0]);
        System.out.println(test.length());
        for (int i = 0; i < test.length(); i++) {
            System.out.println(test.index(i));
        }
        System.out.println(test.index(2));
    }
}