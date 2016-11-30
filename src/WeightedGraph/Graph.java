package WeightedGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.sql.*;


/**
 * Undirected and Weighted Graph
 * Vertex contains: VertexName
 * Edges contains: Weight, Vertex
 * Created by cxz on 2016/10/2.
 */
public class Graph {
    protected HashMap<Vertex, List<Edge>> graph; // a graph using Vertex as key, and Edge as value
    protected HashMap<String, Vertex> finder;
    /**
     * initialize an empty graph
     * Complexity: constant
     */
  /*  public Graph() {
        graph = new HashMap<>();
    }
    */
    /**
     * readMap Read the map from a file, which must follow the listed pattern:
     * 1. Each line in the file represents a node and the edges linked to it.
     * 2. Words are separated by 'space'.
     * 3. First word of each line represents the name of the Vertex.
     * 4. Following words represents the edges.
     * eg. a b,1 c,2 d,3 e,4
     * It means 'a' is the current node and it is linked to four other nodes:'b' 'c' 'd' 'e'.
     * And weight of each Edges are:1 2 3 4.
     *
     * Complexity: number of Edges (E)
     */
    public Graph() {
        graph = new HashMap<>();
        finder = new HashMap<>();
        List<String> temp = new LinkedList<>(); //Storing one line in File f, which represents a Vertex and Edges linked to it
        
        String str;
        try {
        	// String sql = "select src_stop_id, dest_stop_id, duration from nystation;";  
    	   	// DBHelper db1 = new DBHelper(sql); 
    	    // ResultSet ret = db1.pst.executeQuery();
    	     
        	
        	
            try {
                Class.forName("com.mysql.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
                //Class.forName("org.gjt.mm.mysql.Driver");
               System.out.println("Success loading Mysql Driver!");
              }
              catch (Exception e) {
                System.out.print("Error loading Mysql Driver!");
                e.printStackTrace();
              }
              try {
                Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/newyorksubway","root","123456");
                     //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

                System.out.println("Success connect Mysql server!");
               
                Statement stmt = connect.createStatement();
                ResultSet ret = stmt.executeQuery("select * from nystation");
                                                                        //user 为你表的名称
                while (ret.next()) {
                	addEdge(ret.getString("src_stop_id"),ret.getString("dest_stop_id"),ret.getInt("duration"));
                  //System.out.println(ret.getString("dest_stop_id"));
                }
              }
              catch (Exception e) {
                System.out.print("get data error!");
                e.printStackTrace();
              }
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
    	     //while(ret.next()){
    	    	 
    	     //}

        } catch (Exception e) {
            e.printStackTrace();
        }
        temp.clear();/*
        for (Vertex v: graph.keySet()) {
        	System.out.println(v.getVertexName());
        	for (Edge e: graph.get(v))
        		System.out.println("\t" + e.startVertex.getVertexName() + " " + e.endVertex.getVertexName() + " " + e.getWeight());
        }//*/
    }

    protected boolean addEdge(String startVertexName, String endVertexName, int weight) {
    	Vertex startV, endV;
    	if (finder.containsKey(startVertexName))
    		startV = finder.get(startVertexName);
    	else {
    		startV = new Vertex(startVertexName);
    		finder.put(startVertexName, startV);
    	}
    	if (finder.containsKey(endVertexName))
    		endV = finder.get(endVertexName);
    	else {
    		endV = new Vertex(endVertexName);
    		finder.put(endVertexName, endV);
    	}
        Edge e = new Edge(startV, endV, weight);
        return addEdge(e);
    }

    /**
     * add an Edge to the Graph
     * Complexity: constant
     */
    protected boolean addEdge(Edge edge) {
        List<Edge> temp;
        Edge eReverse = edge.reverse();
        // if graph is empty, then add 2 Vertices, and add Edge e to each Vertex

        if (graph.containsKey(edge.getStartVertex())) {
            if (graph.containsKey(edge.getEndVertex())) {
                // if graph have both Start and End Vertices, then add Edge e to each Vertex
                temp = graph.get(edge.getStartVertex());
                if (!temp.contains(edge))
                    temp.add(edge);
                temp = graph.get(edge.getEndVertex());
                if (!temp.contains(eReverse))
                    temp.add(eReverse);
            } else {
                // if graph have only Start Vertex, then add End Vertex, and add Edge e to each Vertex
                temp = graph.get(edge.getStartVertex());
                if (!temp.contains(edge)) temp.add(edge);
                temp = new LinkedList<>();
                temp.add(eReverse);
                
            }
            graph.put(edge.getEndVertex(), temp);
        } else if (graph.containsKey(edge.getEndVertex())) {
            // if graph have only End Vertex, then add Start Vertex, and add Edge e to each Vertex
            temp = graph.get(edge.getEndVertex());
            if (!temp.contains(eReverse)) temp.add(eReverse);
            temp = new LinkedList<>();
            temp.add(edge);
            graph.put(edge.getStartVertex(), temp);
        } else {
            // if graph don`t have Start or End Vertices, then add 2 Vertices, and add Edge e to each Vertex
            temp = new LinkedList<>();
            temp.add(edge);
            graph.put(edge.getStartVertex(), temp);
            temp = new LinkedList<>();
            temp.add(eReverse);
            graph.put(edge.getEndVertex(), temp);
        }
        /*
        if(!finder.containsKey(edge.startVertex.getVertexName())){
        	finder.put(edge.startVertex.getVertexName(), edge.startVertex);
        }
        if(!finder.containsKey(edge.endVertex.getVertexName())){
        	finder.put(edge.endVertex.getVertexName(), edge.endVertex);
        }*/
        return true;
    }

	public void dijkstra(String start, String dest) {
		Vertex startV = finder.get(start);
		Vertex destV = finder.get(dest);
		
		HashSet<Vertex> Q = new HashSet<>();
		for(Vertex v: graph.keySet()){
			v.value = Integer.MAX_VALUE;
			v.setPreVertex(null);
			Q.add(v);
		}
		
		startV.value = 0;

		while(!Q.isEmpty()){
			Vertex u = minDist(Q);
			Q.remove(u);
			if (u == null)
				break;
			//System.out.println(u.getVertexName());
			for(Edge e: graph.get(u)){
				if(e.startVertex.equals(u)){
					Vertex v = e.endVertex;
					int alt = u.value + e.weight;
					
					if(alt < v.value){
						v.value = alt;
						v.setPreVertex(u);
					}
				}
				
			}
		}
		
		while(destV!=null){
			System.out.println(destV.getVertexName() + " " + destV.value);
			destV = destV.getPreVertex();
		}
		
	}

	private Vertex minDist(HashSet<Vertex> q) {
		int min = Integer.MAX_VALUE;
		Vertex u = null;
		for(Vertex v: q){
			if(v.value < min){
				min = v.value;
				u = v;
			}
		
		}
		return u;
	}


    
}
