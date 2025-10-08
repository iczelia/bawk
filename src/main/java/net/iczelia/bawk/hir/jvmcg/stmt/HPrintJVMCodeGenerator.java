package net.iczelia.bawk.hir.jvmcg.stmt;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.HLetDecl;
import net.iczelia.bawk.hir.stmt.HPrint;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

import java.lang.reflect.Method;

public class HPrintJVMCodeGenerator implements JVMCodeGenerator<HPrint> {
    @Override
    public void visit(HPrint node, CodeGenBase context) {
        FieldAccess.forField(new TypeDescription.ForLoadedType(System.class)
                        .getDeclaredFields().filter(f -> f.getName().equals("out")).getOnly())
                .read().apply(context.mv, null);
        context.emitExpr(node.expr);
        Method m;
        if (node.expr.type == PrimitiveType.I32) {
            m = CodeGenBase.getPrintln(int.class);
        } else if (node.expr.type == PrimitiveType.F32) {
            m = CodeGenBase.getPrintln(float.class);
        } else {
            m = CodeGenBase.getPrintln(String.class);
        }
        MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(m)).apply(context.mv, null);
    }
}
