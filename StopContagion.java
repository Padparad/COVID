import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class StopContagion {
    public static class Vertex{
        int number;
        int degree;
        long cInfluence;
        boolean visited;

        //constructors
        //we have multiple constructors to adapted degree and influence data
        Vertex(int num){
            number = num;
        }
        Vertex(int num, int degree){
            this.number = num;
            this.degree = degree;
        }
        Vertex(int num, long cIn){
            this.number = num;
            this.cInfluence = cIn;
            boolean visited = false;
        }

        public int getNumber(){
            return number;
        }
        public int getDegree(){
            return degree;
        }
        public long getCInfluence(){
            return cInfluence;
        }
    }

    //build graph by reading the input file
    public static Graph buildGraph(String input){
        Graph graph = new Graph();
        try{
            File file = new File(input);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                Integer a = scanner.nextInt();
                Integer b = scanner.nextInt();
                graph.addVertex(a);
                graph.addVertex(b);
                graph.addEdge(a,b);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void removeByDegree(Graph graph, int times){
        PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(new Comparator<StopContagion.Vertex>(){
            //build a reversed PQ which vertex with larger degree goes to the top
            public int compare(StopContagion.Vertex v1, StopContagion.Vertex v2)
            {
                int degree1 = v1.getDegree();
                int degree2 = v2.getDegree();
                if(degree1 < degree2){
                    return 1;
                }
                else if(degree2 == degree1){
                    if(v1.getNumber()>v2.getNumber()){
                        return 1;
                    }
                    return -1;
                }
                else{
                    return -1;
                }
            }
        });
        //remove desired number of vertices and print their data
        for(int i = 0; i<times;i++){
            for(Integer a:graph.getAllVertices()){
                Vertex ver = new Vertex((int)a, graph.getDegree(a));
                pq.add(ver);
            }

            if(!graph.isEmpty()){
                int a = (pq.remove()).getNumber();
                System.out.println(a + "\t" + graph.getDegree(a));
                graph.removeVertex(a);
            }
            pq.clear();
        }
    }

    //BONUS function which prints connected vertices in one line
    public static void printConnected(Graph graph){
        HashMap<Integer, Boolean> visited = new HashMap<>();
        for(Integer key:graph.getAllVertices()){
            visited.put(key,false);
        }
        for(Integer a:graph.getAllVertices()){
            if(!visited.get(a)){
                DFS(graph, a,visited);
                System.out.println();
            }
        }
        System.out.println("");
    }

    //helper method for printConnected()
    public static void DFS(Graph graph, Integer a , HashMap<Integer, Boolean> visited){
        visited.replace(a, true);
        System.out.print(a + "\t");
        for(Integer b: graph.getNeighbors(a)){
            if(!visited.get(b)){
                DFS(graph, b, visited);
            }
        }
    }

    public static void removeByInfluence(Graph graph, int times, int radius){
        PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(new Comparator<StopContagion.Vertex>(){
            //similar to removeByDegree(), construct a reversed PQ
            public int compare(StopContagion.Vertex v1, StopContagion.Vertex v2)
            {
                long inf1 = graph.getInfluence(v1.getNumber(), radius);
                long inf2 = graph.getInfluence(v2.getNumber(), radius);
                if(inf1 < inf2){
                    return 1;
                }
                else if(inf2 == inf1){
                    if(v1.getNumber()>v2.getNumber()){
                        return 1;
                    }
                    return -1;
                }
                else{
                    return -1;
                }
            }
        });

        //remove desired number of vertices and their data
        for(int i = 0; i<times;i++){
            for(Integer a:graph.getAllVertices()){
                Vertex ver = new Vertex((int)a, graph.getInfluence(a, radius));
                pq.add(ver);
            }
            if(!graph.isEmpty()){
                int a = (pq.remove()).getNumber();
                System.out.println(a + "\t" + graph.getInfluence(a,radius));
                graph.removeVertex(a);

            }
            pq.clear();
        }

    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please read README before enter input.");
        //read user's input
        String command = scanner.nextLine();
        //extract digits
        String numberOnly = command.replaceAll("[^0-9]", "");
        //last digit must be the number of removal
        int numRemove = Integer.parseInt(numberOnly.substring(numberOnly.length() - 1));
        //default radius is 2
        int radius = 2;
        //if there is more than one digit, the first one must be radius
        if (numberOnly.length() > 1) {
            //then the radius is updated to this number
            radius = Integer.parseInt(numberOnly.substring(0, 1));
        }
        //file name is obtained from the end of the input String
        String fileName = command.substring(command.lastIndexOf(" ") + 1);
        //use file name to construct graph
        Graph graph = buildGraph(fileName);
        //if input contains [-d], the program calls removeByDegree()
        if (command.contains("-d")) {
            //if input contains [-t], we remove vertex one by one and print connected groups after each removal
            if(command.contains("-t")) {
                for(int i = 0; i < numRemove; i++) {
                    removeByDegree(graph, 1);
                    printConnected(graph);
                }
            } else {
                removeByDegree(graph, numRemove);
            }
        } else { //if [-d] is not contained, the program calls removeByInfluence()
            //if input contains [-t], we remove vertex one by one and print connected groups after each removal
            if(command.contains("-t")) {
                for(int i = 0; i < numRemove; i++) {
                    removeByInfluence(graph, 1, radius);
                    printConnected(graph);
                }
            } else {
                removeByInfluence(graph, numRemove, radius);
            }
        }

        //BONUS 2, produce a graph
        JFrame f = new JFrame("Stop Contagion");//Creates the frame
        f.add(graph);//adds canvas to frame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//frame properties
        f.setSize(1100,850);
        f.setVisible(true);
    }
}


