public class ROB {
    int validLine;
    int destReg;
    int res;
    int vaildRes;
    int stage;


    public ROB() {
        this.validLine = 0;
        this.destReg = -1;
        this.res = 0;
        this.vaildRes = 0;
        this.stage = 0;
	}

    public void set(int validLine, int destReg, int res, int vaildRes, int stage) {
        this.validLine = validLine;
        this.destReg = destReg;
        this.res = res;
        this.vaildRes = vaildRes;
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "[" +
                "validLine=" + validLine +
                ", destReg=" + destReg +
                ", res=" + res +
                ", vaildRes=" + vaildRes +
                ", stage=" + stage +
                ']';
    }
}
