package cp.aedd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cp.ComponenteDeProcesamiento;
import cp.Linea;

public class DelimitadorFunciones extends ComponenteDeProcesamiento {

	@Override
	public List<String> ejecutar(List<String> archivo) {
		// Uso Array List porque es mas eficiente la operacion get(i) y no necesito hacer add()
		List<String> archivoTransformado = new ArrayList<>(archivo);
		List<Linea> pilaBloquesAbiertos = new ArrayList<>();
		int numLinea = 1;
		for(String lineaOriginal: archivo) {
			String lineaConMarca = "";
			if(abreBloque(lineaOriginal.charAt(lineaOriginal.length() - 1))){
				Linea lineaEnPila = new Linea(numLinea,"");
				String aux = lineaOriginal.substring(0, lineaOriginal.length() - 2);
				if(esCabeceraDeFuncion(aux)){
					
					pilaDeBloquesAbiertos.add(lineaEnPila.setCodLinea(aux));
				}
			}
			else{
				if(cierraBloque(lineaOriginal.charAt(lineaOriginal.length() - 1))){
					String marca = "//CIERRA EL BLOQUE DE " + pilaDeBloquesAbiertos.remove(pilaDeBloquesAbiertos.size() - 1).toString();
					lineaConMarca = lineaOriginal + marca;
					if(marca.startsWith("//CIERRA EL BLOQUE DE switch")){
						pilaDeSwitch.remove(pilaDeSwitch.size() - 1);
					}
				}
				else{
					if(lineaOriginal.equals("};")){
						String marca = "//CIERRA LA DEFINICION DE " + pilaDeBloquesAbiertos.remove(pilaDeBloquesAbiertos.size() - 1).toString();
						lineaConMarca = lineaOriginal + marca;
					}
					else{
						lineaConMarca = lineaOriginal;
					}
				}
			}
			numLinea++;
		}
		return archivoTransformado;
	}

	private boolean abreBloque(char c) {
		return (c == '{');
	}

	private boolean cierraBloque(char c) {
		return (c == '}');
	}
}