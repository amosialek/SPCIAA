import java.util.concurrent.CyclicBarrier;

class Eroot extends Production {
    Eroot(Vertex Vert, CyclicBarrier Count) {
        super(Vert, Count);
    }

    Vertex apply(Vertex T) {
        // Solve the system
        // ...
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


        divider = T.m_a[1][1];
        for(int i=1;i<3;i++)
            T.m_a[1][i] /= divider;
        T.m_b[1]/=divider;
        multiplier = T.m_a[2][1];
        for(int i=1;i<3;i++)
            T.m_a[2][i] -= multiplier*T.m_a[1][i];
        T.m_b[2] -= multiplier*T.m_b[1];

        T.m_x[2]=T.m_b[2]/T.m_a[2][2];
        T.m_x[1]=(T.m_b[1] - T.m_a[1][2]*T.m_x[2])/T.m_a[1][1];
        T.m_x[0]=(T.m_b[0] - T.m_a[0][2]*T.m_x[2] - T.m_a[0][1]*T.m_x[1])/T.m_a[0][0];

        // Undo the swap made by A2 !!!
        double x0 = T.m_x[1];
        T.m_x[1] = T.m_x[0];
        T.m_x[0] = x0;

        return T;
    }
}