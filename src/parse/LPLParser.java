package parse;

import ast.*;
import sbnf.ParseException;
import sbnf.lex.Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** Parse an LPL program and build its AST.  */
public class LPLParser {
    public static final String LPL_SBNF_FILE = "data/LPL.sbnf";

    private Lexer lex;

    /**
     * Initialise a new LPL parser.
     */
    public LPLParser() {
        lex = new Lexer(LPL_SBNF_FILE);
    }

    /** Parse an LPL source file and return its AST.
     *
     * @param sourcePath a path to an LPL source file
     * @return the Program AST for the parsed LPL program
     * @throws ParseException if the source file contains syntax errors
     * @throws IOException
     */
    public Program parse(String sourcePath) throws IOException {
        lex.readFile(sourcePath);
        lex.next();
        Program prog = Program();


        List<Callable> callables = new LinkedList<>();
        while (lex.tok().isType("PROC") || lex.tok().isType("FUN")) {
            if (lex.tok().isType("PROC")) {
                callables.add(ProcedureDecl());
            } else { // FUN
                callables.add(FunctionDecl());
            }
        }


        prog.setCallables(callables);

        if (!lex.tok().isType("EOF")) {
            throw new ParseException(lex.tok(), "EOF");
        }
        return prog;
    }

    private Program Program() {
        List<VarDecl> globals = new LinkedList<>();
        List<Stm> body = new LinkedList<>();
        lex.eat("BEGIN");
        while (lex.tok().isType("INT_TYPE")) {
            globals.add(GlobalVarDecl());
        }
        while (!lex.tok().isType("END")) {
            body.add(Stm());
        }
        lex.eat("END");
        return new Program(globals, body);
    }

    private Proc ProcedureDecl() {
        lex.eat("PROC");
        String name = lex.eat("ID");
        lex.eat("LBR");


        List<VarDecl> params = new LinkedList<>();
        if (!lex.tok().isType("RBR")) {
            params.add(ParamDecl());
            while (lex.tok().isType("COMMA")) {
                lex.eat("COMMA");
                params.add(ParamDecl());
            }
        }
        lex.eat("RBR");
        lex.eat("LCBR");


        List<VarDecl> locals = new LinkedList<>();
        while (lex.tok().isType("INT_TYPE")) {
            locals.add(LocalVarDecl());
        }


        List<Stm> body = new LinkedList<>();
        while (!lex.tok().isType("RCBR")) {
            body.add(Stm());
        }
        lex.eat("RCBR");

        return new Proc(name, params, locals, body);
    }

    private Func FunctionDecl() {
        lex.eat("FUN");
        Type returnType = FunctionReturnType();
        String name = lex.eat("ID");
        lex.eat("LBR");


        List<VarDecl> params = new LinkedList<>();
        if (!lex.tok().isType("RBR")) {
            params.add(ParamDecl());
            while (lex.tok().isType("COMMA")) {
                lex.eat("COMMA");
                params.add(ParamDecl());
            }
        }
        lex.eat("RBR");
        lex.eat("LCBR");


        List<VarDecl> locals = new LinkedList<>();
        while (lex.tok().isType("INT_TYPE")) {
            locals.add(LocalVarDecl());

        }


        List<Stm> body = new LinkedList<>();
        while (!lex.tok().isType("RCBR")) {
            body.add(Stm());
        }
        lex.eat("RCBR");

        return new Func(returnType, name, params, locals, body);
    }


    private Type FunctionReturnType() {
        Type baseType = Type();


        while (lex.tok().isType("LSQBR")) {
            lex.eat("LSQBR");
            lex.eat("RSQBR");
            baseType = new TypeArray(baseType);
        }

        return baseType;
    }

    private VarDecl ParamDecl() {
        Type t = ComplexType();
        String id = lex.eat("ID");
        return new VarDecl(t, id);
    }

    private VarDecl LocalVarDecl() {
        Type t = ComplexType();
        String id = lex.eat("ID");
        lex.eat("SEMIC");
        return new VarDecl(t, id);
    }

    private List<VarDecl> GlobalVarDeclKleene() {
        switch(lex.tok().type) {
            case "INT_TYPE": {
                VarDecl varDecl = GlobalVarDecl();
                List<VarDecl> rest = GlobalVarDeclKleene();
                rest.add(0, varDecl);
                return rest;
            }
            default:
                return new ArrayList<>();
        }
    }

    private Type Type() {
        lex.eat("INT_TYPE");
        Type t = new TypeInt();
        return t;
    }


    private Type ComplexType() {
        Type baseType = Type();

        // Check for array dimensions
        while (lex.tok().isType("LSQBR")) {
            lex.eat("LSQBR");
            lex.eat("RSQBR");
            baseType = new TypeArray(baseType);
        }

        return baseType;
    }

    private VarDecl GlobalVarDecl() {
        Type t = ComplexType();
        String id = lex.eat("ID");
        lex.eat("SEMIC");
        return new VarDecl(t, id);
    }

