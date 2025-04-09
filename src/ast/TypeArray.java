package ast;

/**
 * AST node for an array type.
 */
public class TypeArray extends Type {
    public final Type elementType;

    /**
     * Initialise a new array type AST node.
     * @param elementType the type of the array elements
     */
    public TypeArray(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return elementType.toString() + "[]";
    }

    @Override
    public <T> T accept(ast.util.Visitor<T> visitor) {
        return visitor.visit(this);
    }
}