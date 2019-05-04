import org.junit.Before;
import org.junit.Test;

public class A2Test extends ProductionTest {

    private Vertex left;
    private Vertex right;
    private Vertex result;

    @Before
    public void prepareMatrices() {
        left = new Vertex(null, null, null, "");
        right = new Vertex(null, null, null, "");

        left.m_a[1][1] = 1;
        left.m_a[1][2] = 2;
        left.m_a[2][1] = 3;
        left.m_a[2][2] = 4;
        left.m_b[1] = 1;
        left.m_b[2] = 2;

        right.m_a[1][1] = 5;
        right.m_a[1][2] = 6;
        right.m_a[2][1] = 7;
        right.m_a[2][2] = 8;
        right.m_b[1] = 3;
        right.m_b[2] = 4;

        result = new Vertex(left, right, null, "");
        left.set_parent(result);
        right.set_parent(result);

        setupBarrier(1);
    }

    @Test
    public void mergingWorks() {
        Production merge = new A2(result, barrier);
        merge.run();

        double[][] matrix = {
                {9, 3, 6},
                {2, 1, 0},
                {7, 0, 8}
        };
        double[] rhs = {5, 1, 4};

        assertMatrixEquals(matrix, result.m_a);
        assertVectorEquals(rhs, result.m_b);
    }

}
