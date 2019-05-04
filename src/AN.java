import java.util.concurrent.CyclicBarrier;

class AN extends A {

    AN(Vertex Vert, CyclicBarrier Barrier) {
        super(Vert, Barrier);
    }
    public double endValue = 1/6.0;
    Vertex apply(Vertex T) {
        System.out.println("A");
        //fill T.m_a and T.m_b with your system here
        T.m_a[1][1]=T.m_a[2][2]=1;
        T.m_a[1][2]=T.m_a[2][1]=-1;
        T.m_b[2]=endValue;
        return T;
    }
}