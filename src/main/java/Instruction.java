public class Instruction {

    private int operationCode, type, rc, ra, rb, inm;

    public Instruction(int opCode, int type, int rc, int ra, int rb, int inm){
        this.operationCode = opCode;
        this.type = type;
        this.rc = rc;
        this.ra = ra;
        this.rb = rb;
        this.inm = inm;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getType() {
        return type;
    }

    public int getRc() {
        return rc;
    }

    public int getRa() {
        return ra;
    }

    public int getRb() {
        return rb;
    }

    public int getInm() {
        return inm;
    }
}
