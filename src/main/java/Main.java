public class Main {
	final static int NUM_REG = 10;
	final static int NUM_DAT = 10;
	final static String FILE_NAME = "instructions.txt";
	final static int QUEUE_LENGTH = 10;
	
	public static void main(String[] args) {
		Memory.initializeRegisters(NUM_REG);
		Memory.initializeDataMem(NUM_DAT);
		Memory.initializeInstructionMem(FILE_NAME);
		Memory.initializeInstructionsQueue(QUEUE_LENGTH); //TODO: Crear función en la clase Memory
		FunctionalUnit[] fu = new FunctionalUnit[4];
		fu[0] = new FunctionalUnit().addFU();
		fu[1] = new FunctionalUnit().subFU();
		fu[2] = new FunctionalUnit().chargeFU();
		fu[3] = new FunctionalUnit().multFU();
	}
	
}
