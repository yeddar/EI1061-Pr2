public class InstructionWindow {
    int validLine;
    int op;
    int type;
    int opA;
    int vOpA;
    int opB;
    int vOpB;
    int robLine;

    public InstructionWindow(int validLine, int op, int type, int opA, int vOpA, int opB, int vOpB, int robLine) {
        this.validLine = validLine;
        this.op = op;
        this.type = type;
        this.opA = opA;
        this.vOpA = vOpA;
        this.opB = opB;
        this.vOpB = vOpB;
        this.robLine = robLine;
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
	}

	public int getValidLine() {
        return validLine;
    }

    public void setValidLine(int validLine) {
        this.validLine = validLine;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOpA() {
        return opA;
    }

    public void setOpA(int opA) {
        this.opA = opA;
    }

    public int getvOpA() {
        return vOpA;
    }

    public void setvOpA(int vOpA) {
        this.vOpA = vOpA;
    }

    public int getOpB() {
        return opB;
    }

    public void setOpB(int opB) {
        this.opB = opB;
    }

    public int getvOpB() {
        return vOpB;
    }

    public void setvOpB(int vOpB) {
        this.vOpB = vOpB;
    }

    public int getRobLine() {
        return robLine;
    }

    public void setRobLine(int robLine) {
        this.robLine = robLine;
    }
}
