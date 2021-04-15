import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {

    private final Digraph wordNet;
    private final HashMap<String, LinkedList<Integer>> nounMap;
    private final HashMap<Integer, String> synsetMap;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkNull(synsets);
        checkNull(hypernyms);
        
        nounMap = new HashMap<>();
        synsetMap = new HashMap<>();
        // read synsets.
        In synsetIn = new In(synsets);
        while (synsetIn.hasNextLine()) {
            String[] fields = synsetIn.readLine().split(",");
            synsetMap.put((Integer) Integer.parseInt(fields[0]), fields[1]);
            
            String[] words = fields[1].split(" ");
            for (String word : words) {
                if (nounMap.containsKey(word)) {
                    LinkedList<Integer> ids = nounMap.get(word);
                    ids.add(Integer.parseInt(fields[0]));
                    nounMap.replace(word, ids);
                } else {
                    LinkedList<Integer> ids = new LinkedList<Integer>();
                    ids.add(Integer.parseInt(fields[0]));
                    nounMap.put(word, ids);
                }
            }
        
        }

        // add error handling to check if input is a rooted dag.
        // read hypernyms.
        wordNet = new Digraph(nounMap.size());
        In hypernymIn = new In(hypernyms);
        int hypCount = 0;
        while (hypernymIn.hasNextLine()) {
            String[] fields = hypernymIn.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                wordNet.addEdge(v, w);
            }
            hypCount++;
        }

        DirectedCycle dc = new DirectedCycle(wordNet);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("Graph has cycle.");
        }

        int roots = 0;
        for (int v = 0; v < hypCount; v++) {            
            if (wordNet.outdegree(v) == 0)
                roots++;
            if (roots >= 2)
                throw new IllegalArgumentException("Graph has multiple roots.");
        }
        sap = new SAP(wordNet);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounMap.containsKey(word);
        // return findNoun(word) >= 0;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNull(nounA);
        checkNull(nounB);
        LinkedList<Integer> v = nounMap.get(nounA);
        LinkedList<Integer> w = nounMap.get(nounB);
        return sap.length(v, w);


        // LinkedList<Integer> listA = nounMap.get(nounA);
        // LinkedList<Integer> listB = nounMap.get(nounB);
        // int least = -1;
        // for (int a : listA) {
        //     for (int b : listB) {
        //         int dist = Math.abs(a - b);
        //         if (least == -1 || dist < least)   
        //             least = dist;
        //     }
        // }
        // System.out.println(nounA + " " + nounB + " " + least);
        // return least;

        // int indexA = findNoun(nounA);
        // int indexB = findNoun(nounB);
        // if (indexA > indexB) {
        //     return least(indexA) - greatest(indexB);
        // } else {
        //     return least(indexB) - greatest(indexA);
        // }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        LinkedList<Integer> v = nounMap.get(nounA);
        LinkedList<Integer> w = nounMap.get(nounB);
        return synsetMap.get(sap.ancestor(v, w));
    }

    private void checkNull(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("Word is null.");
    }

    // private int findNoun(String word) {
    //     // Binary Search
    //     int lo = 0;
    //     int hi = synsetMap.size() - 1;
        
    //     while (lo <= hi) {
    //         int mid = (lo + hi) / 2;
    //         String[] synset = synsetMap.get(mid).split(" ");
    //         // System.out.println(word + " " + synsetMap.get(mid));
    //         int compare = word.compareTo(synset[0]);
    //         System.out.println(compare);
    //         if (compare < 0) { // word is smaller
    //             hi = mid - 1;
    //         } else if (compare > 0) {
    //             lo = mid + 1;
    //         } else {
    //             return mid;
    //         }
    //     }
    //     System.out.print(word);
    //     return -1;
    // }

    // private int least(int index) {
    //     String word = synsetMap.get(index).split(" ")[0];
    //     int n = 0;
    //     while(word.equals(synsetMap.get(index - n - 1).split(" ")[0])) {
    //         n++;
    //     }
    //     return index - n;
    // }

    // private int greatest(int index) {
    //     String word = synsetMap.get(index).split(" ")[0];
    //     int n = 0;
    //     // System.out.print(index);
    //     while(word.equals(synsetMap.get(index + n + 1).split(" ")[0])) {
    //         n++;
    //     }
    //     return index + n;
    // }

    // do unit testing of this class
    // public static void main(String[] args) {

    // }
}