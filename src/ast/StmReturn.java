package ast;

import compile.SymbolTable;

/**
 * AST node for a return statement
 */
public class StmReturn extends Stm {
    public final Exp returnValue;

    /**
     * Initialise a new return statement AST.
     * @param returnValue the expression to return, or null for void return
     */
    public StmReturn(Exp returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void compile(SymbolTable st) {
        if (returnValue != null) {

            returnValue.compile(st);
        } else {

            emit("ldc 0");
        }
        emit("ret");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}