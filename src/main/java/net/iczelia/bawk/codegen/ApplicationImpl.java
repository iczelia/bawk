package net.iczelia.bawk.codegen;


import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.hir.stmt.HProgram;
import net.iczelia.bawk.hir.stmt.HStmt;

record ApplicationImpl(HProgram program, String className, JVMEnvironment env) implements Implementation {
    @Override
    public ByteCodeAppender appender(Target implementationTarget) {
        return new App(program, className, env);
    }

    @Override
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    private static final class App extends CodeGenBase {
        private final HProgram program;

        App(HProgram program, String className, JVMEnvironment env) {
            this.program = program;
            this.currentClassName = className;
            this.env = env;
        }

        @Override
        public Size apply(MethodVisitor methodVisitor, Context implementationContext, MethodDescription instrumentedMethod) {
            this.mv = methodVisitor;
            this.nextLocal = 1; // slot 0 = String[] args
            this.scope = new Scope(null);
            mv.visitCode();
            for (HStmt s : program.statements) emitStmt(s);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitEnd();
            return new Size(-1, nextLocal);
        }
    }
}
