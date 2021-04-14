package main.java.cp.cPlusPlus;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
//import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Interval;

import main.java.cp.cPlusPlus.sources.*;


public class CPPListener extends CPP14BaseListener {
	
	//declarar y asignar atributo de lista de marcas
	public final List<AnotacionMarca> marcas = new ArrayList<>();
		
	private CPP14Parser parser;
	
	public CPPListener(CPP14Parser parser) {
		this.parser = parser;
	}
	
	private String getRuleOriginalCode(ParserRuleContext ctx) {
		return getOriginalCode(ctx.getStart(), ctx.getStop());
		/*int start = ctx.getStart().getStartIndex();
		int stop = ctx.getStop().getStopIndex();
		Interval interval = new Interval(start, stop);
		return ctx.start.getInputStream().getText(interval);
		*/
	}
	
	private String getOriginalCode(Token start, Token stop) {
		return getOriginalCode(start,stop,1);
	}
	
	private String getOriginalCode(Token start, Token stop, int adicion) {
		//Se crea un int que sera el index siguiente al stop.
		int indexNuevo = stop.getStopIndex() + adicion;
		Interval interval = new Interval(start.getStartIndex(), indexNuevo);
		String retorno = start.getInputStream().getText(interval);
		retorno = retorno.replaceAll("(\n|\r|\t| )+", " ");
		//reemplazar todos los espacios, tabulaciones
		return retorno;
	}
	
