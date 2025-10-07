package net.iczelia.bawk.hir.jvmcg.expr;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.codegen.CodeGenBase;
import net.iczelia.bawk.hir.expr.HExpr;
import net.iczelia.bawk.hir.expr.HFnCall;
import net.iczelia.bawk.hir.expr.HIfElseExpr;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.type.Type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class HFnCallJVMCodeGenerator implements JVMCodeGenerator<HFnCall> {
    @Override
    public void visit(HFnCall fc, CodeGenBase context) {
        String methodName = fc.function.name();
        List<TypeDescription> paramDescs = Arrays.stream(fc.args)
                .map(a -> a.type)
                .map(Type::getTypeDescription).toList();
        MethodDescription callTarget = context.env.declaredMethods.stream()
                .filter(m -> m.getName().equals(methodName)
                        && m.getParameters().size() == paramDescs.size()
                        && IntStream.range(0, paramDescs.size())
                        .allMatch(i -> m.getParameters().get(i).getType().asErasure().equals(paramDescs.get(i))))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Method not found: " + methodName));
        for (HExpr arg : fc.args) context.emitExpr(arg);
        MethodInvocation.invoke(callTarget).apply(context.mv, null);
        if (fc.type == null) {
            // push i32(0) for void functions to keep stack balanced
            context.mv.visitLdcInsn(0);
        }
    }
}
