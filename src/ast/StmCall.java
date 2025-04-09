package ast;

import compile.SymbolTable;

import java.util.List;

/**
 * AST node for a procedure call statement
 */
public class StmCall extends Stm {
    public final String procName;
    public final List<Exp> arguments;

    /**
     * Initialise a new procedure call statement AST.
     * @param procName the name of the procedure to call
     * @param arguments the arguments to pass to the procedure
     */
    public StmCall(String procName, List<Exp> arguments) {
        this.procName = procName;
        this.arguments = arguments;
    }

    @Override
    public void compile(SymbolTable st) {

        emit("bsr " + st.freshLabel("proc_" + procName));

    }
    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}