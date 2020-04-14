public class FunctionalUnit {
	int ADD_OP = 0, SUB_OP = 1, CHARGE_OP = 2, MULT_OP = 3;
    int inUse;
    int cycleCount;
    int RobLine;
    int op;
    int res;
    int opA;
    int opB;

    public FunctionalUnit(int inUse, int cycleCount, int robLine, int op, int res, int opA, int opB) {
        this.inUse = inUse;
        this.cycleCount = cycleCount;
        RobLine = robLine;
        this.op = op;
        this.res = res;
        this.opA = opA;
        this.opB = opB;
    }
	
	public FunctionalUnit(){}
	
	public FunctionalUnit addFU(){
		this.op = ADD_OP;
		return this;
	}
	
	public FunctionalUnit subFU(){
		this.op = SUB_OP;
		return this;
	}
	
	public FunctionalUnit chargeFU(){
		this.op = CHARGE_OP;
		return this;
	}
	
	public FunctionalUnit multFU(){
		this.op = MULT_OP;
		return this;
	}
}
