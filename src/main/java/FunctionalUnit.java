public class FunctionalUnit {
	int ADD_OP = 0, SUB_OP = 1, CHARGE_OP = 2, STORE_OP = 3, MULT_OP = 4;
    int inUse;
    int cycleCount;
    int robLine;
    int op;
    int res;
    int opA;
    int opB;
    int inm;
	int maxCycles;

    public FunctionalUnit(int inUse, int cycleCount, int robLine, int op, int res, int opA, int opB) {
        this.inUse = inUse;
        this.cycleCount = cycleCount;
        this.robLine = robLine;
        this.op = op;
        this.res = res;
        this.opA = opA;
        this.opB = opB;
    }
	
	public FunctionalUnit(){
	}

	// Creo que todos estos métodos se pueden sustituir por el constuctor de arriba
	public FunctionalUnit addFU(){
		this.op = ADD_OP;
		this.maxCycles = 1;
		return this;
	}
	
	public FunctionalUnit subFU(){
		this.op = SUB_OP;
		this.maxCycles = 1;
		return this;
	}
	
	public FunctionalUnit chargeFU(){
		this.op = CHARGE_OP;
		this.maxCycles = 2;
		return this;
	}
	
	public FunctionalUnit storeFU(){
		this.op = STORE_OP;
		this.maxCycles = 2;
		return this;
	}
	
	public FunctionalUnit multFU(){
		this.op = MULT_OP;
		this.maxCycles = 5;
		return this;
	}
	
	public FunctionalUnit init(int robLine, int opA, int opB, int inm) {
		this.inUse = 1;
        this.cycleCount = 0;
        this.robLine = robLine;
        this.res = 0;
        this.opA = opA;
        this.opB = opB;
        this.inm = inm;
		return this;
	}

	public boolean execute() { // TODO: Cambiar diecciones registros por valor
		this.cycleCount++;
		if(cycleCount >= maxCycles) {
			this.inUse = 0;
			if (this.op == ADD_OP) {
				this.res = this.opA+ this.opB + this.inm;
				return true;
			}
			if (this.op == SUB_OP) {
				this.res = this.opA - this.opB - this.inm;
				return true;
			}
			if (this.op == CHARGE_OP) {
				this.res = Memory.dataMem[this.opA + this.inm];
				return true;
			}
			if (this.op == STORE_OP) {
				Memory.dataMem[this.opA + this.inm] = this.opB;
				return true;
			}
			if (this.op == MULT_OP) {
				this.res = this.opA * this.opB;
				return true;
			}
			throw new RuntimeException("Operacion no encontrada");
		}
		return false;
	}
}
