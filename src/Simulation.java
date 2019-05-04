import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Simulation {

    final Problem problem = new HeatTransfer();

    public void run(int k, double dt, int steps, Function<Double, Double> initialState) throws Exception {
        // build the tree with k levels (P1, P2, P3)
        // ...
        //[(P1)]
        k=6;
        Vertex S = new Vertex(null, null, null, "S");
        CyclicBarrier barrier = new CyclicBarrier(1 + 1);
        P1 p1 = new P1(S, barrier);
        p1.start();
        barrier.await();
        //[(P2)1(P2)2]
        barrier = new CyclicBarrier(2 + 1);
        P2 p2a = new P2(p1.m_vertex.m_left, barrier);
        P2 p2b = new P2(p1.m_vertex.m_right, barrier);
        p2a.start();
        p2b.start();
        barrier.await();
        barrier = new CyclicBarrier(4 + 1);
        P2 p2c = new P2(p2a.m_vertex.m_left, barrier);
        P2 p2d = new P2(p2a.m_vertex.m_right, barrier);
        P3 p3e = new P3(p2b.m_vertex.m_left, barrier);
        P3 p3f = new P3(p2b.m_vertex.m_right, barrier);
        p2c.start();
        p2d.start();
        p3e.start();
        p3f.start();
        barrier.await();
        //[(P3)1(P3)2(P3)3(P3)4]
        barrier = new CyclicBarrier(4 + 1);
        P3 p3a = new P3(p2c.m_vertex.m_left, barrier);
        P3 p3b = new P3(p2c.m_vertex.m_right, barrier);
        P3 p3c = new P3(p2d.m_vertex.m_left, barrier);
        P3 p3d = new P3(p2d.m_vertex.m_right, barrier);
        p3a.start();
        p3b.start();
        p3c.start();
        p3d.start();
        barrier.await();
        p3a.m_vertex.m_x[1]=initialState.apply(0d);
        p3a.m_vertex.m_x[2]=p3b.m_vertex.m_x[1]=initialState.apply(1d/6);
        p3b.m_vertex.m_x[2]=p3c.m_vertex.m_x[1]=initialState.apply(2d/6);
        p3c.m_vertex.m_x[2]=p3d.m_vertex.m_x[1]=initialState.apply(3d/6);
        p3d.m_vertex.m_x[2]=p3e.m_vertex.m_x[1]=initialState.apply(4d/6);
        p3e.m_vertex.m_x[2]=p3f.m_vertex.m_x[1]=initialState.apply(5d/6);
        p3f.m_vertex.m_x[2]=initialState.apply(6d/6);


        // Plot the initial state
        List<Vertex> s = collectLeaves(p1.m_vertex);
        double[] init = new double[7];// ...
        for(int i=0;i<6;i++)
        {
            init[i] = s.get(i).m_x[1];
        }
        init[6]=s.get(5).m_x[2];
        plotSolution(init);

        for (int i = 0; i < steps; ++i)
        {
            barrier = new CyclicBarrier(7);
                HeatTransfer ht = new HeatTransfer();
                ht.makeA1(p3a.m_vertex,1d/k,dt,i*dt,barrier).start();
                ht.makeA(p3b.m_vertex,1d/k,dt,i*dt,barrier).start();
                ht.makeA(p3c.m_vertex,1d/k,dt,i*dt,barrier).start();
                ht.makeA(p3d.m_vertex,1d/k,dt,i*dt,barrier).start();
                ht.makeA(p3e.m_vertex,1d/k,dt,i*dt,barrier).start();
                ht.makeAN(p3f.m_vertex,1d/k,dt,i*dt,barrier).start();
                barrier.await();
            barrier = new CyclicBarrier(3+1);
            A2 mergedMat1 = new A2(p2c.m_vertex, barrier);
            A2 mergedMat2 = new A2(p2d.m_vertex, barrier);
            A2 mergedMat3 = new A2(p2b.m_vertex, barrier);
            mergedMat1.start(); mergedMat2.start(); mergedMat3.start();
            barrier.await();
            barrier = new CyclicBarrier(3+1);
            E2 emergedMat1 = new E2(p2c.m_vertex, barrier);
            E2 emergedMat2 = new E2(p2d.m_vertex, barrier);
            E2 emergedMat3 = new E2(p2b.m_vertex, barrier);
            emergedMat1.start(); emergedMat2.start(); emergedMat3.start();
            barrier.await();
            barrier = new CyclicBarrier(1+1);
            A2 a24 = new A2(p2a.m_vertex,barrier);
            a24.start();
            barrier.await();
            barrier = new CyclicBarrier(1+1);
            E2 e24 = new E2(p2a.m_vertex,barrier);
            e24.start();
            barrier.await();
            barrier = new CyclicBarrier(1+1);
            Aroot aroot = new Aroot(p1.m_vertex,barrier);
            aroot.start();
            barrier.await();
            barrier = new CyclicBarrier(1+1);
            Eroot eroot = new Eroot(p1.m_vertex,barrier);
            eroot.start();
            barrier.await();
            System.out.println(S);
            barrier = new CyclicBarrier(1+1);
            BS bsRoot = new BS(p1.m_vertex,barrier);

            bsRoot.start();
            barrier.await();
            barrier = new CyclicBarrier(2+1);
            BS bs1 = new BS(p2a.m_vertex,barrier);
            BSA bsa1 = new BSA(p2b.m_vertex,barrier);
            bs1.start();
            bsa1.start();
            barrier.await();
            barrier = new CyclicBarrier(2+1);
            BSA bsa2 = new BSA(p2c.m_vertex,barrier);
            BSA bsa3 = new BSA(p2d.m_vertex,barrier);
            bsa2.start();
            bsa3.start();
            barrier.await();

            // Get the solution from the leaves and plot it
            double[] solution =  new double[7]; // ...
            s = collectLeaves(p1.m_vertex);
            for(int j=0;j<6;j++)
            {
                solution[j] = s.get(j).m_x[1];
            }
            solution[6]=s.get(5).m_x[2];
            plotSolution(solution);

        }
    }

    private void plotSolution(double[] values) throws InterruptedException {
        int delay = 1000;
        ResultPrinter.printResult(values);
        ResultPrinter.plot.setFixedBounds(1, -1.5, 1.5);
        TimeUnit.MILLISECONDS.sleep(delay);
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
    public static void main(String[] args) throws Exception {
        Simulation s = new Simulation();

        int k = 3;
        double dt = 0.01;
        int steps = 10;
        s.run(k, dt, steps, x -> Math.sin(2 * Math.PI * x));
    }
}
