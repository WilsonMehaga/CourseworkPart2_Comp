package ast;

import compile.SymbolTable;

public class ExpPlus extends Exp {

    public final Exp left, right;

    public ExpPlus(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void compile(SymbolTable st) {
        left.compile(st);
        right.compile(st);
        emit("add");
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) { return visitor.visit(this); }

}
