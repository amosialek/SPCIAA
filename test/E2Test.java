import org.junit.Before;
import org.junit.Test;

public class E2Test extends ProductionTest {

    private Vertex node;

    @Before
    public void prepareMatrix() {
        node = new Vertex(null, null, null, "");
        node.m_a = new double[][]{
                {2, -1, -1},
                {-1, 1, 0},
                {-1, 0, 1}
        };
        node.m_b = new double[]{1, 2, 3};

        setupBarrier(1);
    }

    @Test
    public void partialEliminationWorks() {
        Production eliminate = new E2(node, barrier);
        eliminate.run();

        double[][] matrix = {
                {1, -0.5, -0.5},
                {0, 0.5, -0.5},
                {0, -0.5, 0.5}
        };
        double[] rhs = {0.5, 2.5, 3.5};

        assertMatrixEquals(matrix, node.m_a);
        assertVectorEquals(rhs, node.m_b);
    }

}
