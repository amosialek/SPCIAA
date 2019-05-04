public class PrettyPrint {

    public static String print(double[] b) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (double val : b) {
            sb.append(String.format(" %6.3f ", val));
        }
        sb.append(")");
        return sb.toString();
    }

    public static String print(double[][] m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(String.format(" %6.3f  %6.3f  %6.3f \n", m[i][0], m[i][1], m[i][2]));
        }
        return sb.toString();
    }

    public static String print(double[][] m, double[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(String.format(" %6.3f  %6.3f  %6.3f  ~  %6.3f \n", m[i][0], m[i][1], m[i][2], b[i]));
        }
        return sb.toString();
    }

    public static String print(double[][] m, double[] x, double[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(String.format(" %6.3f  %6.3f  %6.3f  | %6.3f  ~  %6.3f \n", m[i][0], m[i][1], m[i][2], x[i], b[i]));
        }
        return sb.toString();
    }

    public static String printTreeStructure(Vertex root) {
        StringBuilder sb = new StringBuilder();
        processTree(root, "", sb);
        return sb.toString();
    }

    private static void processTree(Vertex node, String prefix, StringBuilder sb) {
        sb.append(prefix + node.m_label).append('\n');
        String newPrefix = prefix + "|  ";
        if (node.m_left != null) {
            processTree(node.m_left, newPrefix, sb);
        }
        if (node.m_right != null) {
            processTree(node.m_right, newPrefix, sb);
        }
    }
}
