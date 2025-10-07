package net.iczelia.bawk.codegen;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.jar.asm.Opcodes;
import net.iczelia.bawk.hir.stmt.HFnDecl;
import net.iczelia.bawk.hir.stmt.HProgram;
import net.iczelia.bawk.hir.stmt.HStmt;

import java.util.Arrays;
import java.util.Collections;

public final class CodeGen {
    private final HProgram program;
    private final JVMEnvironment env;

    public CodeGen(HProgram program) {
        this.program = program;
        this.env = new JVMEnvironment();
    }

    private DynamicType.Builder<?> baseBuilder(String className) {
        DynamicType.Builder<?> builder = new ByteBuddy(ClassFileVersion.JAVA_V8)
                .subclass(Object.class)
                .visit(new EnableFramesComputing())
                .name(className)
                .defineMethod("main", void.class, Visibility.PUBLIC, Ownership.STATIC)
                .withParameters(String[].class)
                .intercept(new ApplicationImpl(program, className, env));

        for (HStmt s : program.statements) {
            if (s instanceof HFnDecl fn) {
                var retType = fn.returnType != null ? fn.returnType.getJavaType() : void.class;
                var argTypes = Arrays.asList(fn.params).stream()
                        .map(p -> p.type() != null ? p.type().getJavaType() : int.class).toList();
                var argTokens = argTypes.stream()
                        .map(TypeDescription.Generic.OfNonGenericType.ForLoadedType::of)
                        .map(t -> new ParameterDescription.Token(t, Collections.emptyList())).toList();
                TypeDescription declaringType = new TypeDescription.Latent(
                        className, // fully qualified name
                        Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER,
                        null, // no superclass
                        Collections.emptyList() // interfaces
                );
                MethodDescription methodDesc = new MethodDescription.Latent(
                        declaringType,
                        fn.name.name(),
                        Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
                        Collections.emptyList(), // type variables
                        TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(retType),
                        argTokens,
                        Collections.emptyList(), // exceptions
                        Collections.emptyList(), // annotations
                        null, // default value
                        null  // receiver type
                );
                env.declaredMethods.add(methodDesc);
                builder = builder.defineMethod(fn.name.name(), retType, Visibility.PUBLIC, Ownership.STATIC)
                        .withParameters(argTypes).intercept(new FunctionImpl(fn, className, env));
            }
        }
        return builder;
    }

    public byte[] generateBytes(String className) {
        return baseBuilder(className).make().getBytes();
    }

    public Class<?> loadAndDefine(String className) {
        return baseBuilder(className)
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }
}
