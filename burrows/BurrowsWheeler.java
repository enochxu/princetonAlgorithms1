import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        // System.out.print("yo");
        String str = BinaryStdIn.readString();
        CircularSuffixArray circular = new CircularSuffixArray(str);

        int i = 0;
        while (i < circular.length() && circular.index(i) != 0)
            i++;
        // first
        BinaryStdOut.write(i);

        for (int j = 0; j < circular.length(); j++) {
            int tInd;
            if (circular.index(j) == 0)
                tInd = circular.length() - 1;
            else
                tInd = circular.index(j) - 1;
            BinaryStdOut.write(str.charAt(tInd));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        CircularSuffixArray next = new CircularSuffixArray(t);

        // Stack<Character> msg = new Stack<Character>();
        int c = next.index(first);
        for (int i = 0; i < t.length(); i++) {
            BinaryStdOut.write(t.charAt(c));
            c = next.index(c);
        }

        // for (int i = 0; i < t.length(); i++) {
        //     BinaryStdOut.write(msg.pop());
        // }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }
}