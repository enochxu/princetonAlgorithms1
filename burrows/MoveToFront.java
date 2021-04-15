import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;

public class MoveToFront {

    private final static int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	int[] pos = new int[R];
    	int[] chars = new int[R];
    	for (int i = 0; i < R; i++) {
    		chars[i] = i;
    		pos[i] = i;
    	}
    	
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // System.out.print(c);
            int out = pos[c];
            BinaryStdOut.write((char) out);
            // System.out.println((char) out);
            for (int i = out; i > 0; i--) {
                pos[chars[i - 1]]++;
                chars[i] = chars[i - 1];
            }
            pos[c] = 0;
            chars[0] = c;
        }
        BinaryStdOut.close();
    }


    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> chars = new ArrayList<Character>(R);
    	for (int i = 0; i < R; i++) {
    		chars.add((char) (255 - i));
    	}
    
    	while(!BinaryStdIn.isEmpty()) {
	    	int i = BinaryStdIn.readChar();
	    	char c = chars.remove(255 - i);
	    	BinaryStdOut.write(c);
	    	chars.add(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        // encode();
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}