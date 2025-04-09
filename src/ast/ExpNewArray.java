package ast;

import compile.SymbolTable;
import java.util.List;


public class ExpNewArray extends Exp {
    public final List<Exp> dimensions;


    public ExpNewArray(List<Exp> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public void compile(SymbolTable st) {
        dimensions.get(0).compile(st);
        emit("ldc 0");
        emit("heapgrow");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}