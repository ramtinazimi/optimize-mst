/**
 * Code is from http://algs4.cs.princeton.edu/24pq/MinPQ.java.html but slighty modified
 */

package Prim;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class BinaryHeap<Key> {
	private Key[] heap;
	private int N;
	private HashMap<Key, Integer> map;
	
	public BinaryHeap(int initCapacity){
		heap = (Key[]) new Object[initCapacity + 1];
		N = 0;
		map = new HashMap<Key, Integer>();
	}
	
	public BinaryHeap(){
		this(1);
	}
	
	public BinaryHeap(Key[] keys){
		N = keys.length;
		heap = (Key[]) new Object[keys.length + 1];
		map = new HashMap<Key, Integer>();
		for (int i = 0; i < N; i++) {
			heap[i+1] = keys[i];
			map.put(keys[i], i+1);
		}
		for (int k = N/2; k >= 1; k--) {
			sink(k);
		}
	}
	
	public BinaryHeap(Key[] keys, int left , int right){
		N = keys.length;
		heap = (Key[]) new Object[keys.length + 1];
		map = new HashMap<Key, Integer>();
		for (int i = left; i <= right; i++) {
			heap[i+1] = keys[i];
			map.put(keys[i], i+1);
		}
		for (int k = right/2; k >= 1; k--) {
			sink(k);
		}
	}
	
	public boolean isEmpty(){
		return N == 0;
	}
	
	public int size(){
		return N;
	}
	
	public Key min(){
		if(isEmpty()) throw new NoSuchElementException("Binary heap underflow");
		return heap[1];
	}
	
	private void resize(int capacity){
		Key[] temp = (Key[]) new Object[capacity];
		for(int i = 1; i <= N; i++){
			temp[i] = heap[i];
		}
		heap = temp;
	}
	
	public void insert(Key x){
		if (N == heap.length - 1)
			resize(2* heap.length);
		heap[++N] = x;
		map.put(x, N);
		swim(N);
	}
	

	public Key delMin() {
		if(isEmpty()) throw new NoSuchElementException("Binary heap underflow");
		swap(1, N);
		Key min = heap[N--];
		sink(1);
		map.remove(heap[N+1]);
		heap[N+1] = null;
		return min;
	}
	
	public void decreaseKey(Key oldKey, Key newKey){
		int idx = map.get(oldKey);
		heap[idx] = newKey;
		swim(idx);
	}
	
	public boolean contains(Key x){
		return map.containsKey(x);
	}
	
	//----------- Helper functions to restore the heap property -----------
	
	private void sink(int k) {
		while(2*k <= N){
			int j = 2*k;
			if(j < N && greater(j, j+1)) j++;
			if(!greater(k, j)) break;
			swap(k, j);
			k = j;
		}
	}

	private void swim(int k) {
		while(k > 1 && greater(k/2, k)){
			swap(k, k/2);
			k = k/2;
		}
		
	}
	
	//----------- Helper functions ----------------------------------------

	private boolean greater(int i, int j) {
		return ((Comparable<Key>)heap[i]).compareTo(heap[j]) > 0;
	}
	
	private void swap(int i, int j) {
		Key temp = heap[i];
		heap[i] = heap[j];
		map.put(heap[j], i);
		heap[j] = temp;
		map.put(temp, j);
		
	}
}
