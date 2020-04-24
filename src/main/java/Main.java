public class Main {
	// Estructuras de almacenamiento
	final static int NUM_REG = 16;
	final static int NUM_DAT = 32;
	final static int NUM_INS = 32;
	final static String FILE_NAME = "instructions.txt";
	final static int QUEUE_MAX_LENGTH = 6;
	final static int WINDOW_SIZE = 2;
	final static int ROB_LENGTH = 6;

	// Códigos para las UF
	public static final int TOTAL_UF = 4, UF_SUM = 0, UF_RES = 1, UF_CA = 2, UF_MULT = 3;

	// Número máximo de intrucciones que se pueden procesar en cada etapa por ciclo
	public static final int MAX_INST = 2;

	// Ciclos de ejecución de UF
	public static final int
			TOT_CICLOS_CA = 2, // Carga y almacentamiento
			TOT_CICLOS_SR = 1, // Suma y resta
			TOT_CICLOS_MULT = 5; //Mult

	// Estapas en ROB
	public static final int
			ID = 0, ISS = 1, EX = 2, F0 = 3, F1 = 4;


	static int inst_instructionQueue = 0, inst_instructionWindow = 0, inst_rob = 0; // Número actual de instrucciones en su correspondiente estructura de datos
	static int instructionQueueFirst = -1; // Posición donde empiezan las instrucciones de la cola de instrucciones. -1 = Cola vacía
	static int instructionQueueLast = 0; // SIGUIENTE posición a la última instrucción de la cola de instrucciones. Si First == Last: La cola está llena.
	
	static int instructionRobFirst = -1; // Posición donde empiezan las instrucciones del ROB. -1 = Cola vacía
	static int instructionRobLast = 0; // SIGUIENTE posición a la última instrucción del ROB. Si First == Last: La cola está llena.
	
	static int numOfInstructions = -1;

	// Related instructions queue


	static int programCounter = 0; //TODO: Es el contador del programa. Pero creo que conforme lo ibamos a hacer nosotros no hace falta

	public static void main(String[] args) {
		Memory.initializeRegisters(NUM_REG);
		Memory.initializeDataMem(NUM_DAT);

		// Initialization of instruction memory
		Memory.instructionMem = new Instruction[NUM_INS];
		numOfInstructions = Memory.initializeInstructionMem(NUM_INS, FILE_NAME); //TODO: Hacer que la función devuelva el número de instrucciones

		// Initialization of instruction queue
		Memory.initializeInstructionsQueue(); //TODO: Crear función en la clase Memory


		//Inicialización de las Unidades Funcionales
		FunctionalUnit[] functionUnits = new FunctionalUnit[TOTAL_UF];
		functionUnits[UF_SUM] = new FunctionalUnit(UF_SUM);
		functionUnits[UF_RES] = new FunctionalUnit(UF_RES);
		functionUnits[UF_CA] = new FunctionalUnit(UF_CA);
		functionUnits[UF_MULT] = new FunctionalUnit(UF_MULT);
		
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
		

		
		while ((inst_rob > 0) || (numOfInstructions > 0)) { // un ciclo de simulación ejecuta las 5 etapas.
		    // WB, RX, ISS, ID, IF
			//TODO: Creo que habría que cambiar el orden de ejecución de las etapas para que no se sobreescriban los registros
		    etapa_IF(); // etapa_IF();
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

	private static void etapa_IF() {
		// TODO Auto-generated method stub
		// Check size of the queue
		int i = 0;
		while ( ( Memory.instructionQueue.size() < QUEUE_MAX_LENGTH ) && (i < MAX_INST)  ) {
			Memory.instructionQueue.add(Memory.instructionMem[programCounter++]);
			i++;

		}

	}

	private static void etapa_ID(InstructionWindow[] iw, ROB[] rob) {
		// TODO Hay que crear una función para la búsqueda de un operando en ROB y evitar la repetición de código.
		// Get instructions from instructions queue.

		if ( (inst_instructionWindow == 0) && (Memory.instructionQueue.size() > 0) ) {
			int rPointer, qPointer = 0;
			while ( (Memory.instructionQueue.size() > 0) && qPointer < MAX_INST) {
				Instruction ins = Memory.instructionQueue.poll();
				// Antes de cargar instrucción, buscar en banco de registros validez y ROB
				// Si registro tiene contenido válido
				int id_ra = ins.getRa(); // Register identifier
				int id_rb = ins.getRb();
				if (Memory.registers[id_ra].validData == 1) {
					iw[qPointer].opA = Memory.registers[id_ra].data;
					iw[qPointer].vOpA = 1;
				} else {
					// Búsqueda en ROB operando A
					rPointer = inst_rob - 1;
					if (instructionRobFirst != instructionRobLast) { // is not empty
						while (rob[rPointer].validLine == 1) {
							if (rob[rPointer].destReg == id_ra) { // Dependency
								if (rob[rPointer].vaildRes == 1) {
									iw[qPointer].opA = rob[rPointer].res;
									iw[qPointer].vOpA = 1;
								} else { // Registro a la espera de ser actualizado
									// Guardamos el índice de línea ROB que contiene operando
									iw[qPointer].opA = rPointer;
									iw[qPointer].vOpA = 0;
								}

							}
						}
					}
				}
				// Operando B. Tener en cuenta que puede ser dato inmediato
				if (ins.getType() == Memory.typeI) { //Type I instruction
					iw[qPointer].opB = ins.getInm();
					iw[qPointer].vOpB = 1;
				} else if (Memory.registers[id_rb].validData == 1) {
					iw[qPointer].opB = ins.getRb();
					iw[qPointer].vOpB = 1;
				} else { // Buscar en ROB
					rPointer = inst_rob - 1;
					if (instructionRobFirst != instructionRobLast) { // is not empty
						while (rob[rPointer].validLine == 1) {
							if (rob[rPointer].destReg == id_rb) { // Dependency
								if (rob[rPointer].vaildRes == 1) {
									iw[qPointer].opB = rob[rPointer].res;
									iw[qPointer].vOpB = 1;
								} else { // Registro a la espera de ser actualizado
									// Guardamos el índice de línea ROB que contiene operando
									iw[qPointer].opB = rPointer;
									iw[qPointer].vOpB = 0;
								}

							}
						}
					}

				}
				qPointer++;
			} // Fin while
		}

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
