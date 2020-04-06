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

    //
    // Juego de instrucciones
    public static final int
            nop = -1,
            trap = 0,
            lw = 1,
            sw = 2,
            add = 3,
            sub = 4,
            addi = 5,
            subbi = 6,
            mult = 7;

    // Tipo de instrucciones
    public static final int tipoR = 0, tipoI = 1;


    // Códigos para las UF
    public static final int
            TOTAL_UF = 4,
            UF_SR_1 = 0,
            UF_SR_2 = 1,
            UF_CA = 2,
            UF_MULT = 3;


    // Ciclos de ejecución de UF
    public static final int
            TOT_CICLOS_CA = 2, // Carga y almacentamiento
            TOT_CICLOS_SR = 1, // Suma y resta
            TOT_CICLOS_MULT = 5; //Mult

    // Estapas en ROB
    public static final int
            ID = 0,
            ISS = 1,
            EX = 2,
            F0 = 3,
            F1 = 4;




}
