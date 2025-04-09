package ast;

import compile.SymbolTable;
import java.util.List;

public class ExpCall extends Exp {
    public final String funcName;
    public final List<Exp> arguments;


    public ExpCall(String funcName, List<Exp> arguments) {
        this.funcName = funcName;
        this.arguments = arguments;
    }

    @Override
    public void compile(SymbolTable st) {

        for (int i = arguments.size() - 1; i >= 0; i--) {
            arguments.get(i).compile(st);
        }


        emit("bsr " + st.freshLabel("func_" + funcName));


    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}