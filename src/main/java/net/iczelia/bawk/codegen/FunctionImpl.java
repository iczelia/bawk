package net.iczelia.bawk.codegen;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.hir.expr.HExprBlock;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HFnDecl;
import net.iczelia.bawk.hir.stmt.HStmt;
import net.iczelia.bawk.type.PrimitiveType;

record FunctionImpl(HFnDecl fnDecl, String className, JVMEnvironment env) implements Implementation {
    @Override
    public ByteCodeAppender appender(Target implementationTarget) {
        return new App(fnDecl, className, env);
    }

    @Override
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    private static final class App extends CodeGenBase {
        private final HFnDecl fnDecl;
        private Label loopStart;

        App(HFnDecl fnDecl, String currentClassName, JVMEnvironment env) {
            this.fnDecl = fnDecl;
            this.currentClassName = currentClassName;
            this.env = env;
            this.currentFnDecl = fnDecl; // provide to base for TailSelfCall support
        }

        @Override
        public Size apply(MethodVisitor methodVisitor, Context implementationContext, MethodDescription instrumentedMethod) {
            this.mv = methodVisitor;
            this.nextLocal = 0;
            this.scope = new Scope(null);
            for (int i = 0; i < fnDecl.params.length; i++) {
                var param = fnDecl.params[i];
                scope.map.put(param.name(), new Slot(nextLocal++, param.type()));
            }
            mv.visitCode();
            // mark loop start for tail self-call optimization
            loopStart = new Label();
            mv.visitLabel(loopStart);
            this.tailLoopStart = loopStart;

            if (fnDecl.body instanceof HBlock block) {
                for (HStmt s : block.stmts) emitStmt(s);
            } else if (fnDecl.body instanceof HExprBlock eb) {
                for (HStmt s : eb.precStmts) emitStmt(s);
                emitExpr(eb.finalExpr);
            } else {
                throw new IllegalStateException("Function body is neither a block nor an expression block: " + fnDecl.body.getClass().getName());
            }
            // depending on the return type, emit the appropriate return
            if (fnDecl.returnType == null) {
                mv.visitInsn(Opcodes.RETURN);
            } else if (fnDecl.returnType == PrimitiveType.I32) {
                mv.visitInsn(Opcodes.IRETURN);
            } else if (fnDecl.returnType == PrimitiveType.F32) {
                mv.visitInsn(Opcodes.FRETURN);
            } else if (fnDecl.returnType == PrimitiveType.STR) {
                mv.visitInsn(Opcodes.ARETURN);
            } else {
                mv.visitInsn(Opcodes.ARETURN);
            }
            mv.visitEnd();
            return new Size(-1, nextLocal);
        }
    }
}
