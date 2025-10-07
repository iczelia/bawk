package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.HierarchyComparison;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

import java.util.List;
import java.util.stream.Stream;

public class ArrayLit extends Expr {
    private final List<Expr> initializers;
    private final Type explicitType; // Can be null if not explicitly specified

    public ArrayLit(List<Expr> initializers, Type explicitType) {
        this.initializers = initializers;
        this.explicitType = explicitType;
    }

    public List<Expr> getInitializers() {
        return initializers;
    }

    @Override
    protected Type tryInfer(TypeEnvironment env) {
        Stream<Type> types = initializers.stream().map(x -> x.getInferredType(env));
        if (explicitType instanceof ArrayType at) {
            // every type must be compatible with explicitType.
            if (!types.allMatch(x -> x.compatible(at.elementType))) {
                env.errors.add(new ASTError("Array literal initializer types are not compatible with explicit type"));
            }
            return explicitType;
        } else if (explicitType == null) {
            // Take unique initializer types. If any pair is unrelated, then error. Otherwise pick the eldest type.
            List<Type> uniqueTypes = types.distinct().toList();
            if (uniqueTypes.isEmpty()) {
                // Empty array literal. Default to int[].
                env.errors.add(new ASTError("Cannot infer type of empty array literal."));
                return PrimitiveType.I32; // Return something to continue processing.
            } else if (uniqueTypes.size() == 1) {
                return new ArrayType(uniqueTypes.getFirst());
            } else {
                // More than one unique type. Check if they are all compatible with each other.
                for (int i = 0; i < uniqueTypes.size(); i++) {
                    for (int j = i + 1; j < uniqueTypes.size(); j++) {
                        if (uniqueTypes.get(i).compareHierarchy(uniqueTypes.get(j)) == HierarchyComparison.UNRELATED) {
                            env.errors.add(new ASTError("Array literal initializer types are not compatible with each other"));
                            return PrimitiveType.I32; // Return something to continue processing.
                        }
                    }
                }
                // All types are compatible. Pick the eldest type.
                Type eldest = uniqueTypes.getFirst();
                for (Type t : uniqueTypes) {
                    if (eldest.compareHierarchy(t) == HierarchyComparison.YOUNGER) {
                        eldest = t;
                    }
                }
                return new ArrayType(eldest);
            }
        } else {
            env.errors.add(new ASTError("Array literal has non-array explicit type"));
            return null;
        }
    }
}
