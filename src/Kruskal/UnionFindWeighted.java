/**
 * This the UnionFindWeighted Data Structure, which has a better amortized complexity than the other UnionFind Datastructure
 * The data stucture is taken from algorithms by Sedgewick, Robert and Wayne, Kevin.
 */
package Kruskal;

public class UnionFindWeighted {
	private int[] id;
	private int[] rank;
	private int count;

	public UnionFindWeighted(int N) {
		if (N < 0)
			throw new IllegalArgumentException();
		count = N;
		id = new int[N];
		rank = new int[N];
		for (int i = 0; i < N; i++) {
			id[i] = i;
			rank[i] = 0;
		}
	}

	public int find(int p) {
		if (p < 0 || p >= id.length)
			throw new IndexOutOfBoundsException();
		while (p != id[p]) {
			id[p] = id[id[p]];
			p = id[p];
		}
		return p;
	}

	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	public void union(int p, int q) {
		int i = find(p);
		int j = find(q);

		if (i == j)
			return;

		if (rank[i] < rank[j])
			id[i] = j;
		else if (rank[i] > rank[j])
			id[j] = i;
		else {
			id[j] = i;
			rank[i]++;
		}
		count--;
	}
}