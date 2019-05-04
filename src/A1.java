import java.util.concurrent.CyclicBarrier;

class A1 extends Production {

    A1(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }

    Vertex apply(Vertex T)
    {

        T.m_a[1][1]=T.m_a[2][2]=1;
        T.m_a[2][1]=-1;
        return T;
    }
}