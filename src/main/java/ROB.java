public class ROB {
    int validLine;
    int destReg;
    int res;
    int vaildRes;
    int stage;

    public ROB(int validLine, int destReg, int res, int vaildRes, int stage) {
        this.validLine = validLine;
        this.destReg = destReg;
        this.res = res;
        this.vaildRes = vaildRes;
        this.stage = stage;
    }

    public ROB() {
        this.validLine = -1;
        this.destReg = -1;
        this.res = -1;
        this.vaildRes = -1;
        this.stage = -1;
	}


}
