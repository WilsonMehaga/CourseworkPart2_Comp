package ast;

import compile.SymbolTable;


public class ExpNull extends Exp {


    public ExpNull() {
    }

    @Override
    public void compile(SymbolTable st) {

        emit("ldc 0");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}