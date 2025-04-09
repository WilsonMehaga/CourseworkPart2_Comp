package ast;

import compile.SymbolTable;
import java.util.List;


public class ExpArrayLength extends Exp {
    public final String arrayName;
    public final List<Exp> indices;  // Empty for direct array.length, non-empty for nested access


    public ExpArrayLength(String arrayName, List<Exp> indices) {
        this.arrayName = arrayName;
        this.indices = indices;
    }

    @Override
    public void compile(SymbolTable st) {

        emit("lda " + SymbolTable.makeVarLabel(arrayName));
        emit("ldh 0");


        for (Exp index : indices) {

            index.compile(st);


            emit("ldh 0");
        }


        emit("ldc -1");
        emit("ldh 0");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}