import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Vertex // implements Comparator
{
   private String city;
   private String state;
   private double lat;
   private double lon;
   private int ID;
   private int incount;
   private int outcount;
   private Vertex prev;
   private boolean visited;
   private double minCost = Integer.MAX_VALUE;
   private double minDistance = Integer.MAX_VALUE; //this is going to be constantly changing
                                                //it will also be reset to MAX_VALUE every time
                                                //a shortestpath algorithm is called
 
   
   
   public Vertex (int myid, String citystate, double alon, double alat)
   {
      ID = myid;
      lat = alat;
      lon = alon;
      incount = 0;
      outcount = 0;
      
      String[] both = citystate.split(", ");
      if (both.length == 2)
      {
         city = both[0];
         state = both[1];
      }
      else
      {
         city = both[0];
         state = both[0];
      }
   }
   
   static final Comparator<Vertex> MIN_DISTANCE = 
      new Comparator<Vertex>() {
         public int compare(Vertex v1, Vertex v2) {
            return (int)(Math.signum(v1.getMinDistance() - v2.getMinDistance()));
         }
      };
   static final Comparator<Vertex> MIN_COST = 
      new Comparator<Vertex>() {
         public int compare(Vertex v1, Vertex v2) {
            return (int)(Math.signum(v1.getMinCost() - v2.getMinCost()));
         }
      };
   
   
   public String getCity()
   {
      return city;
   }
   public String getState()
   {
      return state;
   }
   public double getLat()
   {
      return lat;
   }
   public double getLon()
   {
      return lon;
   }
   public int getID()
   {
      return ID;
   }
   public int getIncount()
   {
      return incount;
   }
    
   public int getOutcount()
   {
      return outcount;
   }
   
   public void incIncount()
   {
      incount++;
   }
   public void incOutcount()
   {
      outcount++;
   }
   
   public Vertex getPrev()
   {
      return prev;
   }
   public void setPrev(Vertex pre)
   {
      prev = pre;
   }
   
   public boolean isVisited()
   {
      return visited;
   }
   public void setVisited(boolean b)
   { 
      visited = b;
   }
   public double getMinDistance()
   {
      return minDistance;
   }
   public void setMinDistance(double n)
   {
      minDistance = n;
   }
   public double getMinCost()
   {
      return minCost;
   }
   public void setMinCost(double n)
   {
      minCost = n;
   }

}
