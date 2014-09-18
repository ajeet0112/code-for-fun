import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.DataInputStream;
import java.io.FileInputStream;



public class Finals {
	static List<Pair<Integer,Integer>> arthur= new ArrayList<Pair<Integer,Integer>>();
	static List<Pair<Integer,Integer>> trillion= new ArrayList<Pair<Integer,Integer>>();
	static List<Pair<Integer,Integer>> arthurPath= new ArrayList<Pair<Integer,Integer>>();
	static List<Pair<Integer,Integer>> trillionPath= new ArrayList<Pair<Integer,Integer>>();
	static NumberFormat formatter = new DecimalFormat("#0.00000");   
	public static class Pair<X,Y> {

		private X x;
		private Y y;
		public Pair(X x, Y y){
			this.x = x;
			this.y = y;
		}
		public X getX(){ return x; }
		public Y getY(){ return y; }
		public void setX(X x){ this.x = x; }
		public void setY(Y y){ this.y = y; }

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (!(o instanceof Pair)) return false;
			Pair pairo = (Pair) o;
			return this.x.equals(pairo.getX()) &&
					this.y.equals(pairo.getY());
		}

	}


	public static void main(String[] args) {

		BufferedReader br = null;
		PrintWriter pWriter = null;
		boolean badInput = false;

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			pWriter = new PrintWriter(new OutputStreamWriter(System.out));

			String line=null;
			if( (line=br.readLine()) != null) {
				int numInputs = Integer.parseInt(line);
				if(numInputs < 2 || numInputs > 50000){
					badInput = true;
				} else{
					for(int i=0;i<numInputs;i++){
						line = br.readLine().trim();
						String[] co = line.split(" ");
						if(Integer.parseInt(co[0]) < 0 || Integer.parseInt(co[0])> 30000 || Integer.parseInt(co[1]) < 0 || Integer.parseInt(co[1])> 30000){
							badInput = true;
							break;
						}
						arthur.add(new Pair<Integer, Integer>(Integer.parseInt(co[0]), Integer.parseInt(co[1])));
					}
				}
			} 

			if(badInput == false){
				if( (line=br.readLine()) != null) {
					int numOutputs = Integer.parseInt(line);
					if(numOutputs < 2 || numOutputs > 50000){
						badInput = true;
					}else{
						for(int i=0;i<numOutputs;i++){
							line = br.readLine().trim();
							String[] co = line.split(" ");
							if(Integer.parseInt(co[0]) < 0 || Integer.parseInt(co[0])> 30000 || Integer.parseInt(co[1]) < 0 || Integer.parseInt(co[1])> 30000){
								badInput = true;
								break;
							}
							trillion.add(new Pair<Integer, Integer>(Integer.parseInt(co[0]), Integer.parseInt(co[1])));
						}
					}
				}
			}

			String ret = null;

			if(badInput == false){
				calculateFullPathCoOrdinates();
				ret = calculateShortestPath();
			}
			else{
				ret = "Impossible";
			}

			pWriter.println(ret);

			br.close();
			pWriter.flush();
			pWriter.close();

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	private static String calculateShortestPath() {
		String ret = "Impossible";
		ArrayList<Double> dist = new ArrayList<Double>();
		ArrayList<Double> deltaDist = new ArrayList<Double>();
		for(int i=0;i<arthurPath.size();i++){
			Pair<Integer, Integer> arthurPos = arthurPath.get(i);
			for(int j = i; j < trillionPath.size();j++){
				Pair<Integer, Integer> trillionPos = trillionPath.get(j);
				double distance = calculateDistance(arthurPos,trillionPos);
				if((double)(j-i) == distance){
					dist.add(distance);					
				} else{
					double trilTavel = (double)(j-i);
					if((distance - trilTavel) < 1){
						boolean IncreaseX = false;
						if((j+1) < trillionPath.size()){
							Pair<Integer, Integer> trillionNextPos = trillionPath.get(j+1);
							if(trillionNextPos != null){
								if(trillionNextPos.x > trillionPos.x)
									IncreaseX = true;							
							}
							if(IncreaseX){
								for(int a=1;a<10;a++){
									double deltaDistance = calculateDeltaDistance(arthurPos.x,arthurPos.y,(double)trillionPos.x+(0.1 *a),trillionPos.y);
									if(Math.abs((double)((j-i) + (0.1 *a)) - deltaDistance) < 0.1){
										deltaDist.add(deltaDistance);					
									}
								}
							}
							else{
								for(int a=1;a<10;a++){
									double deltaDistance = calculateDeltaDistance(arthurPos.x,arthurPos.y,trillionPos.x,(double)trillionPos.y+(0.1 *a));
									if(Math.abs((double)((j-i) + (0.1 *a)) - deltaDistance) < 0.1){
										deltaDist.add(deltaDistance);					
									}
								}
							}
						}
					}
				}
			}
		}
		if(dist.isEmpty()){
			if(deltaDist.isEmpty() == false){
				Collections.sort(deltaDist);
				ret = String.valueOf(formatter.format(deltaDist.get(0)));				
			}
		}
		else{
			Collections.sort(dist);
			ret = String.valueOf(formatter.format(dist.get(0)));
		}
		return ret;
	}


	private static double calculateDeltaDistance(double x, double y,
			double x2, double y2) {
		return Math.sqrt(Math.pow(x - x2,2) + Math.pow(y - y2,2));
	}



	private static double calculateDistance(Pair<Integer, Integer> arthurPos,
			Pair<Integer, Integer> trillionPos) {
		return Math.sqrt(Math.pow(arthurPos.x - trillionPos.x,2) + Math.pow(arthurPos.y - trillionPos.y,2));
	}


	private static void calculateFullPathCoOrdinates() {
		//Calculate Arthur's Path
		int curr = 0, next = 1, numInputs = 0;
		numInputs = arthur.size();
		Pair<Integer, Integer> currentPos;
		Pair<Integer, Integer> nextPos;
		arthurPath.add(arthur.get(0));

		while(next < numInputs){
			currentPos = arthur.get(curr);
			nextPos = arthur.get(next);
			if(nextPos.x > currentPos.x){
				int x = currentPos.x + 1;
				int y = currentPos.y;
				while(x <= nextPos.x){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					arthurPath.add(nextPoint);
					x++;
				}
			} else 	if(nextPos.x < currentPos.x){
				int x = currentPos.x - 1;
				int y = currentPos.y;
				while(x >= nextPos.x){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					arthurPath.add(nextPoint);
					x--;
				}
			} else if(nextPos.y > currentPos.y){
				int x = currentPos.x;
				int y = currentPos.y + 1;
				while(y <= nextPos.y){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					arthurPath.add(nextPoint);
					y++;
				}
			} else if(nextPos.y < currentPos.y){
				int x = currentPos.x;
				int y = currentPos.y - 1;
				while(y >= nextPos.y){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					arthurPath.add(nextPoint);
					y--;
				}
			}
			curr = next;
			next++;			

		}

		curr = 0; next = 1; numInputs = 0;
		numInputs = trillion.size();
		trillionPath.add(trillion.get(0));

		while(next < numInputs){
			currentPos = trillion.get(curr);
			nextPos = trillion.get(next);
			if(nextPos.x > currentPos.x){
				int x = currentPos.x + 1;
				int y = currentPos.y;
				while(x <= nextPos.x){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					trillionPath.add(nextPoint);
					x++;
				}
			} else if(nextPos.x < currentPos.x){
				int x = currentPos.x - 1;
				int y = currentPos.y;
				while(x >= nextPos.x){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					trillionPath.add(nextPoint);
					x--;
				}
			} else if(nextPos.y > currentPos.y){
				int x = currentPos.x;
				int y = currentPos.y + 1;
				while(y <= nextPos.y){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					trillionPath.add(nextPoint);
					y++;
				}
			} else if(nextPos.y < currentPos.y){
				int x = currentPos.x;
				int y = currentPos.y - 1;
				while(y >= nextPos.y){
					Pair<Integer, Integer> nextPoint = new Pair<Integer, Integer>(x, y);
					trillionPath.add(nextPoint);
					y--;
				}
			}
			curr = next;
			next++;			

		}

	}
}
