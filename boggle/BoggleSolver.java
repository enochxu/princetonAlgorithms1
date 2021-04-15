import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;

public class BoggleSolver
{

    private final BoggleTrie wordTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException("Dictionary is null.");
        
        wordTrie = new BoggleTrie();
        for (int i = 0; i < dictionary.length; i++) {
            wordTrie.put(dictionary[i], i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Words words = new Words(board);
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                words.addWords(i, j);
            }
        }
        return words.getWords();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (wordTrie.contains(word)) {
            return points(word);
        }
        return 0;
    }

    private class Words {

        int rows, cols;
        private final BoggleBoard board;
        boolean[][] used;
        private HashMap<Integer, String> words;
        // Queue<String> words;

        public Words(BoggleBoard board) {
            rows = board.rows();
            cols = board.cols();
            this.board = board;
            words = new HashMap<>();
        }

        public Iterable<String> getWords() {
            return words.values();
        }

        public void addWords(int row, int col) {
            used = new boolean[rows][cols];
            addWords(wordTrie.getRoot(), "", row, col);
        }

        // what if q?
        private void addWords(Node x, String seq, int row, int col) {
            if (!used[row][col]) {     
                char ltr = board.getLetter(row, col);
                if (ltr == 'Q') {
                    seq += ltr + "U";
                    x = getNext(x, ltr);
                    ltr = 'U';
                } else {
                    seq += ltr;
                }                    
                // System.out.println(seq);
                Node current = equalNode(x, ltr);
                if (current != null && seq.length() > 2) {
                    // System.out.println(current.val + " " + seq);
                    words.putIfAbsent(current.val, seq);
                }
                Node next = getNext(x, ltr);
                used[row][col] = true;
                if (next != null) {
                    // 8 possibilities moving forward
                    if (row > 0) {
                        addWords(next, seq, row - 1, col);
                        // diagonals
                        searchCols(next, seq, row - 1, col);
                    }                
                    if (row + 1 < rows) {
                        addWords(next, seq, row + 1, col);
                        // diagonals
                        searchCols(next, seq, row + 1, col);
                    }
                    searchCols(next, seq, row, col);
                }
                used[row][col] = false;
            }
            return;
        }

        private void searchCols(Node next, String seq, int row, int col) {
            if (col > 0)
                addWords(next, seq, row, col - 1);
            if (col + 1 < cols)
                addWords(next, seq, row, col + 1);
        }
        
        private Node equalNode(Node node, char c) {
            Node current = node;
            while (current != null) {
                if (c < current.c)
                    current = current.left;
                else if (c > current.c)
                    current = current.right;
                else if (current.isString)
                    return current;
                else
                    return null;
            }
            return null;
        }
  
        private Node getNext(Node current, char next) {
            if (current == null) return null;
            if (next < current.c)
                return getNext(current.left, next);
            else if (next > current.c)
                return getNext(current.right, next);
            else
                return current.mid;
        }
    }

    private int points(String word) {
        int len = word.length();
        if (len >= 8)
            return 11;
        else if (len == 7)
            return 5;
        else if (len == 6)
            return 3;
        else if (len == 5)
            return 2;
        else if (len == 3 || len == 4)
            return 1;
        else
            return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}