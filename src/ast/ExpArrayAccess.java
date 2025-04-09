package ast;

import compile.SymbolTable;
import java.util.List;


public class ExpArrayAccess extends Exp {
    public final String arrayName;
    public final List<Exp> indices;


    public ExpArrayAccess(String arrayName, List<Exp> indices) {
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
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}