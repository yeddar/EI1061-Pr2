import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Memory {

    // Capacidad de las estructuras de almacenamiento
    public static final int NUM_REG = 16, NUM_DAT = 32, NUM_INS = 32, MAX_INS_COLA_INST = 6,
            MAX_INS_VI = 2, MAX_INS_ROB = 6;

    // Tipo de instrucciones
    public static final int typeR = 0, typeI = 1;

    // Juego de instrucciones
    public static final int nop = -1, trap = 0, lw = 1, sw = 2, add = 3, sub = 4, addi = 5, subbi = 6, mult = 7;

    public static Register [] registers = new Register[NUM_REG];
    public static Instruction[] instructionMem = new Instruction[NUM_INS];
    public static int[] dataMem;
    public static Instruction[] instructionQueue = new Instruction[MAX_INS_COLA_INST];


    public static void initializeDataMem(int num_dat){
	dataMem ) new int[num_dat];
        for(int i=1; i<=num_dat; i++)
            dataMem[i-1] = i;
    }

    public static void initializeRegisters(int num_reg) {
        for(int i=1; i<=num_reg; i++) {
            registers[i - 1].data = i;
            registers[i - 1].validData = 1;
        }
    }

    public static void initializeInstructionMem(String fileName) {
        try {
            chargeIntructions(fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void chargeIntructions(String fileName) throws IOException {
        String instruction;
        String[] aux;
        int nInstruction = 0;
        FileReader f = new FileReader(fileName);
        BufferedReader b = new BufferedReader(f);
        // Default values for the registers
        int ra = -1, rb = -1, rc = -1, inm = -1;
        while ((instruction = b.readLine()) != null) {
            String[] instrucDec = instruction.split(" ");
            String opType = instrucDec[0];
            switch (opType) {
                case "lw":
                    //System.out.println("Es lw");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    aux = instrucDec[2].split("\\(");
                    inm = Integer.parseInt(aux[0]);
                    ra = Integer.parseInt(aux[1].substring(1,aux[1].length()-1));
                    instructionMem[nInstruction++] = new Instruction(lw, typeI, rc, ra, rb, inm);
                    break;
                case "sw":
                    //System.out.println("Es sw");
                    rb = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    aux = instrucDec[2].split("\\(");
                    inm = Integer.parseInt(aux[0]);
                    ra = Integer.parseInt(aux[1].substring(1,aux[1].length()-1));
                    instructionMem[nInstruction++] = new Instruction(sw, typeI, rc, ra, rb, inm);
                    break;
                case "add":
                    //System.out.println("Es add");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    rb = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(add, typeR, rc, ra, rb, inm);
                    break;
                case "addi":
                    //System.out.println("Es addi");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    inm = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(addi, typeI, rc, ra, rb, inm);
                    break;
                case "sub":
                    //System.out.println("Es sub");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    rb = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(sub, typeR, rc, ra, rb, inm);
                    break;
                case "subi":
                    //System.out.println("Es subi");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    inm = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(subbi, typeI, rc, ra, rb, inm);
                    break;
                case "mult":
                    //System.out.println("Es mult");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    rb = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(mult, typeR, rc, ra, rb, inm);
                    break;
                default:
                    instructionMem[nInstruction++] = new Instruction(nop, typeR, rc, ra, rb, inm);
                    //System.out.println("NOP");


            }
        }
        // Ciclos extra
        for(int i = 0; i < 4; i++)
            instructionMem[nInstruction++] = new Instruction(nop, typeR, rc, ra, rb, inm);


    }




}
