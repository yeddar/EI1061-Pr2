public class FunctionalUnit {
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
}
