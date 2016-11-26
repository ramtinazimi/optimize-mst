package Sorting;

import mst.Edge;

/**
 * @author Vladimir Yaroslavskiy
 * @version 2009.09.17 m765.817
 */
public class DualPivotQuicksort {
	public static void sort(Edge[] a) {
		sort(a, 0, a.length);
	}

	public static void sort(Edge[] a, int fromIndex, int toIndex) {
		rangeCheck(a.length, fromIndex, toIndex);
		dualPivotQuicksort(a, fromIndex, toIndex - 1);
	}

	private static void rangeCheck(int length, int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex
					+ ")> toIndex(" + toIndex + ")");
		}
		if (fromIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(fromIndex);
		}
		if (toIndex > length) {
			throw new ArrayIndexOutOfBoundsException(toIndex);
		}
	}

	private static void dualPivotQuicksort(Edge[] a, int left, int right) {
		int len = right - left;
		Edge x;
		if (len < TINY_SIZE) { // insertion sort on tiny array
			for (int i = left + 1; i <= right; i++) {
				for (int j = i; j > left && a[j].weight < a[j - 1].weight; j--) {
					x = a[j - 1];
					a[j - 1] = a[j];
					a[j] = x;
				}
			}
			return;
		}
		// median indexes
		int sixth = len / 6;
		int m1 = left + sixth;
		int m2 = m1 + sixth;
		int m3 = m2 + sixth;
		int m4 = m3 + sixth;
		int m5 = m4 + sixth;
		// 5-element sorting network
		if (a[m1].weight > a[m2].weight) {
			x = a[m1];
			a[m1] = a[m2];
			a[m2] = x;
		}
		if (a[m4].weight > a[m5].weight) {
			x = a[m4];
			a[m4] = a[m5];
			a[m5] = x;
		}
		if (a[m1].weight > a[m3].weight) {
			x = a[m1];
			a[m1] = a[m3];
			a[m3] = x;
		}
		if (a[m2].weight > a[m3].weight) {
			x = a[m2];
			a[m2] = a[m3];
			a[m3] = x;
		}
		if (a[m1].weight > a[m4].weight) {
			x = a[m1];
			a[m1] = a[m4];
			a[m4] = x;
		}
		if (a[m3].weight > a[m4].weight) {
			x = a[m3];
			a[m3] = a[m4];
			a[m4] = x;
		}
		if (a[m2].weight > a[m5].weight) {
			x = a[m2];
			a[m2] = a[m5];
			a[m5] = x;
		}
		if (a[m2].weight > a[m3].weight) {
			x = a[m2];
			a[m2] = a[m3];
			a[m3] = x;
		}

		if (a[m4].weight > a[m5].weight) {
			x = a[m4];
			a[m4] = a[m5];
			a[m5] = x;
		}
		// pivots: [ < pivot1 | pivot1 <= && <= pivot2 | > pivot2 ]
		Edge pivot1 = a[m2];
		Edge pivot2 = a[m4];
		boolean diffPivots = pivot1.weight != pivot2.weight;
		a[m2] = a[left];
		a[m4] = a[right];
		// center part pointers
		int less = left + 1;
		int great = right - 1;
		// sorting
		if (diffPivots) {
			for (int k = less; k <= great; k++) {
				x = a[k];
				if (x.weight < pivot1.weight) {
					a[k] = a[less];
					a[less++] = x;
				} else if (x.weight > pivot2.weight) {
					while (a[great].weight > pivot2.weight && k < great) {
						great--;
					}
					a[k] = a[great];
					a[great--] = x;
					x = a[k];
					if (x.weight < pivot1.weight) {
						a[k] = a[less];
						a[less++] = x;
					}
				}
			}
		} else {
			for (int k = less; k <= great; k++) {
				x = a[k];
				if (x.weight == pivot1.weight) {
					continue;
				}
				if (x.weight < pivot1.weight) {
					a[k] = a[less];
					a[less++] = x;
				} else {
					while (a[great].weight > pivot2.weight && k < great) {
						great--;
					}
					a[k] = a[great];
					a[great--] = x;
					x = a[k];
					if (x.weight < pivot1.weight) {
						a[k] = a[less];
						a[less++] = x;
					}

				}
			}
		}
		// swap
		a[left] = a[less - 1];
		a[less - 1] = pivot1;
		a[right] = a[great + 1];
		a[great + 1] = pivot2;
		// left and right parts
		dualPivotQuicksort(a, left, less - 2);
		dualPivotQuicksort(a, great + 2, right);
		// equal elements
		if (great - less > len - DIST_SIZE && diffPivots) {
			for (int k = less; k <= great; k++) {
				x = a[k];
				if (x.weight == pivot1.weight) {
					a[k] = a[less];
					a[less++] = x;
				} else if (x.weight == pivot2.weight) {
					a[k] = a[great];
					a[great--] = x;
					x = a[k];

					if (x.weight == pivot1.weight) {
						a[k] = a[less];
						a[less++] = x;
					}
				}
			}
		}
		// center part
		if (diffPivots) {
			dualPivotQuicksort(a, less, great);
		}
	}

	private static final int DIST_SIZE = 13;
	private static final int TINY_SIZE = 17;
}
