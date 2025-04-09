package ast;

import compile.SymbolTable;

import java.util.Collections;
import java.util.List;

/**
 * AST node for procedure declarations
 */
public class Proc extends Callable {
    public final List<Stm> body;

    /**
     * Initialise a new Procedure AST.
     * @param name the name of the procedure
     * @param parameters the procedure's parameter declarations
     * @param locals the local variable declarations
     * @param body the statements in the procedure body
     */
    public Proc(String name, List<VarDecl> parameters, List<VarDecl> locals, List<Stm> body) {
        super(name, parameters, locals);
        this.body = Collections.unmodifiableList(body);
    }

    /**
     * Emit SSM assembly code for this procedure.
     */
    @Override
    public void compile(SymbolTable st) {

        String procLabel = st.freshLabel("proc_" + name);
        emit(procLabel + ":");


        for (Stm stm : body) {
            stm.compile(st);
        }


        emit("ret");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}