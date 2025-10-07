package net.iczelia.bawk.type;

import net.bytebuddy.description.type.TypeDescription;

import java.util.List;

public class FunctionType implements Type {
    public Type returnType;
    public List<Type> paramTypes;

    public FunctionType(List<Type> list, Type returnType) {
        this.paramTypes = list;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("fn(");
        for (int i = 0; i < paramTypes.size(); i++) {
            sb.append(paramTypes.get(i));
            if (i < paramTypes.size() - 1) sb.append(", ");
        }
        sb.append(") -> ").append(returnType);
        return sb.toString();
    }

    @Override
    public boolean compatible(Type another) {
        if (!(another instanceof FunctionType ft)) return false;
        if (!this.returnType.compatible(ft.returnType)) return false;
        if (this.paramTypes.size() != ft.paramTypes.size()) return false;
        for (int i = 0; i < this.paramTypes.size(); i++) {
            if (!this.paramTypes.get(i).compatible(ft.paramTypes.get(i))) return false;
        }
        return true;
    }

    @Override
    public Class<?> getJavaType() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getJavaTypeString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public TypeDescription getTypeDescription() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public HierarchyComparison compareHierarchy(Type another) {
        if (this.equals(another)) return HierarchyComparison.SAME;
        return HierarchyComparison.UNRELATED;
    }
}
