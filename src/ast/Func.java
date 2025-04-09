package ast;

import compile.SymbolTable;

import java.util.Collections;
import java.util.List;

/**
 * AST node for function declarations (procedures with return values)
 */
public class Func extends Callable {
    public final Type returnType;
    public final List<Stm> body;

    /**
     * Initialise a new Function AST.
     * @param returnType the return type of the function
     * @param name the name of the function
     * @param parameters the function's parameter declarations
     * @param locals the local variable declarations
     * @param body the statements in the function body
     */
    public Func(Type returnType, String name, List<VarDecl> parameters, List<VarDecl> locals, List<Stm> body) {
        super(name, parameters, locals);
        this.returnType = returnType;
        this.body = Collections.unmodifiableList(body);
    }

    /**
     * Emit SSM assembly code for this function.
     */
    @Override
    public void compile(SymbolTable st) {

        String funcLabel = st.freshLabel("func_" + name);
        emit(funcLabel + ":");


        for (Stm stm : body) {
            stm.compile(st);
        }
        emit("ldc 0");
        emit("ret");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}