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
    String opString[] = {"�op0?", "lw", "sw", "add", "sub", "addi", "subi", "mult"};


    public InstructionWindow() {
        super();
        rset();
	}

    @Override
    public String toString() {
        return "[" +
                "validLine=" + validLine +
                ", op= " + ((op>=0)?opString[op]:"none") +
                ", type=" + type +
                ", opA=" + opA +
                ", vOpA=" + vOpA +
                ", opB=" + opB +
                ", vOpB=" + vOpB +
                ", robLine=" + robLine +
                ", inm=" + inm +
                ']';
    }
    
    public void rset() {
    	validLine = 0;
        op = -1;
        type = -1;
        opA = 0;
        vOpA = -1;
        opB = 0;
        vOpB = -1;
        robLine = -1;
        inm = 0;
    }
}
