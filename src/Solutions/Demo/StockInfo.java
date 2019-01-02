/*
* PURPOSE: This class is being used by multiplay class to generate list of stocks
* This defines information about stock companies
*/

package Solutions.Demo;

public class StockInfo {
		public String name;
		public String mission;
		public String founder;
		
		// Constructors
		public StockInfo() {}
	
		public StockInfo(String name, String mission, String founder) {
			this.name = name;
			this.mission = mission;
			this.founder = founder;
		}
	
}
