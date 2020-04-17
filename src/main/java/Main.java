public class Main {
	final static int NUM_REG = 10;
	final static int NUM_DAT = 10;
	final static String FILE_NAME = "instructions.txt";
	final static int QUEUE_LENGTH = 6;
	final static int WINDOW_SIZE = 2;
	final static int ROB_LENGTH = 6;

	static int inst_instructionQueue = 0, inst_instructionWindow = 0, inst_rob = 0; // Número actual de instrucciones en su correspondiente estructura de datos
	static int instructionQueueFirst = -1; // Posición donde empiezan las instrucciones de la cola de instrucciones. -1 = Cola vacía
	static int instructionQueueLast = 0; // SIGUIENTE posición a la última instrucción de la cola de instrucciones. Si First == Last: La cola está llena.
	
	static int instructionRobFirst = -1; // Posición donde empiezan las instrucciones del ROB. -1 = Cola vacía
	static int instructionRobLast = 0; // SIGUIENTE posición a la última instrucción del ROB. Si First == Last: La cola está llena.
	
	static int numOfInstructions = -1;
	
	public static void main(String[] args) {
		Memory.initializeRegisters(NUM_REG);
		Memory.initializeDataMem(NUM_DAT);
		numOfInstructions = Memory.initializeInstructionMem(FILE_NAME); //TODO: Hacer que la función devuelva el número de instrucciones
		Memory.initializeInstructionsQueue(QUEUE_LENGTH); //TODO: Crear función en la clase Memory
		
		//Inicialización de las Unidades Funcionales
		FunctionalUnit[] functionUnits = new FunctionalUnit[4];
		functionUnits[0] = new FunctionalUnit().addFU();
		functionUnits[1] = new FunctionalUnit().subFU();
		functionUnits[2] = new FunctionalUnit().chargeFU();
		functionUnits[3] = new FunctionalUnit().multFU();
		
		//Inicialización de las ventanas de instrucción
		InstructionWindow[] instructionWindow = new InstructionWindow[WINDOW_SIZE];
		for (int i=0; i<WINDOW_SIZE; i++) {
			instructionWindow[i] =  new InstructionWindow();
		}
		
		//Inicialización del ROB
		ROB[] rob = new ROB[ROB_LENGTH];
		for(int i=0; i<ROB_LENGTH; i++) {
			rob[i] = new ROB();
		}
		
		int programCounter = 0; //TODO: Es el contador del programa. Pero creo que conforme lo ibamos a hacer nosotros no hace falta
		
		while ((inst_rob > 0) || (numOfInstructions > 0)) { // un ciclo de simulación ejecuta las 5 etapas.
		    // WB, RX, ISS, ID, IF
			//TODO: Creo que habría que cambiar el orden de ejecución de las etapas para que no se sobreescriban los registros
		    etapa_IF(programCounter); // etapa_IF();
		    etapa_ID(instructionWindow, rob);
		    etapa_ISS(functionUnits, rob);
		    etapa_EX(functionUnits, rob);
		    etapa_WB(rob, instructionWindow);
		    //Mostrar el contenido de las distintas estructuras para ver como evoluciona la simulación
		    show_instructionQueue();
		    show_instructionWindow(instructionWindow);
		    show_ROB(rob);
		    show_DataRegisters();
		  }
	}

	private static void etapa_IF(int programCounter) {
		// TODO Auto-generated method stub
		
	}

	private static void etapa_ID(InstructionWindow[] instructionWindow, ROB[] rob) {
		// TODO Auto-generated method stub
		
	}

	private static void etapa_ISS(FunctionalUnit[] functionUnits, ROB[] rob) {
		// TODO Auto-generated method stub
		
	}

	private static void etapa_EX(FunctionalUnit[] functionUnits, ROB[] rob) {
		// TODO Auto-generated method stub
		
	}

	private static void etapa_WB(ROB[] rob, InstructionWindow[] instructionWindow) {
		// TODO Auto-generated method stub
		
	}
	
	
	//SHOWERS (Mostradores, no duchas xDD)

	private static void show_instructionQueue() {
		// TODO Auto-generated method stub
		
	}

	private static void show_instructionWindow(InstructionWindow[] instructionWindow) {
		// TODO Auto-generated method stub
		
	}

	private static void show_ROB(ROB[] rob) {
		// TODO Auto-generated method stub
		
	}

	private static void show_DataRegisters() {
		// TODO Auto-generated method stub
		
	}
	
}
