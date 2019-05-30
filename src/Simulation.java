import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Simulation {

//    final Problem problem = new HeatTransfer();
    static Problem problem;

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

                problem.makeA1(p3a.m_vertex,1d/k,dt,i*dt,barrier).start();
                problem.makeA(p3b.m_vertex,1d/k,dt,i*dt,barrier).start();
                problem.makeA(p3c.m_vertex,1d/k,dt,i*dt,barrier).start();
                problem.makeA(p3d.m_vertex,1d/k,dt,i*dt,barrier).start();
                problem.makeA(p3e.m_vertex,1d/k,dt,i*dt,barrier).start();
                problem.makeAN(p3f.m_vertex,1d/k,dt,i*dt,barrier).start();
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

    public Vertex runLevels(int levels, double dt, int steps, Function<Double, Double> initialState) {
        Vertex S = new Vertex(null, null, null, "S");
        try {
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();
            List<List<Production>> levelProductions = new LinkedList<>();
            levelProductions.add(new LinkedList<>());
            levelProductions.get(0).add(p1);
            //PrettyPrint.printTreeStructure(p1.m_vertex);
            for(int i=0;i<levels-2;i++)
            {
                levelProductions.add(new LinkedList<>());
                barrier = new CyclicBarrier((int)Math.pow(2, (i+1)) + 1);
                for(int j=0;j<levelProductions.get(i).size();j++) {
                    P2 p_left = new P2(levelProductions.get(i).get(j).m_vertex.m_left, barrier);
                    P2 p_right = new P2(levelProductions.get(i).get(j).m_vertex.m_right, barrier);
                    levelProductions.get(i+1).add(p_left);
                    levelProductions.get(i+1).add(p_right);
                    p_left.start();
                    p_right.start();
                }
                barrier.await();
            }
            //PrettyPrint.printTreeStructure(p1.m_vertex);
            levelProductions.add(new LinkedList<>());
            barrier = new CyclicBarrier((int)Math.pow(2,(levels-1)) + 1);
            for(int j=0;j<levelProductions.get(levels-2).size();j++) {
                P3 p_left = new P3(levelProductions.get(levels-2).get(j).m_vertex.m_left, barrier);
                P3 p_right = new P3(levelProductions.get(levels-2).get(j).m_vertex.m_right, barrier);
                levelProductions.get(levels-1).add(p_left);
                levelProductions.get(levels-1).add(p_right);
                p_left.start();
                p_right.start();
//                System.out.println(levelProductions.get(levels-1).size());
//                System.out.println((int)Math.pow(2,(levels-1)) + 1);
            }
            barrier.await();
            int leavesCount = (int)Math.pow(2,levels-1);
            for(int j=0;j<levelProductions.get(levels-1).size();j++)
            {
                levelProductions.get(levels-1).get(j).m_vertex.m_x[1]=initialState.apply(1.0/leavesCount * j);
            }
            List<Vertex> s = collectLeaves(p1.m_vertex);

            double[] init = new double[leavesCount+1];
            for(int i=0;i<leavesCount;i++)
            {
                init[i] = s.get(i).m_x[1];
            }
            init[leavesCount]=s.get(leavesCount-1).m_x[2];
            plotSolution(init);
            for (int i = 0; i < steps; ++i) {
                barrier = new CyclicBarrier(leavesCount+1);

                problem.makeA1(levelProductions.get(levels-1).get(0).m_vertex, 1d / leavesCount, dt, i * dt, barrier).start();
                for(int leafIndex=1;leafIndex<levelProductions.get(levels-1).size()-1;leafIndex++)
                {
                    problem.makeA(levelProductions.get(levels-1).get(leafIndex).m_vertex, 1d / leavesCount, dt, leafIndex * dt, barrier).start();
                }
                problem.makeAN(levelProductions.get(levels-1).get(levelProductions.get(levels-1).size()-1).m_vertex, 1d / leavesCount, dt, i * dt, barrier).start();
                barrier.await();
                for(int level=levels-2;level>0;level--)
                {
                    barrier = new CyclicBarrier(levelProductions.get(level).size()+1);
                    for(int j=0;j<levelProductions.get(level).size();j++)
                    {
                        A2 a2 = new A2(levelProductions.get(level).get(j).m_vertex,barrier);
                        a2.start();
                    }
                    barrier.await();
                    barrier = new CyclicBarrier(levelProductions.get(level).size()+1);
                    for(int j=0;j<levelProductions.get(level).size();j++)
                    {
                        E2 e2 = new E2(levelProductions.get(level).get(j).m_vertex,barrier);
                        e2.start();
                    }
                    barrier.await();

                }
                barrier = new CyclicBarrier(2);
                Aroot aroot = new Aroot(p1.m_vertex,barrier);
                aroot.start();
                barrier.await();
//                System.out.println(p1.m_vertex);
                barrier = new CyclicBarrier(2);
                Eroot eroot = new Eroot(p1.m_vertex,barrier);
                eroot.start();
                barrier.await();
//                System.out.println(p1.m_vertex);
                for(int level=0;level<levels-2;level++)
                {
                    barrier = new CyclicBarrier(levelProductions.get(level).size()+1);
                    for(int j=0;j<levelProductions.get(level).size();j++)
                    {
                        BS bs = new BS(levelProductions.get(level).get(j).m_vertex,barrier);
                        bs.start();
                    }
                    barrier.await();
                }
                barrier = new CyclicBarrier(levelProductions.get(levels-2).size()+1);
                for(int productionIndex=0;productionIndex<levelProductions.get(levels-2).size();productionIndex++)
                {
                    BSA bsa = new BSA(levelProductions.get(levels-2).get(productionIndex).m_vertex,barrier);
                    bsa.start();
                }
                barrier.await();
                // Get the solution from the leaves and plot it
                double[] solution =  new double[leavesCount+1];
                s = collectLeaves(p1.m_vertex);
                for(int j=0;j<leavesCount;j++)
                {
                    solution[j] = s.get(j).m_x[1];
                }
                solution[leavesCount]=s.get(leavesCount-1).m_x[2];
                plotSolution(solution);
            }
            for(int i=0;i<levelProductions.get(levels-1).size();i++) {
//                System.out.println( );
//                System.out.println(i);
//                System.out.println(levelProductions.get(levels-1).get(i).m_vertex);
//                System.out.println( );
            }




//            barrier = new
//            A1 a1 = new A1(levelProductions.get(levels).get(0).m_vertex,barrier);
//            AN an = new AN(levelProductions.get(levels).get(2^(levels-1)).m_vertex,barrier);


            return S;
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private void plotSolution(double[] values) throws InterruptedException {
        int delay = 1000;
        ResultPrinter.printResult(values);
        ResultPrinter.plot.setFixedBounds(1, -1.5, 1.5);
        //TimeUnit.MILLISECONDS.sleep(delay);
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

        int k = 8;
        double dt = 0.001;
        int steps = 10000;
//        problem = new HeatTransfer();
        //s.runLevels(k, dt, steps, x -> Math.sin(2 * Math.PI * x));
        problem = new FlowingWave();
        //problem = new Excercise2();
        //s.runLevels(k, dt, steps, x -> Math.sin(2 * Math.PI * x));
//        s.runLevels(k, dt, steps, x -> 0.0);
        s.run(6, dt, steps, x->Math.sin(2 * Math.PI * x));
    }
}
