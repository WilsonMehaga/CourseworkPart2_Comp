package ast;

import compile.SymbolTable;
import java.util.List;

/**
 * Base class for callable units (procedures and functions)
 */
public abstract class Callable extends AST {
    public final String name;
    public final List<VarDecl> parameters;
    public final List<VarDecl> locals;

    /**
     * Initialise a new Callable AST node.
     * @param name the name of the callable unit
     * @param parameters the parameter declarations
     * @param locals the local variable declarations
     */
    public Callable(String name, List<VarDecl> parameters, List<VarDecl> locals) {
        this.name = name;
        this.parameters = parameters;
        this.locals = locals;
    }

    /**
     * Emit SSM assembly code for this callable unit.
     */
    public abstract void compile(SymbolTable st);
}