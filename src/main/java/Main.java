public class Main {
	// Estructuras de almacenamiento
	final static int NUM_REG = 16;
	final static int NUM_DAT = 32;
	final static int NUM_INS = 32;
	final static String FILE_NAME = "/Users/diegobenitez/IdeaProjects/EI1061/Practica2/src/main/java/instructions.txt";
	final static int QUEUE_MAX_LENGTH = 6;
	final static int WINDOW_SIZE = 2;
	final static int ROB_LENGTH = 6;

	// Códigos para las UF
	public static final int TOTAL_UF = 4, UF_SUM1 = 0, UF_SUM2 = 1, UF_CA = 2, UF_MULT = 3;

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

	// ROB indexes
	static int inst_rob = 0; // Num lineas válidas. TODO: Tengo mis dudas sobre si hace falta inst_rob
	static int firstIndexRob = -1;
	static int lastIndexRob = 0;

	static int inst_instructionWindow = 0; // Número actual de instrucciones en su correspondiente estructura de datos
	static int numOfInstructions = -1;

	static int programCounter = 0; //TODO: Es el contador del programa. Pero creo que conforme lo ibamos a hacer nosotros no hace falta

	public static void main(String[] args) {
		Memory.initializeRegisters(NUM_REG);
		Memory.initializeDataMem(NUM_DAT);

		// Initialization of instruction memory
		Memory.instructionMem = new Instruction[NUM_INS];
		numOfInstructions = Memory.initializeInstructionMem(NUM_INS, FILE_NAME); //TODO: Hacer que la función devuelva el número de instrucciones

		// Initialization of instruction queue
		Memory.initializeInstructionsQueue();


		//Inicialización de las Unidades Funcionales
		FunctionalUnit[] functionUnits = new FunctionalUnit[TOTAL_UF];
		functionUnits[UF_SUM1] = new FunctionalUnit();
		functionUnits[UF_SUM2] = new FunctionalUnit();
		functionUnits[UF_CA] = new FunctionalUnit();
		functionUnits[UF_MULT] = new FunctionalUnit().multFU();
		
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
		    etapa_ID(instructionWindow, rob, firstIndexRob);
		    etapa_ISS(instructionWindow, functionUnits, rob);
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
	
	// Busca operando en ROB.
	// Devuelve:
	// -1 si no se encuentra el operando
	// Puntero de linea si encuentra registro y línea es válida
	private static int busquedaROB(ROB [] rob, int robPointer, int operand) {
		for ( int i = 0; i < ROB_LENGTH; i++ ) {
			if ( (rob[robPointer].destReg == operand) && (rob[robPointer].validLine == 1) ) { // Dependency
				return robPointer;
			}
			robPointer = (robPointer + 1) % ROB_LENGTH;
		}
		return -1;
	}

	// Añade línea en ROB.
	// Devuelve:
	// -1 Si buffer lleno
	// 1 Si se ha añadido/modificado línea
	private static int addLineROB(ROB [] rob, int validLine, int destReg, int res, int validRes, int stage) {
		if (firstIndexRob == lastIndexRob) // Buffer lleno
			return -1;

		if (firstIndexRob == -1) { // Buffer vacío
			rob[0].set(validLine, destReg,res,validRes,stage);
			firstIndexRob = 0;
		} else {
			rob[lastIndexRob].set(validLine,destReg,res,validRes,stage);
		}
		lastIndexRob = (lastIndexRob + 1) % ROB_LENGTH;
		inst_rob++; // Actualiza el número de elementos en ROB
		return 1;
	}


	private static void etapa_ID(InstructionWindow[] iw, ROB[] rob, int robPointer) {
		// TODO Hay que crear una función para la búsqueda de un operando en ROB y evitar la repetición de código.
		// Get instructions from instructions queue.

		if ( inst_instructionWindow == 0 ) {
			int wPointer = 0;
			while ( (Memory.instructionQueue.size() > 0) && wPointer < MAX_INST) {
				Instruction ins = Memory.instructionQueue.poll();
				// Antes de cargar instrucción, buscar en banco de registros validez y ROB

				// Register identifiers
				int id_ra = ins.getRa();
				int id_rb = ins.getRb();
				int id_rc = ins.getRc();

				// Parte 1. Búsqueda operando A
				if (Memory.registers[id_ra].validData == 1) { // Si registro tiene contenido válido
					iw[wPointer].opA = Memory.registers[id_ra].data;
					iw[wPointer].vOpA = 1;
				} else { // Si no contenido válido
					// Búsqueda en ROB operando A
					int robLine = busquedaROB(rob, robPointer, id_ra);
					if (robLine != -1) { // Operando válido
						if (rob[robLine].vaildRes == 1) {
							iw[wPointer].opA = rob[robLine].res;
							iw[wPointer].vOpA = 1;
						} else { // Operando no válido. Guarda referencia línea ROB
							iw[wPointer].opA = robLine;
							iw[wPointer].vOpA = 0;
						}
					}
				}

				//Parte 2. Búsqueda operando B
				// Operando B. Tener en cuenta que puede ser dato inmediato
				if (ins.getType() == Memory.typeI) { //Type I instruction
					iw[wPointer].opB = ins.getInm();
					iw[wPointer].vOpB = 1;
				} else if (Memory.registers[id_rb].validData == 1) {
					iw[wPointer].opB = ins.getRb();
					iw[wPointer].vOpB = 1;
				} else { // Buscar en ROB
					// Búsqueda en ROB operando B
					int robLine = busquedaROB(rob, robPointer, id_rb);
					if (robLine != -1) { // Operando válido
						if (rob[robLine].vaildRes == 1) {
							iw[wPointer].opB = rob[robLine].res;
							iw[wPointer].vOpB = 1;
						} else {
							iw[wPointer].opB = robLine;
							iw[wPointer].vOpB = 0;
						}
					}
				}

				// Parte 3. Añadir intrucción en ROB
				// Add instruction into ROB
				addLineROB(rob, 1, id_rc, 0, 0, ID);
				wPointer++; // Incrementar puntero de ventana
			}// Fin while
			// Actualizar tamaño ventana intrucciones
			inst_instructionWindow = 2;
		} // fin if
	}


	// Diego
	private static void etapa_ISS(InstructionWindow[] iw, FunctionalUnit[] functionUnits, ROB[] rob) {
		// TODO Auto-generated method stub
		int i=0;
		boolean seguir = true;
		while (i<2 && seguir) {
			if(iw[i].validLine == 1) {
				if ((iw[i].op == Memory.add) || (iw[i].op == Memory.sub) || (iw[i].op == Memory.addi) || (iw[i].op == Memory.subi)) {
					if (iw[i].vOpA == 1 && iw[i].vOpB == 1) { // Esto sieve para tal...
						if (functionUnits[UF_SUM1].inUse == 0) { // Esto pascual ..
							if((iw[i].op == Memory.add) || (iw[i].op == Memory.addi)) functionUnits[UF_SUM1].addFU();
							else functionUnits[UF_SUM1].subFU();
							functionUnits[UF_SUM1].init(lastIndexRob, iw[i].opA, iw[i].opB, iw[i].inm);
							lastIndexRob = (lastIndexRob+1)*ROB_LENGTH;
							iw[i].validLine = 0;
							i++;
						}
						else 
							if (functionUnits[UF_SUM2].inUse == 0) {
								if((iw[i].op == Memory.add) || (iw[i].op == Memory.addi)) functionUnits[UF_SUM2].addFU();
								else functionUnits[UF_SUM2].subFU();
								functionUnits[UF_SUM2].init(lastIndexRob, iw[i].opA, iw[i].opB, iw[i].inm);
								lastIndexRob = (lastIndexRob+1)*ROB_LENGTH;
								iw[i].validLine = 0;
								i++;
							}
						
					}
				}
				else
					if ((iw[i].op == Memory.lw) || (iw[i].op == Memory.sw) ) {
						if (iw[i].vOpA == 1 && (iw[i].op == Memory.lw || iw[i].vOpB == 1)) {
							if (functionUnits[UF_CA].inUse == 0) {
								if((iw[i].op == Memory.add) || (iw[i].op == Memory.lw)) functionUnits[UF_CA].chargeFU();
								else functionUnits[UF_CA].storeFU();
								functionUnits[UF_CA].init(lastIndexRob, iw[i].opA, iw[i].opB, iw[i].inm);
								lastIndexRob = (lastIndexRob+1)*ROB_LENGTH;
								iw[i].validLine = 0;
								i++;
							}
						}
					}
					else
						if (iw[i].op == Memory.mult) {
							if (iw[i].vOpA == 1 && iw[i].vOpB == 1) {
								if (functionUnits[UF_MULT].inUse == 0) {
									functionUnits[UF_MULT].init(lastIndexRob, iw[i].opA, iw[i].opB, iw[i].inm);
									lastIndexRob = (lastIndexRob+1)*ROB_LENGTH;
									iw[i].validLine = 0;
									i++;
								}
							}
						}
				if (i==0) seguir = false;
			}
			else {
				i++;
			}
		}
	}

	private static void etapa_EX(FunctionalUnit[] functionalUnits, ROB[] rob) {
		for(int i=0; i<TOTAL_UF; i++) {
			if (functionalUnits[i].inUse == 1) {
				if(functionalUnits[i].execute()) {
					rob[functionalUnits[i].robLine].res = functionalUnits[i].res;
					rob[functionalUnits[i].robLine].stage = F0;
				}
			}
		}
	}

	private static void etapa_WB(ROB[] rob, InstructionWindow[] instructionWindow) {
		int i=0;
		boolean seguir = true;
		int robPointer = firstIndexRob; // Para que no se modifique el puntero original
		while (i<2 && seguir) {
			if (rob[robPointer].stage == F1 && rob[robPointer].validLine == 1) {
				if (rob[robPointer].destReg == -1) {  // TODO: Esto es si la instrucción es sw? En ese caso sería !=
					Memory.registers[rob[robPointer].destReg].data = rob[robPointer].res;
					Memory.registers[rob[robPointer].destReg].validData = 1;
					rob[robPointer].validLine = 0; // Se invalida la línea
					lastIndexRob++; // A su vez también se 'elimina' del buffer
				}
				//firstIndexRob = (firstIndexRob + 1) % ROB_LENGTH; // TODO: Estás modificando puntero original, solo debe ser modificado cuando se añade un elemento.
				robPointer = (robPointer + 1) % ROB_LENGTH;
				inst_rob--;
				i++;
				numOfInstructions--;
			}
			else
				seguir = false;
		}

		// TODO: Segundo while transformao en un for
		robPointer = firstIndexRob; // Para que no se modifique el puntero original
		for ( i = 0; i < ROB_LENGTH; i++ ) {
			if ( (rob[robPointer].stage == F0) && (rob[robPointer].validLine == 1) ) { // Linea válida con estado a F0
				// Pasar a F1 y marcar res válido
				rob[robPointer].vaildRes = 1;
				rob[robPointer].stage = F1;

				// Actualización de dependencias en VI
				for (i=0; i<MAX_INST;i++) { // Revisar las dos líneas de VI
					// Opernado fuente A
					if ( (instructionWindow[i].opA == robPointer) && (instructionWindow[i].vOpA == 0) ) { // Si se encuentra dependencia en la ventana de instrucciones
						// Se actualiza el en la línea de la VI el resultado
						instructionWindow[i].opA = rob[robPointer].res;
						instructionWindow[i].vOpA = 1;
					// Operando fuente B
					} else if ( (instructionWindow[i].opB == robPointer) && (instructionWindow[i].vOpB == 0) ) {
						instructionWindow[i].opB = rob[robPointer].res;
						instructionWindow[i].vOpB = 1;
					}
				}
			}
			robPointer = (robPointer + 1) % ROB_LENGTH;
		}
		/* AQUI ME PIERDO, es el segundo while. Desde aquí.... */
		/*for (int puntero = 0; puntero<ROB_LENGTH; puntero++) {
			if (rob[puntero].validLine == F0) {
				rob[puntero].vaildRes = 1;
				rob[puntero].stage = F1;
				for(i=0; i<2; i++) {
					if(instructionWindow[i].opA == rob[puntero].res && instructionWindow[i].vOpA == 0) {
						instructionWindow[i].opA = 
					}
				}
			}
		}
		/*..... hasta aquí */
		
		
	}
	
	
	//SHOWERS (Mostradores, no duchas xDD)
	// Diego
	private static void show_instructionQueue() {
		// TODO Auto-generated method stub
		for (Instruction ins : Memory.instructionQueue) {
			if (ins != null)
				System.out.println(ins.toString());
		}
		
	}
	// Diego
	private static void show_instructionWindow(InstructionWindow[] instructionWindow) {
		// TODO Auto-generated method stub
		for (int i = 0; i < WINDOW_SIZE; i++) {
			System.out.println(instructionWindow[i].toString());
		}
		
	}

	private static void show_ROB(ROB[] rob) {
		System.out.println("\tlinea_valida\tDestino\tres\tres_valido\tetapa");
		for(int i=0; i<ROB_LENGTH; i++) {
			System.out.print("L"+i+"\t");
			System.out.print(""+rob[i].validLine+"\t\t");
			System.out.print(""+rob[i].destReg+"\t");
			System.out.print(""+rob[i].res+"\t");
			System.out.print(""+rob[i].vaildRes+"\t");
			System.out.println(rob[i].stage);
		}
	}

	private static void show_DataRegisters() {
		for(int i=0; i<Memory.dataMem.length; i++) {
			System.out.println("Registro "+i+" -> "+Memory.dataMem[i]);
		}
	}
	
}
