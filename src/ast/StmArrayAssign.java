package ast;

import compile.SymbolTable;
import java.util.List;

/**
 * AST node for an array assignment statement.
 */
public class StmArrayAssign extends Stm {
    public final String arrayName;
    public final List<Exp> indices;
    public final Exp value;

    /**
     * Initialise a new array assignment statement AST node.
     * @param arrayName the name of the array variable
     * @param indices the list of index expressions
     * @param value the value to assign
     */
    public StmArrayAssign(String arrayName, List<Exp> indices, Exp value) {
        this.arrayName = arrayName;
        this.indices = indices;
        this.value = value;
    }

    @Override
    public void compile(SymbolTable st) {

        emit("lda " + SymbolTable.makeVarLabel(arrayName));
        emit("ldh 0");


        for (int i = 0; i < indices.size() - 1; i++) {

            indices.get(i).compile(st);


            emit("ldh 0");
        }


        indices.get(indices.size() - 1).compile(st);


        value.compile(st);


        emit("sth 0");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}