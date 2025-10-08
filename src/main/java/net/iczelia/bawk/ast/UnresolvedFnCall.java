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
        // Collect argument types
        List<Type> argTypes = args.stream().map(a -> a.getInferredType(env)).toList();
        if (argTypes.contains(null)) {
            env.errors.add(new ASTError("cannot infer type of one or more arguments in call to '" + name + "'"));
            return null;
        }
        // Lookup function by name and argument types
        var sym = env.sym.lookup(name, argTypes);
        if (!(sym instanceof FunctionType fn)) {
            env.errors.add(new ASTError("no matching overload for function '" + name + "' with argument types " + argTypes));
            return null;
        }
        this.referredType = fn;
        // Check argument count (should always match if overload found)
        if (fn.paramTypes.size() != args.size()) {
            env.errors.add(new ASTError("function '" + name + "' expects " + fn.paramTypes.size() + " arguments, got " + args.size()));
            return fn.returnType;
        }
        // Check argument compatibility
        for (int i = 0; i < args.size(); i++) {
            var at = argTypes.get(i);
            var pt = fn.paramTypes.get(i);
            if (!at.compatible(pt)) {
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
