/**
 * @ClassName Graph
 * @Description TODO
 * @Author Yulu Liu
 * @Date 04/13/2022
 * @Version 1.0
 **/
import javax.swing.*;
import java.awt.*;
import java.util.*;


public class Graph extends JPanel {
    public static class Coordinate{
        int number;
        int x;
        int y;
        //constructor
        public Coordinate( int num, int x , int y){
            number= num;
            this.x = x;
            this.y = y;
        }
        int getX(){
            return x;
        }
        int getY(){
            return y;
        }
        void changeX(int x){
            this.x = x;
        }
        void changeY(int y){
            this.y = y;
        }
    }
    final private HashMap<Integer, Coordinate> coordinateMap;
    final private HashMap<Integer, Set<Integer>> adjacencyList;
    final private HashMap<Integer, Integer> shortestPath;
    final private Set<Integer> removed;

    /**
     * Create new Graph object.
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.shortestPath = new HashMap<>();
        this.coordinateMap = new HashMap<>();
        this.removed = new HashSet<Integer>();
    }

    public boolean conflict(int x1, int y1){
        for(Integer a: coordinateMap.keySet()){
            int x = (coordinateMap.get(a)).getX();
            int y = (coordinateMap.get(a)).getY();
            if(Math.abs(x1-x) < 22|| Math.abs(y1-y) < 22){
                return false;
            }
        }
        return true;
    }

    public void printCoordinate(){
        for(Integer a: coordinateMap.keySet()){
            int x = (coordinateMap.get(a)).getX();
            int y = (coordinateMap.get(a)).getY();
            System.out.println(a+ "is at   (" + x + "," + y + ")");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        for(Integer a : getAllVertices()){
            graphics.setColor(Color.yellow);
            graphics.fillOval((coordinateMap.get(a)).getX()-5,(coordinateMap.get(a)).getY()-10,20,20);
            graphics.setColor(Color.black);
            graphics.drawString(Integer.toString(a), (coordinateMap.get(a)).getX() +2,(coordinateMap.get(a)).getY()+7);
        }
        for(Integer b: removed){
            graphics.setColor(Color.red);
            graphics.fillOval((coordinateMap.get(b)).getX()-5,(coordinateMap.get(b)).getY()-10,20,20);
            graphics.setColor(Color.black);
            graphics.drawString(Integer.toString(b), (coordinateMap.get(b)).getX() +2,(coordinateMap.get(b)).getY()+7);
        }
        for(Integer c :getAllVertices()){
            for(Integer d : getNeighbors(c)){
                graphics.setColor(Color.black);
                graphics.drawLine((coordinateMap.get(c)).getX(),(coordinateMap.get(c)).getY(),(coordinateMap.get(d)).getX(),(coordinateMap.get(d)).getY());
            }
        }
    }

    public void addVertex(Integer v) {
        if (this.adjacencyList.containsKey(v)) {

        }
        else{
            if(getAllVertices() == null){
                coordinateMap.put(v, new Coordinate(v, (int)Math.floor(Math.random()*(1080-20+1)+20), (int)Math.floor(Math.random()*(980-20+1)+20)));
            }
            this.adjacencyList.put(v, new HashSet<Integer>());
            boolean good = true;
            while(good){
                int newX = (int)Math.floor(Math.random()*(1080-20+1)+20);
                int newY = (int)Math.floor(Math.random()*(980-20+1)+20);
                if((newX <=1080&&newX>=20) && (newY<=980&&newY>=20) && conflict(newX,newY)){
                    coordinateMap.put(v, new Coordinate(v, newX, newY));
                    good = false;
                }
            }



        }
    }

    public void removeVertex(Integer v) {
        if (!this.adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException("Vertex doesn't exist.");
        }

        this.adjacencyList.remove(v);


        for (Integer u: this.getAllVertices()) {
            this.adjacencyList.get(u).remove(v);
        }
        removed.add(v);
    }

    public void addEdge(Integer v, Integer u) {
        if (!this.adjacencyList.containsKey(v) || !this.adjacencyList.containsKey(u)) {
            throw new IllegalArgumentException();
        }

        this.adjacencyList.get(v).add(u);
        this.adjacencyList.get(u).add(v);
    }

    public boolean isAdjacent(Integer v, Integer u) {
        return this.adjacencyList.get(v).contains(u);
    }

    public Iterable<Integer> getNeighbors(Integer v) {
        return this.adjacencyList.get(v);
    }

    public int getDegree(Integer v){
        int degree = 0;
        for(Integer u: getNeighbors(v)){
            degree++;
        }
        return degree;
    }

    public long getInfluence(Integer v, int radius){
        int sum = 0;
        organizePath(v);
        for(Integer a: shortestPath.keySet()){
            if(shortestPath.get(a) == radius){
                sum += getDegree(a) -1;
            }
        }
        int result = (getDegree(v)-1)*sum;
        return result;
    }

    public void printHashMap(){
        for(Integer a: shortestPath.keySet()){
            System.out.println("distance from source to" + a + "is" + shortestPath.get(a));
        }
    }

    public void organizePath(Integer v){
        //initialize
        shortestPath.clear();
        for(Integer a: getAllVertices()){
            shortestPath.put(a,Integer.MAX_VALUE );
        }
        Set<Integer> settled = new HashSet<>();
        Set<Integer> unsettled = new HashSet<>();
        unsettled.add(v);
        shortestPath.replace(v,0);
        //organize Path using dijkstra algo
        while(unsettled.size() != 0){
            Integer lowest = Integer.MAX_VALUE;
            Integer current = 0;
            for(Integer a : unsettled){
                if(shortestPath.get(a) < lowest){
                    lowest = shortestPath.get(a);
                    current = a;
                }
            }
            unsettled.remove(current);
            for(Integer b: getNeighbors(current)){
                if(!settled.contains(b)){
                    if(shortestPath.get(current) !=Integer.MAX_VALUE && shortestPath.get(current)+1< shortestPath.get(b)){
                        shortestPath.replace(b, shortestPath.get(current)+1);
                        unsettled.add(b);
                    }
                }

            }
            settled.add(current);
        }


    }

    public Iterable<Integer> getAllVertices() {
        return this.adjacencyList.keySet();
    }

    public int getLargest(){
        int max = 0;
        for(Integer u:getAllVertices()){
            if((int)u>max){
                max = (int)u;
            }
        }
        return max;
    }

    public boolean isEmpty(){
        return getAllVertices()==null;
    }

    public void printGraph(){

    }


}