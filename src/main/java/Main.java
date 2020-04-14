public class Main {
	final static int NUM_REG = 10;
	final static int NUM_DAT = 10;
	final static String FILE_NAME = "instructions.txt";
	final static int QUEUE_LENGTH = 6;
	final static int WINDOW_SIZE = 2;
	final static int ROB_LENGTH = 6;
	
	public static void main(String[] args) {
		Memory.initializeRegisters(NUM_REG);
		Memory.initializeDataMem(NUM_DAT);
		int numOfInstructions = Memory.initializeInstructionMem(FILE_NAME); //TODO: Hacer que la funci�n devuelva el n�mero de instrucciones
		Memory.initializeInstructionsQueue(QUEUE_LENGTH); //TODO: Crear funci�n en la clase Memory
		
		//Inicializaci�n de las Unidades Funcionales
		FunctionalUnit[] fu = new FunctionalUnit[4];
		fu[0] = new FunctionalUnit().addFU();
		fu[1] = new FunctionalUnit().subFU();
		fu[2] = new FunctionalUnit().chargeFU();
		fu[3] = new FunctionalUnit().multFU();
		
		//Inicializaci�n de las ventanas de instrucci�n
		InstructionWindow[] iw = new InstructionWindow[WINDOW_SIZE];
		for (int i=0; i<WINDOW_SIZE; i++) {
			iw[i] =  new InstructionWindow();
		}
		
		//Inicializaci�n del ROB
		ROB[] rob = new ROB[ROB_LENGTH];
		for(int i=0; i<ROB_LENGTH; i++) {
			rob[i] = new ROB();
		}
		
		int inst_cola_inst = 0, inst_vi = 0, inst_rob = 0; // N�mero actual de instrucciones en su correspondiente estructura de datos
		int instructionQueueFirst = -1; // Posici�n donde empiezan las instrucciones de la cola de instrucciones. -1 = Cola vac�a
		int instructionQueueLast = 0; // SIGUIENTE posici�n a la �ltima instrucci�n de la cola de instrucciones. Si First == Last: La cola est� llena.
		
		int instructionRobFirst = -1; // Posici�n donde empiezan las instrucciones del ROB. -1 = Cola vac�a
		int instructionRobLast = 0; // SIGUIENTE posici�n a la �ltima instrucci�n del ROB. Si First == Last: La cola est� llena.
		
		int programCounter = 0; //TODO: Es el contador del programa. Pero creo que conforme lo ibamos a hacer nosotros no hace falta
	}
	
}
