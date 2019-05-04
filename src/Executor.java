import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
//zadanie z numerkiem 8.
class Executor {

    public Vertex run() {
        Vertex S = new Vertex(null, null, null, "S");
        try {
            //[(P1)]
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
            barrier = new CyclicBarrier(6+1);
            A1 localMat1 = new A1(p3a.m_vertex, barrier);
            A localMat2 = new A(p3d.m_vertex, barrier);
            A localMat3 = new A(p3e.m_vertex, barrier);
            A localMat4 = new A(p3c.m_vertex, barrier);
            A localMat5 = new A(p3b.m_vertex, barrier);
            AN localMat6 = new AN(p3f.m_vertex, barrier);
            localMat1.start(); localMat2.start(); localMat3.start();
            localMat4.start(); localMat5.start(); localMat6.start();
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
            return S;
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    public Vertex run(int levels) {
        Vertex S = new Vertex(null, null, null, "S");
        try {
            CyclicBarrier barrier = new CyclicBarrier(1 + 1);
            P1 p1 = new P1(S, barrier);
            p1.start();
            barrier.await();
            List<List<Production>> levelProductions = new LinkedList<>();
            levelProductions.add(new LinkedList<>());
            levelProductions.get(0).add(p1);
            PrettyPrint.printTreeStructure(p1.m_vertex);
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
            PrettyPrint.printTreeStructure(p1.m_vertex);
            levelProductions.add(new LinkedList<>());
            barrier = new CyclicBarrier((int)Math.pow(2,(levels-1)) + 1);
            for(int j=0;j<levelProductions.get(levels-2).size();j++) {
                P3 p_left = new P3(levelProductions.get(levels-2).get(j).m_vertex.m_left, barrier);
                P3 p_right = new P3(levelProductions.get(levels-2).get(j).m_vertex.m_right, barrier);
                levelProductions.get(levels-1).add(p_left);
                levelProductions.get(levels-1).add(p_right);
                p_left.start();
                p_right.start();
                System.out.println(levelProductions.get(levels-1).size());
                System.out.println((int)Math.pow(2,(levels-1)) + 1);
            }
            barrier.await();

            barrier = new CyclicBarrier(levelProductions.get(levels-1).size()+1);
            A1 a1 = new A1(levelProductions.get(levels-1).get(0).m_vertex,barrier);
            a1.start();
            AN an = new AN(levelProductions.get(levels-1).get(levelProductions.get(levels-1).size()-1).m_vertex,barrier);
            an.endValue=1.0/Math.pow(2,levels-1);
            an.start();

            for(int i=1;i<levelProductions.get(levels-1).size()-1;i++)
            {
                A a = new A(levelProductions.get(levels-1).get(i).m_vertex,barrier);
                a.start();
            }
            barrier.await();
            for(int i=0;i<levelProductions.get(levels-1).size();i++) {
                System.out.println( );
                System.out.println(i);
                System.out.println(levelProductions.get(levels-1).get(i).m_vertex);
                System.out.println( );
              }
            an.m_vertex.m_b[2]=Math.pow(2,levels-1);
            for(int i=levels-2;i>0;i--)
            {
                barrier = new CyclicBarrier(levelProductions.get(i).size()+1);
                for(int j=0;j<levelProductions.get(i).size();j++)
                {
                    A2 a2 = new A2(levelProductions.get(i).get(j).m_vertex,barrier);
                    a2.start();
                }
                barrier.await();
                barrier = new CyclicBarrier(levelProductions.get(i).size()+1);
                for(int j=0;j<levelProductions.get(i).size();j++)
                {
                    E2 e2 = new E2(levelProductions.get(i).get(j).m_vertex,barrier);
                    e2.start();
                }
                barrier.await();

            }
            barrier = new CyclicBarrier(2);
            Aroot aroot = new Aroot(p1.m_vertex,barrier);
            aroot.start();
            barrier.await();
            System.out.println(p1.m_vertex);
            barrier = new CyclicBarrier(2);
            Eroot eroot = new Eroot(p1.m_vertex,barrier);
            eroot.start();
            barrier.await();
            System.out.println(p1.m_vertex);
            for(int i=0;i<levels-2;i++)
            {
                barrier = new CyclicBarrier(levelProductions.get(i).size()+1);
                for(int j=0;j<levelProductions.get(i).size();j++)
                {
                    BS bs = new BS(levelProductions.get(i).get(j).m_vertex,barrier);
                    bs.start();
                }
                barrier.await();
            }
            barrier = new CyclicBarrier(levelProductions.get(levels-2).size()+1);
            for(int i=0;i<levelProductions.get(levels-2).size();i++)
            {
                BSA bsa = new BSA(levelProductions.get(levels-2).get(i).m_vertex,barrier);
                bsa.start();

            }
            barrier.await();


//            barrier = new
//            A1 a1 = new A1(levelProductions.get(levels).get(0).m_vertex,barrier);
//            AN an = new AN(levelProductions.get(levels).get(2^(levels-1)).m_vertex,barrier);


            return S;
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
