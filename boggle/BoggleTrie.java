public class BoggleTrie {
    
    private Node root;      // root of trie

    public BoggleTrie() { }

    public void put(String key, int val) {
        // String str = new String(key);
        // for (int i = 0; i < str.length(); i++) {
        //     if (str.charAt(i) == 'Q') {
        //         StringBuilder newStr = new StringBuilder(str);
        //         if (i + 1 < str.length())
        //             str = newStr.deleteCharAt(i + 1).toString();
        //     }
        // }
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) {
            x.left = put(x.left, key, val, d);
        } else if (c > x.c) {
            x.right = put(x.right, key, val, d);
        } else if (d < key.length() - 1) {
            x.mid = put(x.mid, key, val, d + 1);
        } else {
            x.val = val;
            x.isString = true;
        }
        return x;            
    }
        
    public Node getRoot() {
        return root;
    }

    public Integer get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) 
            return null;
        char c = key.charAt(d);
        if (c < x.c) 
            return get(x.left, key, d);
        else if (c > x.c)
            return get(x.right, key, d);
        else if (d < key.length() - 1)
            return get(x.mid, key, d + 1);
        else if (x.isString)
            return x;
        return null;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }
}
