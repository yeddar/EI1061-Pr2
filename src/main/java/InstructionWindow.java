public class InstructionWindow {

	int validLine;
    int op;
    int type;
    int opA;
    int vOpA;
    int opB;
    int vOpB;
    int robLine;
    int inm;


    public InstructionWindow() {
        super();
	}

    @Override
    public String toString() {
        return "InstructionWindow{" +
                "validLine=" + validLine +
                ", op=" + op +
                ", type=" + type +
                ", opA=" + opA +
                ", vOpA=" + vOpA +
                ", opB=" + opB +
                ", vOpB=" + vOpB +
                ", robLine=" + robLine +
                ", inm=" + inm +
                '}';
    }
}
