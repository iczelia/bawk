package net.iczelia.bawk.codegen;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.hir.expr.*;
import net.iczelia.bawk.hir.jvmcg.CodeGeneratorRegistry;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.stmt.*;
import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class CodeGenBase implements ByteCodeAppender {
    public MethodVisitor mv;
    public int nextLocal;
    public Scope scope;
    public JVMEnvironment env;
    public String currentClassName;
    public HashMap<Class<?>, JVMCodeGenerator<?>> registry = CodeGeneratorRegistry.getRegistry();
    public HFnDecl currentFnDecl;
    public net.bytebuddy.jar.asm.Label tailLoopStart;

    public static Method getPrintln(Class<?> paramType) {
        try {
            return java.io.PrintStream.class.getMethod("println", paramType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void emitStmt(HStmt n) {
        if (registry.containsKey(n.getClass())) {
            @SuppressWarnings("unchecked")
            JVMCodeGenerator<HStmt> gen = (JVMCodeGenerator<HStmt>) registry.get(n.getClass());
            gen.visit(n, this);
        } else {
            throw new IllegalStateException("Statement type " + n.getClass().getSimpleName() + " has no JVM code generator registered");
        }
    }

    public void emitExpr(HExpr e) {
        if (registry.containsKey(e.getClass())) {
            @SuppressWarnings("unchecked")
            JVMCodeGenerator<HExpr> gen = (JVMCodeGenerator<HExpr>) registry.get(e.getClass());
            gen.visit(e, this);
        } else {
            throw new IllegalStateException("Expression type " + e.getClass().getSimpleName() + " has no JVM code generator registered");
        }
    }

    public void loadLValue(HLValue lv) {
        if (lv instanceof HVar v) {
            Slot s = require(v.var.name());
            load(s.index, s.type);
        } else if (lv instanceof HArrayIndexing ai) {
            emitExpr(ai.arrayExpr);
            emitExpr(ai.indexExpr);
            Type at = ai.arrayExpr.type;
            if (at instanceof ArrayType at2) {
                if (at2.elementType == PrimitiveType.I32) {
                    mv.visitInsn(Opcodes.IALOAD);
                } else if (at2.elementType == PrimitiveType.STR) {
                    mv.visitInsn(Opcodes.AALOAD);
                } else {
                    mv.visitInsn(Opcodes.AALOAD);
                }
            } else {
                throw new IllegalStateException("Expected array type, got: " + at);
            }
        } else {
            throw new IllegalStateException("bad lvalue: " + lv.getClass().getSimpleName());
        }
    }

    public void storeLValue(HLValue lv) {
        if (lv instanceof HVar v) {
            Slot s = require(v.var.name());
            store(s.index, s.type);
        } else if (lv instanceof HArrayIndexing ai) {
            emitExpr(ai.arrayExpr);
            mv.visitInsn(Opcodes.DUP_X1);
            mv.visitInsn(Opcodes.POP);
            emitExpr(ai.indexExpr);
            mv.visitInsn(Opcodes.DUP_X1);
            mv.visitInsn(Opcodes.POP);
            Type at = ai.arrayExpr.type;
            if (at instanceof ArrayType at2) {
                if (at2.elementType == PrimitiveType.I32) {
                    mv.visitInsn(Opcodes.IASTORE);
                } else if (at2.elementType == PrimitiveType.STR) {
                    mv.visitInsn(Opcodes.AASTORE);
                } else {
                    mv.visitInsn(Opcodes.AASTORE);
                }
            } else {
                throw new IllegalStateException("Expected array type, got: " + at);
            }
        } else {
            throw new IllegalStateException("bad lvalue: " + lv.getClass().getSimpleName());
        }
    }

    public int allocLocal(String name, Type t) {
        if (scope.map.containsKey(name)) throw new IllegalStateException("redeclaration: " + name);
        int idx = nextLocal;
        nextLocal += 1;
        scope.map.put(name, new Slot(idx, t));
        return idx;
    }

    public Slot require(String name) {
        Slot s = scope.lookup(name);
        if (s == null) throw new IllegalStateException("unknown variable: " + name);
        return s;
    }

    public void store(int idx, Type t) {
        if (t == PrimitiveType.I32) mv.visitVarInsn(Opcodes.ISTORE, idx);
        else mv.visitVarInsn(Opcodes.ASTORE, idx);
    }

    public void load(int idx, Type t) {
        if (t == PrimitiveType.I32) mv.visitVarInsn(Opcodes.ILOAD, idx);
        else mv.visitVarInsn(Opcodes.ALOAD, idx);
    }

    public void defaultInit(Type t) {
        if (t == PrimitiveType.I32) IntegerConstant.forValue(0).apply(mv, null);
        else mv.visitInsn(Opcodes.ACONST_NULL);
    }

    public void enterScope() {
        scope = new Scope(scope);
    }

    public void exitScope() {
        scope = scope.parent;
    }

    public void emitIntBoolFromCompare(int jumpOpcode) {
        Label TRUE = new Label(), DONE = new Label();
        mv.visitJumpInsn(jumpOpcode, TRUE);
        mv.visitLdcInsn(0);
        mv.visitJumpInsn(Opcodes.GOTO, DONE);
        mv.visitLabel(TRUE);
        mv.visitLdcInsn(1);
        mv.visitLabel(DONE);
    }

    public static final class Slot {
        public final int index;
        public final Type type;

        public Slot(int i, Type t) {
            index = i;
            type = t;
        }
    }

    public static final class Scope {
        public final Map<String, Slot> map = new HashMap<>();
        final Scope parent;

        Scope(Scope p) {
            parent = p;
        }

        public Slot lookup(String n) {
            for (Scope s = this; s != null; s = s.parent) {
                var v = s.map.get(n);
                if (v != null) return v;
            }
            return null;
        }
    }
}
