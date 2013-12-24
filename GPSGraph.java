import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GPSGraph
{
   static HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>(); //takes the input of an ID and outputs the vertex
   static HashMap<Vertex, ArrayList<Edge>> adjList = new HashMap<Vertex, ArrayList<Edge>>();//takes the input of a ____ and outputs an ArrayList of its outgoing edges
   static Scanner s = new Scanner (System.in);
   static Vertex source; //source vertex
            
   //Populates a graph using an input file 
   public static void read() throws FileNotFoundException
   {
      System.out.println("Enter filename of cities to read.");
      read(s.next());
   }
   
   //reads the file using the filename as an argument
   public static void read(String filename) throws FileNotFoundException
   {
      Scanner file = null;
      try
      {
         file = new Scanner(new File(filename));
      }
      catch (FileNotFoundException e)
      {
         System.out.println("Invalid file name!");
         return;
      }
   
   
   //reading from the file
      try{
         int N = file.nextInt();
      
         for (int k = 0; k < N; k++)
         {
            file.nextLine();
         
            String citystate = file.nextLine();
            double lon = file.nextDouble();
            double lat = file.nextDouble();
            Vertex v = new Vertex(k, citystate, lon, lat);
            vertices.put(k,v);
            adjList.put(v, new ArrayList<Edge>());
         
         }
      
      //adding random connections between the source and random nodes
         for (int i = 0; i < N; i++)
         {
         
            int numCon = 2 + (int)(Math.random() * 7);
            int[] neighborIDs = new int[numCon]; 
            for (int w = 0; w < numCon; w++)
            {
               int id = (int)(Math.random() * N);
               while (id == i || Arrays.asList(neighborIDs).contains(id))
               {
                  id = (int)(Math.random() * N);
               }
               neighborIDs[w] = id;
            
            
               ArrayList<Edge> edges = adjList.get(vertices.get(i));
               edges.add(new Edge(vertices.get(i), vertices.get(id), 100+(int)(Math.random()* 1901)));
               adjList.put(vertices.get(i), edges); 
               vertices.get(i).incOutcount();
               vertices.get(id).incIncount();
            
            }
         
         }
      }
      catch (NumberFormatException e)
      {
         System.out.println("An error occured while trying to read the file.");
         //clears both the map of vertices and the connection list
         vertices = new HashMap<Integer, Vertex>(); 
         adjList = new HashMap<Vertex, ArrayList<Edge>>();
      }
       
   }
   
   
  //displays some basic info about a state if it is in the database
   public static void searchByState()
   {
      System.out.println("Enter a state to search by.");
      String state = s.next();
      searchByState(state);
      
   }
   
   //displays some basic info about a state if it is in the database (with an argument)
   public static void searchByState(String state)
   {
      boolean b = false;
      for (Vertex w: adjList.keySet())
      {
         if (w.getState().equals(state))
         {
            System.out.println("city: " + w.getCity() + " incount: " + w.getIncount() + " outcount: " +w.getOutcount());
            b = true;
         }
      }
      if (!b)
      {
         System.out.println("State not found.");
      }
      
   }

   
  //displays some basic info about a city if it is in the database

   public static void searchByCity()
   {
      System.out.println("Enter a city to search for.");
      String city = s.next();
      searchByCity(city);
   }
   
   //displays some basic info about a city if it is in the database (with an argument)
   public static void searchByCity(String city)
   {
      for (Vertex w: adjList.keySet())
      {
         if (w.getCity().equals(city))
         {
            System.out.println("City name: " + city);
            System.out.println("State: " + w.getState());
            System.out.println("ID: " + w.getID());
            System.out.println("Latitude: " + w.getLat());
            System.out.println("Longitude: " + w.getLon());
            System.out.println("Incount: " + w.getIncount());
            System.out.println("Outcount: " + w.getOutcount());
            return;
         }
      }
      System.out.println("City not found.");
   }

  //Sets the source node
   public static void setSource()
   {
      System.out.println("Enter ID of city to set it as the current city.");
      try
      {
         setSource(s.nextInt());
      }
      catch (NumberFormatException | InputMismatchException e)
      {
         System.out.println("Not an integer.");
         s.next();
         return;
      }
   }
   
  //Sets the source node (with argument)
   public static void setSource(int id) 
   {
      if (vertices.get(id)==null)
      {
         System.out.println("ID out of bounds.");
         return;
      }
      else
      {
         source = vertices.get(id);
      }  
          
   }
   
   //prints info about the source node
   public static void getSource()
   {
      System.out.println("city: " + source.getCity() + " state: " + source.getState() + " incount: " + source.getIncount() + " outcount: " +source.getOutcount());
   }
   
   //get the clostest cities by distance from the source node
   public static void getClosestByDistance()
   {
      System.out.println("Enter number of cities to obtain.");
      try
      {
         getClosestByDistance(s.nextInt());
      }
      catch (NumberFormatException e)
      {
         System.out.println("Not an integer.");
         s.next();
         return;
      }
      
   }
   //returns the num closest cities by distance from the source node (with an argument)
   public static void getClosestByDistance(int num)
   {
         
      PriorityQueue<Vertex> q = new PriorityQueue<Vertex>(10, 
            Vertex.MIN_DISTANCE);      
   
      //resetting distance values before we compute the haversine distances
      for (Vertex v: adjList.keySet())
      {
         v.setMinDistance(distanceBetween(source, v));
         q.add(v);
           
      }
        
      q.remove();
   
     //at most the algorithm will print its distance to all of the nodes in the graph
     //this prevents an exception later in the for-loop
      if (num > q.size())
      {
         num = q.size();
      }
      
      //printing the N closest nodes from the source node
      for (int w = 0; w < num; w++)
      {
         Vertex x = q.remove();
         System.out.println("city: " + x.getCity() + " state: " + x.getState() + " lat: " + x.getLat() + " lon: "+ x.getLon() + " Distance: " + x.getMinDistance());
      }
   }

       
  //prints the num closest cities by cost from the source node
   public static void getClosestByCost()
   {
      System.out.println("Enter number of cities to obtain.");
      try
      {
         getClosestByCost(s.nextInt());
      }
      catch (NumberFormatException e)
      {
         System.out.println("Invalid ID.");
         s.next();
         return;
      }
      
   }
   
    //gets the num closes cities by cost from the source node (with an argument)	 
   public static void getClosestByCost(int num)
   {
      
      PriorityQueue<Vertex> q = new PriorityQueue<Vertex>(10,
            Vertex.MIN_COST);
   
      //resetting distance values before we compute the minimum paths
      for (Vertex v: adjList.keySet())
      {
         v.setMinCost(Integer.MAX_VALUE);
         v.setVisited(false);
         
      }
      source.setMinCost(0);
      source.setVisited(true);
      
      //adding our beginning node to the queue
      q.add(source);
   
   //enqueueing and dequeueing the neighbors via a BFS algorithm, and updating the minDistance of each node
      while(!q.isEmpty())
      {
         Vertex current = q.remove();
         current.setVisited(true);
         for (Edge e: adjList.get(current))
         {
            double tentativeCost = current.getMinCost() + e.getWeight();
            if (!e.getTo().isVisited()){
               if (e.getTo().getMinCost() > tentativeCost){
                  e.getTo().setMinCost(tentativeCost);
                  e.getTo().setPrev(current);
                  q.add(e.getTo());
               }
            }
         
         }
      }
   
   //Sorting the vertices based on their distance from the source node
      ArrayList<Vertex> calcedVerts = new ArrayList<Vertex>();
      for (Vertex v: adjList.keySet())
      {
         if (v.getID()!= source.getID())
         
            calcedVerts.add(v);
      }
      Collections.sort(calcedVerts, Vertex.MIN_COST);   
     
     
      //at most the algorithm will print its distance to all of the nodes in the graph
     //this prevents an exception later in the for-loop
      if (num > calcedVerts.size())
      {
         num = calcedVerts.size();
      }
   
     //printing the N closest nodes from the source node
      for (int w = 0; w < num; w++)
      {
         System.out.println("city: " + calcedVerts.get(w).getCity() + " state: " + calcedVerts.get(w).getState() + " lat: " + calcedVerts.get(w).getLat() + " lon: "+ calcedVerts.get(w).getLon() + " Cost: " + calcedVerts.get(w).getMinCost());
      }
   
   }

   
   //prints the shortest path from the source node to another node 
   public static void getShortestPath()
   {
         
      System.out.println("Enter ID of destination city.");
      try{
         getShortestPath(s.nextInt());
      }
      catch (NumberFormatException | InputMismatchException e)
      {
         System.out.println("Not an integer.");
         s.next();
         return;
      }
   }
   
    //gets the shortest path from the source node to the given node (with an argument)	 
   public static void getShortestPath(int idTo)
   {
      if (vertices.get(idTo) == null)
      {
         System.out.println("That ID doesn't correspond to a city in this graph.");
         return;   
      }
      else
      {    
      //First we perform the same algorithm for finding the N closest cities by distance - that code is copied
      //without the printing stuff at the end.
         PriorityQueue<Vertex> q = new PriorityQueue<Vertex>(10, Vertex.MIN_COST);
      
      //resetting distance values before we compute the minimum paths
         for (Vertex v: adjList.keySet())
         {
            v.setMinDistance(Integer.MAX_VALUE);
            v.setVisited(false);
         
         }
         source.setMinDistance(0);
         source.setVisited(true);
      
      //adding our beginning node to the queue
         q.add(source);
      
      //enqueueing and dequeueing the neighbors via a BFS algorithm, and updating the minDistance of each node
         while(!q.isEmpty())
         {
            Vertex current = q.remove();
            current.setVisited(true);
            for (Edge e: adjList.get(current))
            {
               double tentativeDistance = current.getMinDistance() + distanceBetween(current, e.getTo());
               if (!e.getTo().isVisited()){
                  if (e.getTo().getMinDistance() > tentativeDistance){
                     e.getTo().setMinDistance(tentativeDistance);
                     e.getTo().setPrev(current);
                     q.add(e.getTo());
                  }
               }
            
            }
         }
      
      //this arraylist will contain the path from the source node to the desired node
         ArrayList<String> path = new ArrayList<String>();   
      
         printPath(vertices.get(idTo), path);
      
         System.out.println(path.toString());
      }
   }

//Calculates the GPS distance between two nodes using the Haversine formula
   public static double distanceBetween(Vertex v1, Vertex v2)
   {
      double dlon = Math.toRadians(v2.getLon() - v1.getLon()); 
      double dlat = Math.toRadians(v2.getLat() - v1.getLat());
      double a = (Math.sin(dlat/2))* (Math.sin(dlat/2)) + Math.cos(Math.toRadians(v1.getLat())) * Math.cos(Math.toRadians(v2.getLat())) * (Math.sin(dlon/2))*(Math.sin(dlon/2)) ;
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a) ) ;
      double d = 3958.75 * c;
      return d;
   }
   
  //builds the path from the source node to the given node
   public static void printPath(Vertex node, ArrayList<String> path)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
   {
      if(node.getPrev() != null)
      {
         printPath(node.getPrev(), path);
      }
      path.add(node.getCity());
   }
   
   public static void exit()
   {
      System.exit(0);
   }
  
        
         
   
       
                  
       
                
}
