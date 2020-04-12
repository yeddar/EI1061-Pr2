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

    public int getValidLine() {
        return validLine;
    }

    public void setValidLine(int validLine) {
        this.validLine = validLine;
    }

    public int getDestReg() {
        return destReg;
    }

    public void setDestReg(int destReg) {
        this.destReg = destReg;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getVaildRes() {
        return vaildRes;
    }

    public void setVaildRes(int vaildRes) {
        this.vaildRes = vaildRes;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
