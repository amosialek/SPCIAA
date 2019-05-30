import java.util.concurrent.CyclicBarrier;

public class FlowingWave implements Problem {
    double epsilon = 0.01;//-0.001;
    double flowForce = 1;//1.0;
    double b(double t){
        return Math.PI*2*Math.cos(Math.PI*2*t);
//        return 0;
    }
    @Override
    public Production makeA1(Vertex v, double h, double dt, double t, CyclicBarrier Barrier) {

        return new ABase(v, h, dt, t, Barrier) {
            @Override
            Vertex apply(Vertex v) {
                double a = dt / (h * h);
                v.m_a[1][1]=1;
                v.m_a[2][1]=-epsilon/(h*h)-flowForce/2*h;
                v.m_a[2][2]=0.5*(1/dt+2*epsilon/(h*h));
                v.m_b[1] = 0;
                v.m_b[2] = 0.5 * (v.m_x[2]/dt+b(t));
                return v;
            }
        };
    }

    @Override
    public Production makeA(Vertex v, double h, double dt, double t, CyclicBarrier Barrier) {
        return new ABase(v, h, dt, t, Barrier) {
            @Override
            Vertex apply(Vertex v) {
                double a = dt / (h * h);
                v.m_a[1][1]=0.5*(1/dt+2*epsilon/(h*h));
                v.m_a[1][2]=-epsilon/(h*h)+flowForce/2*h;
                v.m_a[2][1]=-epsilon/(h*h)-flowForce/2*h;
                v.m_a[2][2]=0.5*(1/dt+2*epsilon/(h*h));
                v.m_b[1] = 0.5 * (v.m_x[1]/dt+b(t));
                v.m_b[2] = 0.5 * (v.m_x[2]/dt+b(t));
                return v;
            }
        };
    }

    @Override
    public Production makeAN(Vertex v, double h, double dt, double t, CyclicBarrier Barrier) {
        return new ABase(v, h, dt, t, Barrier) {
            @Override
            Vertex apply(Vertex v) {
                double a = dt / (h * h);
                v.m_a[1][1]=0.5*(1/dt+2*epsilon/(h*h));
                v.m_a[1][2]=-epsilon/(h*h)+flowForce/2*h;
                v.m_a[2][2]=1;
                v.m_b[1] = 0.5 * (v.m_x[1]/dt+b(t));
                v.m_b[2] =0;
                return v;
            }
        };
    }
}
