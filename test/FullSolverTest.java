import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FullSolverTest {

    @Test
    public void buildingTreeWorks() {
        Executor executor = new Executor();
        Vertex root = executor.run();

        String tree = PrettyPrint.printTreeStructure(root);
        String msg = "Invalid tree:\n" + tree;

        assertEquals(msg, root.m_label, "root");
        checkSubtree(msg, root.m_left, 3);
        checkSubtree(msg, root.m_right, 2);
    }

    @Test
    public void solvingWorks() {
        Executor executor = new Executor();
        Vertex root = executor.run();

        double[] expected = expectedSolution(6);
        double[] solution = extractSolution(collectLeaves(root));

        String msg = "Incorrect solution\n" +
                "Expected: " + PrettyPrint.print(expected) + "\n" +
                "Actual  : " + PrettyPrint.print(solution) + "\n";
        assertArrayEquals(msg, expected, solution, ProductionTest.EPS);
    }

    @Test
    public void buildingUniformTreeWorks() {
        testBuilding(2);
        testBuilding(3);
        testBuilding(4);
    }

    @Test
    public void solvingUniformTreeWorks() {
//        testSolving(2);
        testSolving(3);
        testSolving(4);
    }

    private void testSolving(int levels) {
        Executor executor = new Executor();
        Vertex root = executor.run(levels);

        int elements = (int) Math.round(Math.pow(2, levels - 1));
        double[] expected = expectedSolution(elements);
        double[] solution = extractSolution(collectLeaves(root));

        String msg = "Incorrect solution for " + levels + "levels\n" +
                "Expected: " + PrettyPrint.print(expected) + "\n" +
                "Actual  : " + PrettyPrint.print(solution) + "\n";
        assertArrayEquals(msg, expected, solution, ProductionTest.EPS);
    }

    private void testBuilding(int levels) {
        Executor executor = new Executor();
        Vertex root = executor.run(levels);

        String tree = PrettyPrint.printTreeStructure(root);
        String msg = "Invalid tree for " + levels + " levels:\n" + tree;

        assertEquals(msg, root.m_label, "root");
        checkSubtree(msg, root.m_left, levels - 1);
        checkSubtree(msg, root.m_right, levels - 1);
    }


    private double[] expectedSolution(int elements) {
        double h = 1. / elements;
        double[] expected = new double[elements + 1];
        for (int i = 0; i <= elements; ++i) {
            expected[i] = i * h;
        }
        return expected;
    }

    private void checkSubtree(String msg, Vertex node, int depth) {
        assertNotNull(msg, node);
        assertEquals(msg, node.m_label, depth > 0 ? "int" : "node");

        if (depth > 0) {
            checkSubtree(msg, node.m_left, depth - 1);
            checkSubtree(msg, node.m_right, depth - 1);
        }
    }

    private List<Vertex> collectLeaves(Vertex root) {
        List<Vertex> leaves = new ArrayList<>();
        Deque<Vertex> nodes = new ArrayDeque<>();
        nodes.push(root);

        while (!nodes.isEmpty()) {
            Vertex v = nodes.pop();
            if (v.m_left.m_label.equals("node")) {
                leaves.add(v);
            } else {
                nodes.add(v.m_right);
                nodes.add(v.m_left);
            }
        }
        Collections.reverse(leaves);
        return leaves;
    }

    private double[] extractSolution(final List<Vertex> leafs) {
        double[] solution = new double[leafs.size() + 1];
        solution[0] = leafs.get(0).m_x[1];
        for (int i = 0; i < leafs.size(); ++i) {
            solution[i + 1] = leafs.get(i).m_x[2];
        }
        return solution;
    }

}
