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
    String opString[] = {"¿op0?", "lw", "sw", "add", "sub", "addi", "subi", "mult"};


    public InstructionWindow() {
        super();
        reset();
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
    
    public void reset() {
    	validLine = 1;
        op = -1;
        type = -1;
        opA = -1;
        vOpA = 0;
        opB = -1;
        vOpB = 0;
        robLine = -1;
        inm = -1;
    }
}
