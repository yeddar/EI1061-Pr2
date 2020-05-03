import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.NClob;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Memory {


    // Tipo de instrucciones
    public static final int typeR = 0, typeI = 1;

    // Juego de instrucciones
    public static final int nop = -1, trap = 0, lw = 1, sw = 2, add = 3, sub = 4, addi = 5, subi = 6, mult = 7;

    public static Register [] registers;
    public static Instruction[] instructionMem;
    public static int[] dataMem;
    public static Queue<Instruction> instructionQueue;
    public static Queue<ROB> rob;


    public static void initializeDataMem(int num_dat){
    	dataMem = new int[num_dat];
        for(int i=1; i<=num_dat; i++)
            dataMem[i-1] = i;
    }

    public static void initializeRegisters(int num_reg) {
    	registers = new Register[num_reg];
        for(int i = 1; i <= num_reg; i++) {
            registers[i - 1] = new Register(i, 1);
        }

    }

    public static int initializeInstructionMem(int size, String fileName) {
        int numOfInstructions = 0;
    	try {
            numOfInstructions = chargeIntructions(fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return numOfInstructions;
    }

	public static void initializeInstructionsQueue() {
		instructionQueue = new LinkedList<>();
	}


    
    //TODO: Modificar la función para las instrucciones de esta práctica.
    private static int chargeIntructions(String fileName) throws IOException {
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
                    instructionMem[nInstruction++] = new Instruction(add, typeR, rc, ra, rb, 0);
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
                    instructionMem[nInstruction++] = new Instruction(sub, typeR, rc, ra, rb, 0);
                    break;
                case "subi":
                    //System.out.println("Es subi");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    inm = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(subi, typeI, rc, ra, rb, inm);
                    break;
                case "mult":
                    //System.out.println("Es mult");
                    rc = Integer.parseInt(instrucDec[1].substring(1,instrucDec[1].length()-1));
                    ra = Integer.parseInt(instrucDec[2].substring(1,instrucDec[2].length()-1));
                    rb = Integer.parseInt(instrucDec[3].substring(1));
                    instructionMem[nInstruction++] = new Instruction(mult, typeR, rc, ra, rb, 0);
                    break;
                default:
                    throw new RuntimeException("Se esta usando otra operacion no valida. Revisar los NOP");
            }
        }
        return nInstruction;
    }




}
