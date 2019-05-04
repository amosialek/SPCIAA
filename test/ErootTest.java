import org.junit.Before;
import org.junit.Test;

public class ErootTest extends ProductionTest {

    private Vertex node;

    @Before
    public void prepareMatrix() {
        node = new Vertex(null, null, null, "");
        node.m_a = new double[][]{
                {1, -3, +3},
                {2, +3, -1},
                {4, -3, -1}
        };
        node.m_b = new double[]{-4, 15, 19};

        setupBarrier(1);
    }

    @Test
    public void rootSolverWorks() {
        Production eliminate = new Eroot(node, barrier);
        eliminate.run();

        double[] reorderedSolution = {1, 5, -2};
        assertVectorEquals(reorderedSolution, node.m_x);
    }

}