	@Override
	public void exitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
		//attributespecifierseq? declspecifierseq? declarator virtspecifierseq? functionbody
		String texto = "CIERRA " + getOriginalCode(ctx.declarator().getStart(),ctx.declarator().getStop(),0) + " DE LINEA " + ctx.getStart().getLine();
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
		ctx.getStop().getCharPositionInLine(), texto));	
	}
	
	@Override
	public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
		//attributespecifierseq? declspecifierseq? declarator virtspecifierseq? functionbody
		String texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
	    Token ultimoAntesDeMarca = ctx.virtspecifierseq() != null ? ctx.virtspecifierseq().getStop() : ctx.declarator().getStop();
		marcas.add(new AnotacionMarca(ultimoAntesDeMarca.getLine(),
									 ultimoAntesDeMarca.getCharPositionInLine(),
									 texto));	
	}
		
	@Override
	public void exitSwitchStatement(CPP14Parser.SwitchStatementContext ctx) {
		//Switch '(' condition ')' statement	
		String switchCompleto = getOriginalCode(ctx.getStart(), ctx.condition().getStop());
		String texto = "CIERRA " + switchCompleto + " DE LINEA " + ctx.getStart().getLine();
		marcas.add(new AnotacionMarca(ctx.statement().getStop().getLine(),
			    					  ctx.getStop().getCharPositionInLine(),
				  					  texto));
	}
	
    @Override
    public void enterSwitchStatement(CPP14Parser.SwitchStatementContext ctx) {
		//Switch '(' condition ')' statement	
            String texto = "CIERRA EN LINEA " + ctx.statement().getStop().getLine();
            Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 2).getPayload();
            marcas.add(new AnotacionMarca(parentesis.getLine(),
                                          parentesis.getCharPositionInLine(),
                                          texto));		
    }
	
	/** Listen to matches of classDeclaration */
	@Override
	public void enterIfElseStatement(CPP14Parser.IfElseStatementContext ctx) {
        //If '(' condition ')' statement Else statement	
        String texto = "CIERRA EN LINEA " + ctx.statement(0).getStop().getLine();
        Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 4).getPayload();
        marcas.add(new AnotacionMarca(parentesis.getLine(),
                                      parentesis.getCharPositionInLine(),
                                      texto));
	}
	
	@Override
    public void enterIfStatement(CPP14Parser.IfStatementContext ctx) {
        //If '(' condition ')' statement		
        String texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
        Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 2).getPayload();
        marcas.add(new AnotacionMarca(parentesis.getLine(),
                                      parentesis.getCharPositionInLine(),
                                      texto));		
    }
	
	@Override
	public void exitIfElseStatement(CPP14Parser.IfElseStatementContext ctx) {
		//If '(' condition ')' statement Else statement	
		String ifIncompleto = getOriginalCode(ctx.getStart(), ctx.condition().getStop());
		String texto = "CIERRA " + ifIncompleto + " DE LINEA " + ctx.getStart().getLine();
		marcas.add(new AnotacionMarca(ctx.statement(0).getStop().getLine(),
			  					  ctx.statement(1).getStop().getCharPositionInLine(),
			  					  texto));
		texto = "CIERRA " + " EN LINEA " + ctx.statement(1).getStop().getLine();
		marcas.add(new AnotacionMarca(ctx.statement(1).getStart().getLine(),
			  					  ctx.statement(1).getStart().getCharPositionInLine(),
			  					  texto));	
		String elseCompleto = "else";
		texto = "CIERRA " + elseCompleto + " DE LINEA " + ctx.statement(1).getStart().getLine();
		marcas.add(new AnotacionMarca(ctx.statement(1).getStop().getLine(),
			  					  ctx.getStop().getCharPositionInLine(),
			  					  texto));
	}
	
	@Override
	public void exitIfStatement(CPP14Parser.IfStatementContext ctx) {
		//If '(' condition ')' statement		
		String ifCompleto = getOriginalCode(ctx.getStart(), ctx.condition().getStop());
		String texto = "CIERRA " + ifCompleto + " DE LINEA " + ctx.getStart().getLine();
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
                                      ctx.getStop().getCharPositionInLine(),
                                      texto));		
	}
	
	@Override
	public void exitWhileStatement(CPP14Parser.WhileStatementContext ctx) {
		//While '(' condition ')' statement
		String whileCompleto = getOriginalCode(ctx.getStart(), ctx.condition().getStop());
		String texto = "CIERRA " + whileCompleto + " DE LINEA " + ctx.getStart().getLine();
		
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
				  					  ctx.getStop().getCharPositionInLine(),
				  					  texto));		
	}
	
	@Override
	public void exitDoWhileStatement(CPP14Parser.DoWhileStatementContext ctx) {
		//Do statement While '(' expression ')' ';'
		String DoW = "do";
		String texto = "CIERRA " + DoW + " DE LINEA " + ctx.getStart().getLine();
		
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
				  					  ctx.getStop().getCharPositionInLine(),
				  					  texto));
	}
	
	@Override
	public void exitForStatement(CPP14Parser.ForStatementContext ctx){
		//For '(' forinitstatement condition? ';' expression? ')' statement
		String textoFor, texto;
		String forCompleto = "";
		String forinitstatement = getRuleOriginalCode(ctx.forinitstatement());
		String condition = "";
		String expression = "";
		
		if(ctx.expression() != null) {
			//Se envia expression. La funcion automáticamente encontrara el token siguiente.
			forCompleto = getOriginalCode(ctx.getStart(), ctx.expression().getStop());
		}
			
		//textoFor = "for (" + forinitstatement + condition + ";" + expression + ")";
		texto = "CIERRA " + forCompleto + " DE LINEA " + ctx.getStart().getLine();
		
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
				  					  ctx.getStop().getCharPositionInLine(),
				  					  texto));				
	}
	
	@Override
	public void exitForEachStatement(CPP14Parser.ForEachStatementContext ctx) {
		//For '(' forrangedeclaration ':' forrangeinitializer ')' statement	
		String forEachCompleto = getOriginalCode(ctx.getStart(), ctx.forrangeinitializer().getStop());
		String texto = "CIERRA " + forEachCompleto + " DE LINEA " + ctx.getStart().getLine();
		
		marcas.add(new AnotacionMarca(ctx.getStop().getLine(),
				  					  ctx.getStop().getCharPositionInLine(),
				  					  texto));	
	}
	
	@Override
	public void enterForEachStatement(CPP14Parser.ForEachStatementContext ctx) {
		//For '(' forrangedeclaration ':' forrangeinitializer ')' statement
		String texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
		Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 2).getPayload();
		marcas.add(new AnotacionMarca(parentesis.getLine(),
						parentesis.getCharPositionInLine(),
						texto));
	}
	
	@Override
	public void enterWhileStatement(CPP14Parser.WhileStatementContext ctx) {
		//While '(' condition ')' statement
		String texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
		Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 2).getPayload();
		marcas.add(new AnotacionMarca(parentesis.getLine(),
				   parentesis.getCharPositionInLine(),
				   texto));		
	}
	
	@Override
	public void enterDoWhileStatement(CPP14Parser.DoWhileStatementContext ctx) {
		//Do statement While '(' expression ')' ';';
		String texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
		Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 5).getPayload();
		marcas.add(new AnotacionMarca(parentesis.getLine(),
		parentesis.getCharPositionInLine(),
		texto));
	}
	
	@Override
	public void enterForStatement(CPP14Parser.ForStatementContext ctx){
		//For '(' forinitstatement condition? ';' expression? ')' statement	
		String texto;
		texto = "CIERRA EN LINEA " + ctx.getStop().getLine();
		Token parentesis = (Token)ctx.getChild(ctx.getChildCount() - 2).getPayload();
		marcas.add(new AnotacionMarca(parentesis.getLine(),
				  					  parentesis.getCharPositionInLine(),
				  					  texto));
	}
}
