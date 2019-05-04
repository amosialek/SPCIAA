import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PTest extends ProductionTest {

    private Vertex node;

    @Before
    public void prepareRoot() {
        node = new Vertex(null, null, null, "");
        setupBarrier(1);
    }

    @Test
    public void productionP1Works() {
        Production p = new P1(node, barrier);
        p.run();

        assertNotNull("left child missing", node.m_left);
        assertNotNull("right child missing", node.m_right);

        assertSame("invalid parent in left child", node, node.m_left.m_parent);
        assertSame("invalid parent in right child", node, node.m_right.m_parent);

        assertEquals("invalid label in vertex", "root", node.m_label);
        assertEquals("invalid label in left child", "T", node.m_left.m_label);
        assertEquals("invalid label in right child", "T", node.m_right.m_label);
    }

    @Test
    public void productionP2Works() {
        Production p = new P2(node, barrier);
        p.run();

        assertNotNull("left child missing", node.m_left);
        assertNotNull("right child missing", node.m_right);

        assertSame("invalid parent in left child", node, node.m_left.m_parent);
        assertSame("invalid parent in right child", node, node.m_right.m_parent);

        assertEquals("invalid label in vertex", "int", node.m_label);
        assertEquals("invalid label in left child", "T", node.m_left.m_label);
        assertEquals("invalid label in right child", "T", node.m_right.m_label);
    }

    @Test
    public void productionP3Works() {
        Production p = new P3(node, barrier);
        p.run();

        assertNotNull("left child missing", node.m_left);
        assertNotNull("right child missing", node.m_right);

        assertSame("invalid parent in left child", node, node.m_left.m_parent);
        assertSame("invalid parent in right child", node, node.m_right.m_parent);

        assertEquals("invalid label in vertex", "int", node.m_label);
        assertEquals("invalid label in left child", "node", node.m_left.m_label);
        assertEquals("invalid label in right child", "node", node.m_right.m_label);
    }
}
