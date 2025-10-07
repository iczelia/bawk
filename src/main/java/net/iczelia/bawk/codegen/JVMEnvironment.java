package net.iczelia.bawk.codegen;

import net.bytebuddy.description.method.MethodDescription;

import java.util.ArrayList;
import java.util.List;

public class JVMEnvironment {
    public final List<MethodDescription> declaredMethods = new ArrayList<>();
}
