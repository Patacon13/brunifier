package main.java.cp.cPlusPlus;

// import ANTLR's runtime libraries
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import main.java.cp.cPlusPlus.sources.*;

public class Cambiame {

    public static void fuenteMarcado(BufferedReader br, PrintWriter pw, List<AnotacionMarca> todasMarcas) throws IOException {
        
        String entrada = "";
        int nroFila = 1;
        
        /*
        while(true) {
        	entrada = br.readLine();
        	if (entrada == null) break;
        	nroFila++;
        	pw.println(entradaConMarcas(entrada, nroFila, todasMarcas));
        }*/
        /* FIXME:
         * Se recorre la lista de marcas 
         * p/cada marca hay que imprimir en
         * la salida las filas hasta que
         * la fila coincida con alguna marca
         * PARA(CadaMarca)
         * MIENTRAS(Fila != FilaMarca) IMPRIMIR(PosicionDentroDeFila, Fila.length())
         * SI NO IMPRIMIR(HastaLaMarca),IMPRIMIR(Marca) <- Guardar PosicionDentroDeFila
         */
        //if(c)
        entrada = br.readLine();
        int posEnFila = 0;
        for(AnotacionMarca marca : todasMarcas) {
        	//Si no coincide con la fila, se imprime la linea completa
        	while(nroFila != marca.getFila()) {
        		pw.println(entrada.substring(posEnFila));
        		posEnFila = 0;
        		entrada = br.readLine();
        		nroFila++;
        	}
        	//Coincide la linea, se imprime hasta la marca y luego se imprime la marca.
        	pw.print(entrada.substring(posEnFila,marca.getPos() + 1));
        	pw.print(marca.getTexto());
        	posEnFila = marca.getPos() + 1;
        }
        pw.print(entrada.substring(posEnFila));
        entrada = br.readLine();
        while(entrada != null) {
        	pw.println(entrada);
        	entrada = br.readLine();
        }
    }
    
  private static String textoMarcasDeFila(List<AnotacionMarca> todasMarcas, int nroFila, int nroPos) {
  
    String salida = "";
    
    if(!todasMarcas.isEmpty()) {
    	AnotacionMarca marca = todasMarcas.get(0);
    	//System.out.println("La marca esta en fila" + todasMarcas.get(0).getFila() + " columna " + todasMarcas.get(0).getPos());
    	//System.out.println("Fila: " + nroFila + " Pos: " + nroPos);
    	if(marca.getFila() == nroFila) 
      	  if(marca.getPos() == nroPos) {
      		  salida += marca.getTexto();
    		  todasMarcas.remove(0);
    	  }
    }
    
    /** @Deprecated
  	for(AnotacionMarca marca : todasMarcas) {
      if(marca.getFila() == nroFila) 
    	  if(marca.getPos() == nroPos)
    		  salida += marca.getTexto();
    }
    */
    return salida;
  	
  }
  
  private static String entradaConMarcas(String entrada, int nroFila, List<AnotacionMarca> todasMarcas) {
	  String salidaConMarcas = "";
	  int i = 0;
	  while(i <= entrada.length()) {
		  if(nroFila == todasMarcas.get(0).getFila()) {
			  if(i != entrada.length()) salidaConMarcas += entrada.charAt(i);
			  salidaConMarcas += textoMarcasDeFila(todasMarcas, nroFila, i);
		  }
		  else 
			  return salidaConMarcas + entrada.substring(i);
		  i++;
	  }
	  
	  /**@Deprecated
	  	for(int i = 0; i<=entrada.length(); i++) {
	  	if(i != entrada.length()) salidaConMarcas += entrada.charAt(i);
	 		salidaConMarcas += textoMarcasDeFila(todasMarcas, nroFila, i);
	  	}
	   */
	  return salidaConMarcas;
}
  
  public static void main(String[] args) throws IOException {

    String inputFileName = args[0];
    String outputFileName = args[1];
    
	FileInputStream streamEntrada = new FileInputStream(inputFileName);
	ANTLRInputStream antlrEntrada = new ANTLRInputStream(streamEntrada);
	
	FileReader entrada = new FileReader(inputFileName);
	BufferedReader reader = new BufferedReader(entrada);
    
	FileWriter salida = new FileWriter(outputFileName);
	PrintWriter writer = new PrintWriter(salida);
	
	escribeMarcas_traslationunit(antlrEntrada, reader, writer);

  }

  public String marca(String entrada) throws IOException {
   
		ANTLRInputStream antlrEntrada = new ANTLRInputStream(entrada);

		StringReader srEntrada = new StringReader(entrada);
		BufferedReader reader = new BufferedReader(srEntrada);

		StringWriter swSalida = new StringWriter();
		PrintWriter writer = new PrintWriter(swSalida); //FIXME: Por que usamos PrintWriter?
	    
		escribeMarcas_traslationunit(antlrEntrada, reader, writer);

		String salida = swSalida.toString();
		
	 	srEntrada.close();
	    swSalida.close();
	    return salida;
  }
 
  public static void escribeMarcas_traslationunit(ANTLRInputStream antlrEntrada, BufferedReader reader, PrintWriter writer) throws IOException{
	  	
	    CPP14Lexer lexer = new CPP14Lexer(antlrEntrada);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CPP14Parser parser = new CPP14Parser(tokens);
		ParseTree tree = parser.translationunit();
		CPPListener extractor = new CPPListener(parser);
	    ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		walker.walk(extractor, tree); // initiate walk of tree with listener

		fuenteMarcado(reader, writer, extractor.marcas);
	 	writer.close();
	    System.out.println(extractor.marcas);
  
  }
}