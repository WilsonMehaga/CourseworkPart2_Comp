package ast;

import compile.SymbolTable;

public class ExpEqual extends Exp {

    public final Exp left, right;

    public ExpEqual(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void compile(SymbolTable st) {
        left.compile(st);
        right.compile(st);
        emit("sub", "test_z");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) { return visitor.visit(this); }

}
