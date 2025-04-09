package ast;

import compile.SymbolTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program extends AST {

    public final List<VarDecl> varDecls;
    public final List<Stm> body;
    private List<Callable> callables;  // Changed from List<Procedure>

    /**
     * Initialise a new Program AST.
     * @param varDecls the global variable declarations
     * @param body the statements in the main body of the program
     */
    public Program(List<VarDecl> varDecls, List<Stm> body) {
        this.varDecls = varDecls;
        this.body = Collections.unmodifiableList(body);
        this.callables = new ArrayList<>();
    }

    /**
     * Set the callable units (procedures and functions) for this program.
     * @param callables the callable unit declarations
     */
    public void setCallables(List<Callable> callables) {  // Changed from setProcedures
        this.callables = Collections.unmodifiableList(new ArrayList<>(callables));
    }

    /**
     * Get the callable unit declarations for this program.
     * @return the callable unit declarations
     */
    public List<Callable> getCallables() {  // Changed from getProcedures
        return callables;
    }

    /**
     * Emit SSM assembly code for this program.
     */
    public void compile() {
        SymbolTable st = new SymbolTable(this);


        for(Stm stm: body) {
            stm.compile(st);
        }
        emit("halt");

        emit(".data");
        for (String varName: st.globalNames()) {
            emit(st.makeVarLabel(varName) + ": 0");
        }
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) { return visitor.visit(this); }

}