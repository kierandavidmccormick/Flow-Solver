package sample;

/**
 * Created by kieranmccormick on 12/31/17.
 */
public class Filter {
	public int[][] compareVals;
	public static final int ISSQUAREVAL = 0;
	public static final int ISDENTBLOCKVAL = 1;
	public static final int ISDENTEMPTYVAL = 2;
	public static final int ISIRRELEVANT = 3;
	public static final int[][] BLOCKFILTER = {{0, 0}, {0, 0}};
	public static final int[][] DENT1FILTERLEFT  = {{3, 3, 3}, {1, 2, 1}, {3, 1, 3}};
	public static final int[][] DENT1FILTERUP    = {{3, 1, 3}, {3, 2, 1}, {3, 1, 3}};
	public static final int[][] DENT1FILTERRIGHT = {{3, 1, 3}, {1, 2, 1}, {3, 3, 3}};
	public static final int[][] DENT1FILTERDOWN  = {{3, 1, 3}, {1, 2, 3}, {3, 1, 3}};
	public static final int[][] DENT2FILTERLEFT  = {{3, 3, 3, 3}, {1, 2, 2, 1}, {3, 1, 1, 3}};
	public static final int[][] DENT2FILTERUP    = {{3, 1, 3}, {3, 2, 1}, {3, 2, 1}, {3, 1, 3}};
	public static final int[][] DENT2FILTERRIGHT = {{3, 1, 1, 3}, {1, 2, 2, 1}, {3, 3, 3, 3}};
	public static final int[][] DENT2FILTERDOWN  = {{3, 1, 3}, {1, 2, 3}, {1, 2, 3}, {3, 1, 3}};
	public static final int[][][] filters = {BLOCKFILTER, DENT1FILTERLEFT, DENT1FILTERUP, DENT1FILTERRIGHT, DENT1FILTERDOWN, DENT2FILTERLEFT, DENT2FILTERUP, DENT2FILTERRIGHT, DENT2FILTERDOWN};
	
	public Filter(int[][] compareVals){
		this.compareVals = compareVals;
	}
}