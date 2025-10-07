package net.iczelia.bawk.codegen;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.pool.TypePool;

class EnableFramesComputing implements AsmVisitorWrapper {
    @Override
    public final int mergeWriter(int flags) {
        return flags | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public final int mergeReader(int flags) {
        return flags | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public final ClassVisitor wrap(TypeDescription td, ClassVisitor cv, Implementation.Context ctx, TypePool tp, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int wflags, int rflags) {
        return cv;
    }
}
