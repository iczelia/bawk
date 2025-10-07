package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.FunctionType;
import net.iczelia.bawk.type.Type;

import java.util.List;

public class UnresolvedFnCall extends Expr {
    private final String name;
    private final List<Expr> args;
    private Type referredType = null;

    public UnresolvedFnCall(String name, List<Expr> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        // lookup name in env
        var sym = env.sym.lookup(name);
        if (!(sym instanceof FunctionType fn)) {
            env.errors.add(new ASTError("'" + name + "' does not name a function"));
            return null;
        }
        this.referredType = fn;
        if (fn.paramTypes.size() != args.size()) {
            env.errors.add(new ASTError("function '" + name + "' expects " + fn.paramTypes.size() + " arguments, got " + args.size()));
            return fn.returnType;
        }
        for (int i = 0; i < args.size(); i++) {
            var at = args.get(i).getInferredType(env);
            var pt = fn.paramTypes.get(i);
            if (at == null) {
                env.errors.add(new ASTError("cannot infer type of argument " + (i + 1) + " in call to '" + name + "'"));
            } else if (!at.compatible(pt)) {
                env.errors.add(new ASTError("type mismatch for argument " + (i + 1) + " in call to '" + name + "': expected " + pt + ", got " + at));
            }
        }
        return fn.returnType;
    }

    public String getName() {
        return name;
    }

    public List<Expr> getArgs() {
        return args;
    }

    public Type getReferredType() {
        return referredType;
    }
}
