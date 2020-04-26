public class InstructionWindow {
    int inm;
	int validLine;
    int op;
    int type;
    int opA;
    int vOpA;
    int opB;
    int vOpB;
    int robLine;

    public InstructionWindow(int validLine, int op, int type, int opA, int vOpA, int opB, int vOpB, int inm, int robLine) {
        this.validLine = validLine;
        this.op = op;
        this.type = type;
        this.opA = opA;
        this.vOpA = vOpA;
        this.opB = opB;
        this.vOpB = vOpB;
        this.robLine = robLine;
        this.inm = inm;
    }

    public InstructionWindow() {
    	this.validLine = -1;
        this.op = -1;
        this.type = -1;
        this.opA = -1;
        this.vOpA = -1;
        this.opB = -1;
        this.vOpB = -1;
        this.robLine = -1;
        this.inm = 0;
	}

}