    private Stm Stm() {
        switch (lex.tok().type) {
            case "ID": {
                String id = lex.eat("ID");
                if (lex.tok().isType("ASSIGN")) {
                    lex.eat("ASSIGN");
                    Exp valueExpression = Exp();
                    lex.eat("SEMIC");
                    return new StmAssign(id, valueExpression);
                } else if (lex.tok().isType("LBR")) {
                    lex.eat("LBR");
                    List<Exp> args = new ArrayList<>();
                    if (!lex.tok().isType("RBR")) {
                        args.add(Exp());
                        while (lex.tok().isType("COMMA")) {
                            lex.eat("COMMA");
                            args.add(Exp());
                        }
                    }
                    lex.eat("RBR");
                    lex.eat("SEMIC");
                    return new StmCall(id, args);
                } else if (lex.tok().isType("LSQBR")) {

                    List<Exp> indices = new ArrayList<>();


                    lex.eat("LSQBR");

                    if (lex.tok().isType("RSQBR")) {
                        throw new ParseException(lex.tok(), "Expected expression inside array brackets, found ']'");
                    }
                    indices.add(Exp());
                    lex.eat("RSQBR");


                    while (lex.tok().isType("LSQBR")) {
                        lex.eat("LSQBR");
                        if (lex.tok().isType("RSQBR")) {
                            throw new ParseException(lex.tok(), "Expected expression inside array brackets, found ']'");
                        }
                        indices.add(Exp());
                        lex.eat("RSQBR");
                    }

                    lex.eat("ASSIGN");
                    Exp valueExpression = Exp();
                    lex.eat("SEMIC");

                    return new StmArrayAssign(id, indices, valueExpression);
                } else {
                    throw new ParseException(lex.tok(), "ASSIGN", "LBR", "LSQBR");
                }
            }
            case "RETURN": {
                lex.next();

                Exp returnValue = null;
                if (!lex.tok().isType("SEMIC")) {
                    returnValue = Exp();
                }
                lex.eat("SEMIC");
                return new StmReturn(returnValue);
            }
            case "IF": {
                lex.next();
                lex.eat("LBR");
                Exp e = Exp();
                lex.eat("RBR");
                Stm trueBranch = Stm();
                lex.eat("ELSE");
                Stm falseBranch = Stm();
                return new StmIf(e, trueBranch, falseBranch);
            }
            case "WHILE": {
                lex.next();
                lex.eat("LBR");
                Exp e = Exp();
                lex.eat("RBR");
                return new StmWhile(e, Stm());
            }
            case "PRINT": {
                lex.next();
                Exp e = Exp();
                lex.eat("SEMIC");
                return new StmPrint(e);
            }
            case "PRINTLN": {
                lex.next();
                Exp e = Exp();
                lex.eat("SEMIC");
                return new StmPrintln(e);
            }
            case "PRINTCH": {
                lex.next();
                Exp e = Exp();
                lex.eat("SEMIC");
                return new StmPrintChar(e);
            }
            case "NEWLINE": {
                lex.next();
                lex.eat("SEMIC");
                return new StmNewline();
            }
            case "LCBR": {
                lex.next();
                List<Stm> stms = new LinkedList<>();
                while (!lex.tok().isType("RCBR")) {
                    stms.add(Stm());
                }
                lex.eat("RCBR");
                return new StmBlock(stms);
            }
            case "SWITCH": {
                lex.next();
                lex.eat("LBR");
                Exp caseExp = Exp();
                lex.eat("RBR");
                lex.eat("LCBR");
                List<StmSwitch.Case> cases = new ArrayList<>();
                while (!lex.tok().isType("DEFAULT")) {
                    cases.add(SwitchCase());
                }
                lex.eat("DEFAULT");
                lex.eat("COLON");
                Stm defaultCase = Stm();
                lex.eat("RCBR");
                return new StmSwitch(caseExp, defaultCase, cases);
            }
            default:
                throw new ParseException(lex.tok(), "ID", "RETURN", "IF", "WHILE", "PRINT", "PRINTLN", "PRINTCH", "NEWLINE", "LCBR", "SWITCH");
        }
    }

    private StmSwitch.Case SwitchCase() {
        lex.eat("CASE");
        int caseNumber = SignedInt();
        lex.eat("COLON");
        Stm stm = Stm();
        return new StmSwitch.Case(caseNumber, stm);
    }

    private int SignedInt() {
        return OptionalSign() * ParseInt();
    }

    private int ParseInt() {
        String intStr = lex.eat("INTLIT");

        try {

            if (intStr.length() > 1 && intStr.charAt(0) == '0') {

                return Integer.parseInt(intStr, 8);
            } else {

                return Integer.parseInt(intStr);
            }
        } catch (NumberFormatException e) {

            if (intStr.length() > 1 && intStr.charAt(0) == '0') {

                return Integer.parseInt(intStr, 10);
            }

            throw e;
        }
    }

