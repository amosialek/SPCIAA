import java.util.concurrent.CyclicBarrier;

class E2 extends Production {
    E2(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T) {
        double divider = T.m_a[0][0];
        for(int i=0;i<3;i++)
            T.m_a[0][i] /= divider;
        T.m_b[0]/=divider;
        double multiplier = T.m_a[1][0]/T.m_a[0][0];
        for(int i=0;i<3;i++)
            T.m_a[1][i] -= multiplier*T.m_a[0][i];
        T.m_b[1] -= multiplier*T.m_b[0];
        multiplier = T.m_a[2][0]/T.m_a[0][0];
        for(int i=0;i<3;i++)
            T.m_a[2][i] -= multiplier*T.m_a[0][i];
        T.m_b[2] -= multiplier*T.m_b[0];
        return T;
    }
}