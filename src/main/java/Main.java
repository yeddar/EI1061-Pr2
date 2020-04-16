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
		int numOfInstructions = Memory.initializeInstructionMem(FILE_NAME); //TODO: Hacer que la función devuelva el número de instrucciones
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
		
		/*TODO: Deberóan ser globales para que las funciones puedan modificarlas ...*/
		int inst_instructionQueue = 0, inst_instructionWindow = 0, inst_rob = 0; // Número actual de instrucciones en su correspondiente estructura de datos
		int instructionQueueFirst = -1; // Posición donde empiezan las instrucciones de la cola de instrucciones. -1 = Cola vacía
		int instructionQueueLast = 0; // SIGUIENTE posición a la última instrucción de la cola de instrucciones. Si First == Last: La cola está llena.
		
		int instructionRobFirst = -1; // Posición donde empiezan las instrucciones del ROB. -1 = Cola vacía
		int instructionRobLast = 0; // SIGUIENTE posición a la última instrucción del ROB. Si First == Last: La cola está llena.
		/*TODO: ... hasta aquí. Además se deberían de quitar de las funciones del while de abajo.*/
		
		int programCounter = 0; //TODO: Es el contador del programa. Pero creo que conforme lo ibamos a hacer nosotros no hace falta
		
		while ((inst_rob > 0) || (numOfInstructions > 0)) { // un ciclo de simulación ejecuta las 5 etapas.
		    // WB, RX, ISS, ID, IF
		    etapa_IF( programCounter, inst_instructionQueue, instructionQueueLast);
		    etapa_ID(instructionWindow, rob, inst_instructionWindow, instructionQueueFirst, instructionRobFirst);
		    etapa_ISS(functionUnits, rob, inst_instructionWindow);
		    etapa_EX(functionUnits, rob);
		    etapa_WB( numOfInstructions, rob, instructionWindow, instructionRobLast);
		    //Mostrar el contenido de las distintas estructuras para ver como evoluciona la simulación
		    show_instructionQueue();
		    show_instructionWindow(instructionWindow);
		    show_ROB(rob);
		    show_DataRegisters();
		  }
	}
	
}
