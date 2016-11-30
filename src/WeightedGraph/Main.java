package WeightedGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;

public class Main {

	public static void main(String args[]){
		
		//Scanner scan = new Scanner(System.in);
		
		String vertexName = "103";
		
		Map <Vertex, Integer> m1 = new HashMap<>();
		
		Graph roads = new Graph();
		roads.dijkstra("107","619");
		
		try {
			//m1=roads.Dijkstra(vertexName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\t"+m1);
	}
}
