public class Memory {

    // Capacidad de las estructuras de almacenamiento
    public static final int NUM_REG = 16, NUM_DAT = 32, NUM_INS = 32, MAX_INS_COLA_INST = 6,
            MAX_INS_VI = 2, MAX_INS_ROB = 6;


    // Banco de registros
    // 0 int contenido
    // 1 int contenidoValido
    public static int[][] registers = new int[NUM_REG][2];
    public static int [][] intructionMem;
    public static int [][] dataMem;


    private static Instruction[] instrucciones;


}
