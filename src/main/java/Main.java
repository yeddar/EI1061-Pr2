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
	public static final int TOTAL_UF = 4, UF_SUM1 = 0, UF_SUM2 = 1, UF_CA = 2, UF_MULT = 3;

	// Número máximo de intrucciones que se pueden procesar en cada etapa por ciclo
	public static final int MAX_INST = 2;

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
		

		int i = 0;
		while ((inst_rob > 0) || (numOfInstructions > 0)) { // un ciclo de simulación ejecuta las 5 etapas.
			System.out.println("inst_rob = "+inst_rob);
			System.out.println("numOfInstruction = "+numOfInstructions);
			//System.out.println("Numero ventana instruccion = "+ inst_instructionWindow);

		    // WB, RX, ISS, ID, IF
			//TODO: Creo que habría que cambiar el orden de ejecución de las etapas para que no se sobreescriban los registros
			System.out.println("Etapa WB");
			etapa_WB(rob, instructionWindow);
			System.out.println("Etapa EX");
			etapa_EX(functionUnits, rob);
			System.out.println("Etapa ISS");
			etapa_ISS(instructionWindow, functionUnits, rob);
			System.out.println("Etapa ID");
			etapa_ID(instructionWindow, rob, firstIndexRob);
			System.out.println("Etapa IF");
			etapa_IF(); // etapa_IF();


			//Mostrar el contenido de las distintas estructuras para ver como evoluciona la simulación
			System.out.println("\nCiclo numero "+i+"\n");
			show_instructionQueue();
			show_instructionWindow(instructionWindow);
			show_ROB(rob);
			show_FU(functionUnits);
			show_DataRegisters();

			//if (i==10) break;
			i++;

		  }
	}

	private static void etapa_IF() {
		// Check size of the queue
		int i = 0;
		while ( ( Memory.instructionQueue.size() < QUEUE_MAX_LENGTH ) && (i < MAX_INST) && (programCounter < numOfInstructions) ) {
			System.out.println("-----------Cola: "+Memory.instructionMem[programCounter]);
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
			if (robPointer < 0) break;
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
			System.out.println(lastIndexRob);
			rob[lastIndexRob].set(validLine,destReg,res,validRes,stage);
		}
		int devolver = lastIndexRob;
		lastIndexRob = (lastIndexRob + 1) % ROB_LENGTH;
		inst_rob++; // Actualiza el número de elementos en ROB
		return devolver;
	}


	private static void etapa_ID(InstructionWindow[] iw, ROB[] rob, int robPointer) {
		// TODO Hay que crear una función para la búsqueda de un operando en ROB y evitar la repetición de código.
		// Get instructions from instructions queue.
		System.out.println(inst_instructionWindow);
		if ( inst_instructionWindow == 0 ) {
			int wPointer = 0;
			while ( (Memory.instructionQueue.size() > 0) && wPointer < MAX_INST) {
				Instruction ins = Memory.instructionQueue.poll();
				if (ins == null) break;
				// Antes de cargar instrucción, buscar en banco de registros validez y ROB

				// Register identifiers
				int id_ra = ins.getRa();
				int id_rb = ins.getRb();
				int id_rc = ins.getRc();

				// Inicializar línea ventana
				iw[wPointer].rset();
				
				// Parte 1. Búsqueda operando A
				if (Memory.registers[id_ra].validData == 1) { // Si registro tiene contenido válido
					iw[wPointer].opA = Memory.registers[id_ra].data;
					iw[wPointer].vOpA = 1; // TODO: Validar línea de instrucciones
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

				//Parte 2. Búsqueda operando B i inmediato
				// Operando B. Tener en cuenta que puede ser dato inmediato
				if (ins.getType() == Memory.typeI) { //Type I instruction
					iw[wPointer].inm = ins.getInm(); // TODO: Cambiado
				}
				if (ins.getType() == Memory.typeR || ins.getOperationCode() == Memory.sw) {
					if (Memory.registers[id_rb].validData == 1) {
						iw[wPointer].opB = Memory.registers[id_rb].data;
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
				}

				// Actualizar bit de validez banco de registros
				if (ins.getOperationCode() != Memory.sw) { // Intrucción de carga en registro
					Memory.registers[id_rc].validData = 0;
					// Add instruction into ROB
					iw[wPointer].robLine = addLineROB(rob, 1, id_rc, 0, 0, ID);
				}

				// Marcar línea ventana inst. como válida e incrementar puntero.

				iw[wPointer].op = ins.getOperationCode();
				iw[wPointer].type = ins.getType();
				iw[wPointer].validLine = 1;
				wPointer++;

				// Actualizar tamaño ventana intrucciones
				inst_instructionWindow++;
			}// Fin while
		} // fin if
	}


	// Diego
	private static void etapa_ISS(InstructionWindow[] iw, FunctionalUnit[] functionUnits, ROB[] rob) {
		int i=0;
		boolean seguir = true;
		while (i < MAX_INST && seguir) {
			if(iw[i].validLine == 1) {
				if ((iw[i].op == Memory.add) || (iw[i].op == Memory.sub) || (iw[i].op == Memory.addi) || (iw[i].op == Memory.subi)) {
					if (iw[i].vOpA == 1 && (iw[i].vOpB == 1 || iw[i].op == Memory.addi || iw[i].op == Memory.subi) ) { // Esto sieve para mirar que las instrucciones tienen sus datos validos
						if (functionUnits[UF_SUM1].inUse == 0) { // Esto para que, en caso de estar la FU libre, enviar la instruccion
							inst_instructionWindow--; // TODO Prueba
							if((iw[i].op == Memory.add) || (iw[i].op == Memory.addi)) functionUnits[UF_SUM1].addFU();
							else functionUnits[UF_SUM1].subFU();
							functionUnits[UF_SUM1].init(iw[i].robLine, iw[i].opA, iw[i].opB, iw[i].inm);
							//lastIndexRob = (lastIndexRob+1)%ROB_LENGTH;

							//iw[i].validLine = 0; // TODO: Cuando se borra linea de ventana hay que dejar TODOS los bits de validez a 0 y el inmediato también
							// Lo más fácil es:
							iw[i].rset(); // Pone toda la línea a sus valores por defecto
							i++;
						}
						else 
							if (functionUnits[UF_SUM2].inUse == 0) {
								inst_instructionWindow--; // TODO Prueba
								if((iw[i].op == Memory.add) || (iw[i].op == Memory.addi)) functionUnits[UF_SUM2].addFU();
								else functionUnits[UF_SUM2].subFU();
								functionUnits[UF_SUM2].init(iw[i].robLine, iw[i].opA, iw[i].opB, iw[i].inm);
								//lastIndexRob = (lastIndexRob+1)%ROB_LENGTH;
								//iw[i].validLine = 0;
								iw[i].rset();
								i++;
							}
						
					}
				}
				else {
					if ((iw[i].op == Memory.lw) || (iw[i].op == Memory.sw) ) {
						if (iw[i].vOpA == 1 && (iw[i].op == Memory.lw || iw[i].vOpB == 1)) {
							if (functionUnits[UF_CA].inUse == 0) {
								inst_instructionWindow--; // TODO Prueba
								if((iw[i].op == Memory.add) || (iw[i].op == Memory.lw)) functionUnits[UF_CA].chargeFU();
								else functionUnits[UF_CA].storeFU();
								functionUnits[UF_CA].init(iw[i].robLine, iw[i].opA, iw[i].opB, iw[i].inm);
								//lastIndexRob = (lastIndexRob+1)%ROB_LENGTH;
								//iw[i].validLine = 0;
								iw[i].rset();
								i++;
							}
						}
					}
					else
						if (iw[i].op == Memory.mult) {
							if (iw[i].vOpA == 1 && iw[i].vOpB == 1) {
								if (functionUnits[UF_MULT].inUse == 0) {
									inst_instructionWindow--; // TODO Prueba
									functionUnits[UF_MULT].init(iw[i].robLine, iw[i].opA, iw[i].opB, iw[i].inm);
									//lastIndexRob = (lastIndexRob+1)%ROB_LENGTH;
									//iw[i].validLine = 0;
									iw[i].rset();
									i++;
								}
							}
						}else{
							throw new RuntimeException("iw["+i+"].op = "+iw[i].op);
						}
				}
				if (i==0) {
					System.out.println("Aquí nunca llego!!!!");
					seguir = false;
				}
				
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
					System.out.println("------------->"+functionalUnits[i].robLine);
					if(functionalUnits[i].robLine<0) break;
					rob[functionalUnits[i].robLine].res = functionalUnits[i].res;
					rob[functionalUnits[i].robLine].stage = F0;
				}
			}
		}
	}

	private static void etapa_WB(ROB[] rob, InstructionWindow[] instructionWindow) {
		int i=0;
		boolean seguir = firstIndexRob >= 0;
		while (i < MAX_INST && seguir) {
			if (rob[firstIndexRob].stage == F1 && rob[firstIndexRob].validLine == 1) {
				if (rob[firstIndexRob].destReg >= 0) { // TODO: Error. Se escribe en registros en todas las instrucciones excepto las de tipo SW
					Memory.registers[rob[firstIndexRob].destReg].data = rob[firstIndexRob].res;
					Memory.registers[rob[firstIndexRob].destReg].validData = 1;
				}
				rob[firstIndexRob].validLine = 0; // Se invalida la línea // TODO: Se ha sacado del if interno
				firstIndexRob = (firstIndexRob + 1) % ROB_LENGTH;
				inst_rob--;
				i++;
				numOfInstructions--;
			}
			else
				seguir = false;
		}

		int robPointer = firstIndexRob; // Para que no se modifique el puntero original
		for ( i = 0; i < ROB_LENGTH; i++ ) {
			if (robPointer < 0) break;
			if ( (rob[robPointer].stage == F0) && (rob[robPointer].validLine == 1) ) { // Linea válida con estado a F0
				// Pasar a F1 y marcar res válido
				rob[robPointer].vaildRes = 1;
				rob[robPointer].stage = F1;

				// Actualización de dependencias en VI
				for (i = 0; i < MAX_INST; i++) { // Revisar las dos líneas de VI
					// Opernado fuente A
					if ( (instructionWindow[i].opA == robPointer) && (instructionWindow[i].vOpA == 0) ) { // Si se encuentra dependencia en la ventana de instrucciones
						// Se actualiza el en la línea de la VI el resultado
						instructionWindow[i].opA = rob[robPointer].res;
						instructionWindow[i].vOpA = 1;
					// Operando fuente B
					}
					// TODO: Aquí estaba el error de registros iguales en misma instrucción
					if ( (instructionWindow[i].opB == robPointer) && (instructionWindow[i].vOpB == 0) ) {
						instructionWindow[i].opB = rob[robPointer].res;
						instructionWindow[i].vOpB = 1;
					}
				}
			}
			robPointer = (robPointer + 1) % ROB_LENGTH;
		}
	}

	//SHOWERS (Mostradores, no duchas xDD)
	// Diego
	private static void show_instructionQueue() {
		System.out.println("Instruction queue:");
		if(Memory.instructionQueue.isEmpty()) System.out.println("vacía");
		for (Instruction ins : Memory.instructionQueue) {
			if (ins != null)
				System.out.println(ins.toString());
		}
		
	}
	// Diego
	private static void show_instructionWindow(InstructionWindow[] instructionWindow) {
		for (int i = 0; i < WINDOW_SIZE; i++) {
			System.out.println("InstructionWindow["+i+"] = "+instructionWindow[i].toString());
		}
		
	}

	private static void show_ROB(ROB[] rob) {
		
		int robPointer = firstIndexRob;
		if (robPointer < 0) robPointer = 0;
		for (int i = 0; i < ROB_LENGTH; i++) {
			System.out.println("ROB["+i+"] = "+rob[i].toString());
			robPointer = (robPointer + 1) % ROB_LENGTH;
		}
	}

	private static void show_DataRegisters() { // TODO: Se estaba mostrando la memoria de datos en lugar del banco de registros.
		int total = Memory.registers.length;
		int cantidad = (total+3)/4;
		for(int i=0; i<cantidad; i++) {
			System.out.print((i>=10)? "Registro "+i+" -> "+Memory.registers[i].data:"Registro  "+i+" -> "+Memory.registers[i].data);
			System.out.print(((i+cantidad)>=10)?"\t\tRegistro "+(i+cantidad)+" -> "+Memory.registers[(i+cantidad)].data:"\t\tRegistro  "+(i+cantidad)+" -> "+Memory.registers[(i+cantidad)].data);
			System.out.print(((i+2*cantidad)>=10)?"\t\tRegistro "+(i+2*cantidad)+" -> "+Memory.registers[(i+2*cantidad)].data:"\t\tRegistro  "+(i+2*cantidad)+" -> "+Memory.registers[(i+2*cantidad)].data);
			if ((i+3*cantidad)<total) System.out.print(((i+3*cantidad)>=10)?"\t\tRegistro "+(i+3*cantidad)+" -> "+Memory.registers[(i+3*cantidad)].data:"\t\tRegistro  "+(i+3*cantidad)+" -> "+Memory.registers[(i+3*cantidad)].data);
			System.out.println();
		}
	}
	
	private static void show_FU(FunctionalUnit[] fu) {
		for(FunctionalUnit i : fu) {
			System.out.println(i);
		}
	}
	
}
