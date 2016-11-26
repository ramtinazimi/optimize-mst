package Kruskal;

/**
 * This class implements the Union-Find-Datastructure needed for Kruskal.
 * It is taken from Röglin's lecture notes in "Algorithmic and Computability Complexity 1".
 * 
 *
 * @author Ramtin Azimi
 *
 * Created 2015.
 *
 */
import java.util.ArrayList;
import java.util.List;

public class UnionFind {

    // For each i A[i] is the representative of i
    int[] A;
    // An array with each element i of the array is a list of vertices with i as
    // representative
    List<Integer>[] L;
    // Size of each
    int[] size;

    public UnionFind(int N) {
        A = new int[N];
        size = new int[N];
        L = (ArrayList<Integer>[]) new ArrayList<?>[N];
        
        MakeSet();
    }


    public void MakeSet(){
        
        for(int i=0; i< A.length; i++){
            A[i] = i;
            size[i] = 0;
            L[i] = new ArrayList<Integer>();
            L[i].add(i);
        }
    }
    public void union(int x, int y) {
        // Get the representative of x, we call it i.
        int i = find(x);
        // Get the representative of y, let's call it j.
        int j = find(y);

        if (i == j) {
            return;
        }
        // Test, which of the representatives, i and j, have more element in
        // their representative class. Add all the ones from the smaller set to
        // the larger ones.

        // In the following index i will be set to the smaller of those sets.*
        if (size[i] > size[j]) {
            // Swap i and j
            int temp = j;
            j = i;
            i = temp;
        }
        // By definition (see *), we set i to the index of the smaller set. Now,
        // we copy all the elements from the set of i to the set of j.

        // First, for all element in class of i we change their representative
        // to j.
        
        for (int z : L[i]) {
            A[z] = j;
        }
        // Second, we append the elements of i to the list of j. Thus, they
        // belong to one single class with representative j.
        L[j].addAll(L[i]);
        // Adjust the size of the class of j to the number of element of class i which were added.
        size[j] = size[j]+size[i];
        //We still need to delet all the elements from class i and set the size of the class i to 0.
        L[i] = null;
        size[i] = 0;
        
    }

    public int find(int x) {
        return A[x];
    }
    
    public String toString(){
        
        String list = "";
        
        for(int i=0; i< L.length; i++){
            
            list = list + " "+i+": {";
            if(L[i] == null){
                list = list + "empty";
            }else{
                for(int z: L[i]){
                    list = list +z+", ";
                }
            }
            
            list = list + "} \n";
        }
        return list;
        
        
    }
}

