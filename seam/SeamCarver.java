import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;
    private double[][] energy;
    private PictureSP vertSP;
    private PictureSP horSP;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkNull(picture);        
        this.picture = new Picture(picture);

        // initialize energy array;
        energy = new double[this.picture.width()][this.picture.height()];
        for (int x = 0; x < this.picture.width(); x++) {
            for (int y = 0; y < this.picture.height(); y++) {
                energy[x][y] = calcEnergy(x, y);
            }
        }
    }
 
    // current picture
    public Picture picture() {
        return picture;
    }
 
    // width of current picture
    public int width() {
        return picture.width();
    }
 
    // height of current picture
    public int height() {
        return picture.height();
    }
 
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validatePixel(x, y);
        return energy[x][y];
    }
 
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (horSP == null)
            horSP = new PictureSP(picture.height(), picture.width(), transpose(energy));
        return findSeam(horSP, picture.width());
    }
 
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (vertSP == null)
            vertSP = new PictureSP(picture.width(), picture.height(), energy);
        return findSeam(vertSP, picture.height());
    }
 
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, picture.width(), picture.height());
        picture = transpose(picture);
        energy = transpose(energy);
        removeSeam(seam);
        picture = transpose(picture);
        energy = transpose(energy);
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, picture.height(), picture.width());
        removeSeam(seam);
    }

    private double[][] transpose(double[][] x) {
        int row = x.length;
        int col = x[0].length;
        double[][] t = new double[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                t[j][i] = x[i][j];
            }
        }
        return t;
    }

    private Picture transpose(Picture pic) {
        int row = pic.width();
        int col = pic.height();
        Picture t = new Picture(col, row);
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                t.setRGB(y, x, pic.getRGB(x, y));
            }
        }
        return t;
    }

    private int[] findSeam(PictureSP sp, int length) {
        int[] indices = new int[length];
        int i = 0;
        for (int index : sp.seamPath()) {
            indices[i] = index;
            i++;
        }
        return indices;
    }

    private void removeSeam(int[] seam) {
        Picture newPicture = new Picture(picture.width() - 1, picture.height());
        double[][] newEnergy = new double[picture.width() - 1][picture.height()];
        for (int y = 0; y < picture.height(); y++) {
            int newX = 0;
            int removed = seam[y];
            for (int x = 0; x < picture.width(); x++) {
                if (x != removed) {
                    newPicture.setRGB(newX, y, picture.getRGB(x, y));
                    newEnergy[newX][y] = energy(x, y);
                    newX++;
                }
            }
        }
        picture = newPicture;

        for (int i = 0; i < seam.length; i++) {
            int removed = seam[i];
            if (removed > 0)
                newEnergy[removed - 1][i] = calcEnergy(removed - 1, i);
            if (removed < picture.width())
                newEnergy[removed][i] = calcEnergy(removed, i);
        }
        energy = newEnergy;
        vertSP = null;
        horSP = null;
    }

    private double calcEnergy(int x, int y) {
        validatePixel(x, y);

        // Corner Cases
        if (x + 1 >= picture.width() || x - 1 < 0
            || y + 1 >= picture.height() || y - 1 < 0)
            return 1000;

        int[] right = extractRGB(picture.getRGB(x + 1, y));
        int[] left = extractRGB(picture.getRGB(x - 1, y));
        int[] above = extractRGB(picture.getRGB(x, y - 1));
        int[] below = extractRGB(picture.getRGB(x, y + 1));

        double gradx = grad(right, left);
        double grady = grad(above, below);

        return Math.sqrt(gradx + grady);
    }
    
    private double grad(int[] x, int[] y) {
        double grad = Math.pow((x[0] - y[0]), 2) 
                        + Math.pow((x[1] - y[1]), 2)
                        + Math.pow((x[2] - y[2]), 2);
        return grad;
    }

    private int[] extractRGB(int rgb) {
        // 3 channels
        int[] color = new int[3];
        color[0] = (rgb >> 16) & 0xFF;
        color[1] = (rgb >>  8) & 0xFF;
        color[2] = (rgb >>  0) & 0xFF;
        return color;
    }

    private void validatePixel(int x, int y) {
        if (x < 0 || x >= picture.width()
            || y < 0 || y >= picture.height())
            throw new IllegalArgumentException("Pixel out of bounds.");
    }

    private void validateSeam(int[] seam, int dim1, int dim2) {
        checkNull(seam);
        if (seam.length != dim1)
            throw new IllegalArgumentException("Invalid seam length");

        for (int i = 0; i < dim1; i++) {
            if (seam[i] < 0 || seam[i] >= dim2)
                throw new IllegalArgumentException("Seam element out of bounds.");
            if (i != 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException("Seam invalid.");
        }
    }

    private void checkNull(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("Object is null.");
    }

    //  unit testing (optional)
    // public static void main(String[] args) {

    // } 
 }