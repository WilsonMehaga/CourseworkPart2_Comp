package ast.util;

import ast.*;

public class VisitorAdapter<T> implements Visitor<T> {

    @Override
    public T visit(ExpTimes expTimes) {
        return null;
    }

    @Override
    public T visit(ExpPlus expPlus) {
        return null;
    }

    @Override
    public T visit(ExpMinus expMinus) {
        return null;
    }

    @Override
    public T visit(ExpDiv expDiv) {
        return null;
    }

    @Override
    public T visit(ExpLessThan expLessThan) {
        return null;
    }

    @Override
    public T visit(ExpLessThanEqual expLessThanEqual) {
        return null;
    }

    @Override
    public T visit(ExpEqual expEqual) {
        return null;
    }

    @Override
    public T visit(ExpAnd expAnd) {
        return null;
    }

    @Override
    public T visit(ExpOr expOr) {
        return null;
    }

    @Override
    public T visit(ExpInt expInt) {
        return null;
    }

    @Override
    public T visit(ExpNot expNot) {
        return null;
    }

    @Override
    public T visit(ExpVar expVar) {
        return null;
    }

    @Override
    public T visit(Program program) {
        return null;
    }

    @Override
    public T visit(StmAssign stmAssign) {
        return null;
    }

    @Override
    public T visit(StmBlock stmBlock) {
        return null;
    }

    @Override
    public T visit(StmIf stmIf) {
        return null;
    }

    @Override
    public T visit(StmNewline stmNewline) {
        return null;
    }

    @Override
    public T visit(StmPrint stmPrint) {
        return null;
    }

    @Override
    public T visit(StmPrintChar stmPrintChar) {
        return null;
    }

    @Override
    public T visit(StmPrintln stmPrintln) {
        return null;
    }

    @Override
    public T visit(StmSwitch stmSwitch) {
        return null;
    }

    @Override
    public T visit(StmWhile stmWhile) {
        return null;
    }

    @Override
    public T visit(Type type) {
        return null;
    }

    @Override
    public T visit(TypeInt typeInt) {
        return null;
    }

    @Override
    public T visit(VarDecl varDecl) {
        return null;
    }

    @Override
    public T visit(Proc proc) {
        return null;
    }

    @Override
    public T visit(StmReturn stmReturn) {
        return null;
    }

    @Override
    public T visit(StmCall stmCall) {
        return null;
    }

    @Override
    public T visit(Func func) {
        return null;
    }

    @Override
    public T visit(ExpCall expCall) {
        return null;
    }

    @Override
    public T visit(ExpNewArray expNewArray) {
        return null;
    }

    @Override
    public T visit(ExpNull expNull) {
        return null;
    }

    @Override
    public T visit(ExpArrayAccess expArrayAccess) {
        return null;
    }

    @Override
    public T visit(StmArrayAssign stmArrayAssign) {
        return null;
    }

    @Override
    public T visit(ExpArrayLength expArrayLength) {
        return null;
    }
}
