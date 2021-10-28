// Imports package for the program
import java.util.*;
import java.util.LinkedList;
import java.io.*;

/**
 *  This program provides shortest path available from a source to destination where both source and destination represents
 *  states of any country using bredth first search
 */
public class RoadTrip
{
    Graph g = new Graph();                                     // an object of Graph class

    private Set<String> state_names = new TreeSet<>();          // TreeSet to store the name of the states from the file without repetition in ascending order
    private LinkedList<Integer> re_name = new LinkedList<>() ;  // Linked list to store index of the states without repetition
    private ArrayList<String> chain = new ArrayList<>();        // Array list to store names of states in ascending order
    private int values = 0;                                     // integer that sets the first integer value of the state to 0
    private String temp;                                        // a string variable

    /**
     * Constructor
     * @param filename The file containing data of states
     *
     */
    public RoadTrip (String filename)
    {
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNext())            // reads the next states from the file until we get to the end of the file
            {
                state_names.add(reader.next());  // adds the name of the states to the state_names TreeSet
            }

            state_int();                        // invokes state_int method that changes the string value of states to integer
            vertex();                           // invokes the vertex method that adds all the vertex from the file
            reader.close();                     // closes the file
            edges(filename);                    // invokes the edges method where the file name is passed as an argument
        }
        // catch block to throw and exception if the file is not found
        catch (FileNotFoundException e)
        {
            System.out.println("Sorry! File not found");
        }
    }

    /**
     * state_int method
     * @return  the minimum cost to reach from the beginning of the array to the final index of the array
     */
    public void state_int()
    {
        // enhanced for loop to loop all the states name from a state_names TreeSet in ascending order
        for (String a: state_names)
        {
            temp = String.valueOf(values);     // changes the integer value of values variable to string and stores to temp
            chain.add(a);                      // storing the values of all states in chain arraylist in ascending order
            a = temp;
            re_name.add(Integer.parseInt(a));  // changing the values of states to integers starting from 1; in increasing order
            values ++;                         // increments the value of values by 1 which gets assigned to the next state
        }
    }

    /**
     * Method to add all the vertex(states) as an integer
     */
    public void vertex()
    {
        while (!re_name.isEmpty())  // loops while the re_name linkedList is empty
        {
            g.addVertex(re_name.pop());  // adds the elements in edge popped up from linkedList
        }
    }

    /**
     * Method to add all the edges as an integer
     * @param filename The name of file that contains data of States
     * @throws FileNotFoundException when the given file doesn't exsist
     */
    public void edges(String filename)throws FileNotFoundException
    {
        File a = new File(filename);
        Scanner reader = new Scanner(a);
        while (reader.hasNext())
        {
            g.addEdge(chain.indexOf(reader.next()),chain.indexOf(reader.next()) );
        }
    }

    /**
     * Metthod that gives path from source State to destination states
     * @param source The state from which the path starts
     * @param destination The states at which the path ends
     * @return pathInfo The information regarding the path from source to destination
     */
    public String getPath (String source, String destination)
    {
        Set <Integer> s1 = new LinkedHashSet<>();           //Set to keep record of traversed vertices
        Queue <Integer> q1 = new LinkedList<>();            //Queue to store vertices to be traversed in first come first serve order

        q1.add(chain.indexOf(source));                      // Adds source vertex to queue to explore its adjacent states
        int[] route = new int[g.vertices()];                // Array to keep record of routes through which destination is reached

        int parent;                                         // Integer to store parent vertice
        String pathInfo = "";                               // String to store information regarding path from source to vertex

        boolean pathFound = false;                          // Boolean value to track if the destination vertex is found or not

        /**
         * Records path from source to destination untill the destination is reached
         */
        while (! q1.isEmpty() & !pathFound)
        {
            parent = q1.peek();                              // Making the vertex parent to explore its children
            Iterator<Integer> itr = g.getAdjacent(parent);   // Getting all adjacent or children vertices
            s1.add(q1.remove());                             // Adding the vertices in already traversed set and removing it from queue

            /**
             * Traversing through each adjacent vertices to find destination
             */
            while (itr.hasNext() & !pathFound)
            {
                int vertex = itr.next();                    // Integer to store next adjacent state to explore

                // Adss the vertex to queue to explore later if it is not already traversed
                if(!s1.contains(vertex))
                {
                    q1.add(vertex);                         // Adds to queue
                    s1.add(vertex);                         // Adds vertex to already traversed set
                    route[vertex] = parent;                 // Adds parent vertex to route

                    // Sets the boolean pathFound to true to terminate the loop
                    if(vertex == chain.indexOf(destination)) {
                        pathFound = true;
                    }
                }

            }
        }

        Stack<String> way = new Stack<>();                  // Stack to store path
        int i = chain.indexOf(destination);                 // integer to store integer value of states
        int borders = 1;                                    // Integer to record total number of borders crossed to reach destination

        way.push(destination);                              // Push destination to stack

        /**
         * Counts borders crossed
         */
        while(!(chain.indexOf(source) == route[i]))
        {
            way.push(chain.get(route[i]));
            i = route[i];
            borders++;
        }

        String road = source;                               // Initializes road from source

        // adds all states to road
        while(!way.isEmpty())
        {
            road += " -> " + way.pop();
        }

        // Adds path information if destination is found from the source
        if(pathFound)
        {
            pathInfo = "There is a path from " + source + " to " + destination +
                    "\nThe shortest path requires crossing " + borders + " borders" +
                    "\nPath: [" +road + "]";

            return pathInfo;
        }

        // Adds path information if destination cannot be reached from the source
        pathInfo = "There is no path from " + source + " to " + destination;
        return pathInfo;
    }
}
