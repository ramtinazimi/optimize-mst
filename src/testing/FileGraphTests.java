/**
 * @author Alexandre Stüben
 */
package testing;

import java.io.IOException;

import mst.Graph;

public class FileGraphTests extends Tests{
	private final String graphPath;
	
	public FileGraphTests(String graphPath, String resultPath, boolean[] algos) {
		super(resultPath, algos);
		this.graphPath = graphPath;
	}
	/**
	 * Begins the whole testing process
	 */
	@Override
	public void start(){
		try {
			bw = openResultFile(resultPath);	
			Graph graph; 
			try {
				graph =  Graph.convertGraphFromFile(graphPath);
			} catch (IOException e) {
				System.out.println("Could not open source file.");
				bw.close();
				return;
			}
			resultTimes = testAlgos(graph, algos);
			bw.write("#");	//ignored by gnuplot
			writeColumnNames();
			for(int i=0; i< resultTimes.length; i++)
				bw.write(inSeconds(resultTimes[i]) + "\t\t" ); 
			bw.write("\n");
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not open result file.");
			return;
		}
	}
}