    private int OptionalSign() {
        switch (lex.tok().type) {
            case "MINUS":
                lex.next();
                return -1;
            default:
                return 1;
        }
    }

    private Exp Exp() {
        Exp e1 = SimpleExp();
        return OperatorClause(e1);
    }

    private Exp SimpleExp() {
        switch (lex.tok().type) {
            case "ID": {
                String id = lex.tok().image;
                lex.next();

                // array access, function call, or length access
                if (lex.tok().isType("LSQBR")) {

                    List<Exp> indices = new ArrayList<>();


                    lex.eat("LSQBR");

                    if (lex.tok().isType("RSQBR")) {
                        throw new ParseException(lex.tok(), "Expected expression inside array brackets, found ']'");
                    }
                    indices.add(Exp());
                    lex.eat("RSQBR");

                    while (lex.tok().isType("LSQBR")) {
                        lex.eat("LSQBR");
                        if (lex.tok().isType("RSQBR")) {
                            throw new ParseException(lex.tok(), "Expected expression inside array brackets, found ']'");
                        }
                        indices.add(Exp());
                        lex.eat("RSQBR");
                    }

                    // Check for .length
                    if (lex.tok().isType("DOT")) {
                        lex.eat("DOT");
                        lex.eat("LENGTH");
                        return new ExpArrayLength(id, indices);
                    }

                    return new ExpArrayAccess(id, indices);

                } else if (lex.tok().isType("LBR")) {

                    lex.eat("LBR");

                    List<Exp> args = new ArrayList<>();
                    if (!lex.tok().isType("RBR")) {
                        args.add(Exp());
                        while (lex.tok().isType("COMMA")) {
                            lex.eat("COMMA");
                            args.add(Exp());
                        }
                    }
                    lex.eat("RBR");
                    return new ExpCall(id, args);
                } else if (lex.tok().isType("DOT")) {
                    lex.eat("DOT");
                    lex.eat("LENGTH");
                    return new ExpArrayLength(id, new ArrayList<>());
                } else {

                    return new ExpVar(id);
                }
            }
            case "MINUS", "INTLIT": {
                return new ExpInt(SignedInt());
            }
            case "NOT": {
                lex.next();
                return new ExpNot(SimpleExp());
            }
            case "LBR": {
                lex.next();
                Exp e = Exp();
                lex.eat("RBR");
                return e;
            }
            case "NULL": {
                lex.next();
                return new ExpNull();
            }
            case "NEW": {
                lex.next();


                if (lex.tok().isType("INT_TYPE")) {
                    lex.next();

                    // array dimensions
                    List<Exp> dimensions = new ArrayList<>();
                    lex.eat("LSQBR");
                    if (!lex.tok().isType("RSQBR")) {
                        dimensions.add(Exp());
                    } else {
                        dimensions.add(new ExpNull());
                    }
                    lex.eat("RSQBR");

                    while (lex.tok().isType("LSQBR")) {
                        lex.eat("LSQBR");
                        if (!lex.tok().isType("RSQBR")) {
                            dimensions.add(Exp());
                        } else {
                            dimensions.add(new ExpNull());
                        }
                        lex.eat("RSQBR");
                    }

                    return new ExpNewArray(dimensions);
                } else {
                    throw new ParseException(lex.tok(), "INT_TYPE");
                }
            }
            default:
                throw new ParseException(lex.tok(), "ID", "MINUS", "INTLIT", "NOT", "NULL", "NEW", "LBR");
        }
    }

    private Exp OperatorClause(Exp e) {
        switch (lex.tok().type) {
            case "MUL": {
                lex.next();
                return new ExpTimes(e, SimpleExp());
            }
            case "DIV": {
                lex.next();
                return new ExpDiv(e, SimpleExp());
            }
            case "MINUS": {
                lex.next();
                return new ExpMinus(e, SimpleExp());
            }
            case "ADD": {
                lex.next();
                return new ExpPlus(e, SimpleExp());
            }
            case "LT": {
                lex.next();
                return new ExpLessThan(e, SimpleExp());
            }
            case "LE": {
                lex.next();
                return new ExpLessThanEqual(e, SimpleExp());
            }
            case "EQ": {
                lex.next();
                return new ExpEqual(e, SimpleExp());
            }
            case "AND": {
                lex.next();
                return new ExpAnd(e, SimpleExp());
            }
            case "OR": {
                lex.next();
                return new ExpOr(e, SimpleExp());
            }
            default:
                return e;
        }
    }

    /**
     * Parse and pretty-print an LPL source file specified
     * by a command line argument.
     * @param args command-line arguments
     * @throws ParseException if the source file contains syntax errors
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: parse.LPLParser <source-file>");
            System.exit(1);
        }
        LPLParser parser = new LPLParser();
        Program program = parser.parse(args[0]);
        System.out.println(program);
    }
}