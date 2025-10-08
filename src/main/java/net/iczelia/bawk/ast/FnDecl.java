package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.FunctionType;
import net.iczelia.bawk.type.Type;

import java.util.List;

public class FnDecl extends Statement {
    private final String name;
    private final Type returnType;
    private final List<FnParam> params;
    private final AST body;

    public FnDecl(AST body, List<FnParam> params, Type returnType, String name) {
        this.body = body;
        this.params = params;
        this.returnType = returnType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<FnParam> getParams() {
        return params;
    }

    public AST getBody() {
        return body;
    }

    private Type type = null;

    public Type getType() {
        if (type == null) {
            this.type = new FunctionType(params.stream().map(p -> p.type).toList(), returnType);
        }
        return this.type;
    }

    @Override
    protected void check() {
        // Transform tail self-recursive calls before type checks inside body.
        Type ft = getType();
        if (!typeEnv.sym.declare(name, ft, typeEnv.errors)) {
            // error already added
            return;
        }
        typeEnv.sym.enter(); // function scope
        for (FnDecl.FnParam p : params) {
            if (!typeEnv.sym.declare(p.name, p.type, typeEnv.errors)) {
                return; // don't check body if param declaration failed
            }
        }
        if (body instanceof Block block) {
            for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
        } else if (body instanceof ExprBlock block) {
            for (Statement s : block.getStatements()) s.checkConstraints(typeEnv);
            if (block.getFinalExpr() != null) block.getFinalExpr().getInferredType(typeEnv);
        } else if (body != null) {
            typeEnv.errors.add(new ASTError("function body must be block or expr-block"));
        }
        typeEnv.sym.exit();
    }

    public record FnParam(String name, Type type) {
    }
}
