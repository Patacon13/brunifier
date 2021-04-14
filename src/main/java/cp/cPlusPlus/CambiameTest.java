package main.java.cp.cPlusPlus;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CambiameTest {

	final String nl = System.lineSeparator();
	@Test
	void testMainSinCuerpoDosLineas() {
		  Cambiame c = new Cambiame();
		  String prog = "int main() {\n}";
		  String esperado = "int main()/*/CIERRA EN LINEA 2/*/ {" + nl +
		  		"}/*/CIERRA main() DE LINEA 1/*/" + nl;
		  try {
			  String marcado = c.marca(prog);
			  assertEquals(esperado,marcado, "No son iguales.");
		  }
		  catch(IOException e) {
			  fail("Excepcion de entrada/salida.");
		  }
	}
}
