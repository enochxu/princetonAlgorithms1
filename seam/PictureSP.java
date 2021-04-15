import edu.princeton.cs.algs4.Stack;

public class PictureSP {
        
    private final Stack<Integer> reversePost;
    private boolean[][] marked;
    private int[][] moveTo;
    private double[][] energyTo;
    private final double[][] energy;
    private final int width, height;
    private int leastIndex;
    private double leastEnergy;

    public PictureSP(int width, int height, double[][] energy) {
        this.width = width;
        this.height = height;
        
        reversePost = new Stack<Integer>();
        this.energy = new double[width][height];
        moveTo = new int[width][height];
        energyTo = new double[width][height];
        marked = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == 0) {
                    energyTo[x][y] = energy[x][y];
                    dfs(x, y);
                } else {
                    energyTo[x][y] = Double.POSITIVE_INFINITY;
                }
                this.energy[x][y] = energy[x][y];
            }
        }
        leastEnergy = Double.POSITIVE_INFINITY;
        
        while (!reversePost.isEmpty()) {
            int ind = reversePost.pop();
            relax(ind);
        }
    }

    public Iterable<Integer> seamPath() {
        Stack<Integer> path = new Stack<Integer>();
        int x1 = calcX(leastIndex);
        for (int x2 = height - 1; x2 >= 0; x2--) {
            path.push(x1);
            x1 = x1 - moveTo[x1][x2];
        }
        return path;
    }

    private void dfs(int x, int y) {
        marked[x][y] = true;
        if (y + 1 < height) {
            // center
            if (!marked[x][y + 1])
                dfs(x, y + 1);
            // left
            if (x > 0 && !marked[x - 1][y + 1]) {
                dfs(x - 1, y + 1);
            }
            // right
            if (x + 1 < width && !marked[x + 1][y + 1])
                dfs(x + 1, y + 1);                   
        }
        reversePost.push(convert1D(x, y));
    }

    private void relax(int index) {
        int x1  = calcX(index);
        int x2  = calcY(index);

        if (x2 + 1 < height) {
            // center
            updateEnergy(x1, x2 + 1, x1, x2);
            // left
            if (x1 > 0)
                updateEnergy(x1 - 1, x2 + 1, x1, x2);
            // right
            if (x1 + 1 < width)
                updateEnergy(x1 + 1, x2 + 1, x1, x2);                    
        }

    }
    
    private void updateEnergy(int newX, int newY, int oldX, int oldY) {
        if (energyTo[newX][newY] > energyTo[oldX][oldY] + energy[newX][newY]) {
            double vertexEnergy = energyTo[oldX][oldY] + energy[newX][newY];
            energyTo[newX][newY] = vertexEnergy;
            // 1, 0, or -1 depending on movement
            moveTo[newX][newY] = newX - oldX;

            if (newY == height - 1 && vertexEnergy < leastEnergy) {
                leastEnergy = vertexEnergy;
                leastIndex = convert1D(newX, newY);
            }
        }
    }

    private int convert1D(int x, int y) {
        return y * width + x;
    }

    private int calcX(int index) {
        return index % width;
    }

    private int calcY(int index) {
        return index / width; 
    }
}
