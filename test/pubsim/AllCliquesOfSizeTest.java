package pubsim;

import java.util.Iterator;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Robby McKilliam
 */
public class AllCliquesOfSizeTest {
    
    public AllCliquesOfSizeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSubgraphWithDegreeAtleast() {
        System.out.println("subgraph with degree atleast");
        Graph G = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
        
        //4 vertices
        G.addVertex(1);
        G.addVertex(2);
        G.addVertex(3);
        G.addVertex(4);
        
        G.addEdge(1, 2);
        G.addEdge(1, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 3);
        
        Graph S4 = AllCliquesOfSize.subgraphWithDegreeAtleast(4, G);
        assertTrue(S4.vertexSet().isEmpty());
        
        Graph S3 = AllCliquesOfSize.subgraphWithDegreeAtleast(3, G);
        assertTrue(S3.vertexSet().size() == 1);
        assertTrue(S3.containsVertex(1));
        assertTrue(!S3.containsVertex(2));
        
        Graph S2 = AllCliquesOfSize.subgraphWithDegreeAtleast(2, G);
        assertTrue(S2.vertexSet().size() == 3);
        assertTrue(S2.containsVertex(1));
        assertTrue(S2.containsVertex(2));
        assertTrue(S2.containsVertex(3));
        assertTrue(!S2.containsVertex(4));
        assertTrue(S2.containsEdge(1, 2));
        assertTrue(S2.containsEdge(1, 3));
        assertTrue(S2.containsEdge(3, 2));
        
        //S1 and G should be the same in this case
        Graph S1 = AllCliquesOfSize.subgraphWithDegreeAtleast(1, G);
        assertTrue(S1.vertexSet().containsAll(G.vertexSet()));
        assertTrue(G.vertexSet().containsAll(S1.vertexSet()));
        assertTrue(G.edgeSet().containsAll(S1.edgeSet()));
        assertTrue(S1.edgeSet().containsAll(G.edgeSet()));
              
    }
    
    @Test
    public void testSubgraphConnectedTo() {
        System.out.println("subgraph connected to");
        Graph G = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
        
        //4 vertices
        G.addVertex(1);
        G.addVertex(2);
        G.addVertex(3);
        G.addVertex(4);
        
        G.addEdge(1, 2);
        G.addEdge(1, 3);
        G.addEdge(1, 4);
        G.addEdge(2, 3);
        
        Graph S4 = AllCliquesOfSize.subgraphConnectedTo(4, G);
        assertTrue(S4.vertexSet().size() == 1);
        assertTrue(S4.containsVertex(1));
        
        Graph S3 = AllCliquesOfSize.subgraphConnectedTo(3, G);
        assertTrue(S3.vertexSet().size() == 2);
        assertTrue(S3.containsVertex(1));
        assertTrue(S3.containsVertex(2));
        assertTrue(S3.containsEdge(1, 2));
        
        Graph S2 = AllCliquesOfSize.subgraphConnectedTo(2, G);
        assertTrue(S2.vertexSet().size() == 2);
        assertTrue(S2.containsVertex(1));
        assertTrue(S2.containsVertex(3));
        assertTrue(S2.containsEdge(1, 3));
        
        Graph S1 = AllCliquesOfSize.subgraphConnectedTo(1, G);
        assertTrue(S1.vertexSet().size() == 3);
        assertTrue(S1.containsVertex(2));
        assertTrue(S1.containsVertex(3));
        assertTrue(S1.containsVertex(4));
        assertTrue(S1.containsEdge(2, 3));
        assertTrue(!S1.containsEdge(2, 4));
        assertTrue(!S1.containsEdge(3, 4));
        assertTrue(S1.edgesOf(4).isEmpty());
              
    }
    
}
