package net.iczelia.bawk.hir.jvmcg.expr;

import net.iczelia.bawk.hir.expr.*;
import net.iczelia.bawk.hir.expr.binary.*;
import net.iczelia.bawk.hir.expr.unary.*;
import net.iczelia.bawk.hir.jvmcg.JVMCodeGenerator;
import net.iczelia.bawk.hir.transform.HTailSelfCall;

import java.util.HashMap;

public class ExprCodeGenerationRegistry {
    public static HashMap<Class<?>, JVMCodeGenerator<?>> createRegistry() {
        HashMap<Class<?>, JVMCodeGenerator<?>> registry = new HashMap<>();
        registry.put(HAddExpr.class, new HAddExprJVMCodeGenerator());
        registry.put(HSubExpr.class, new HSubExprJVMCodeGenerator());
        registry.put(HI32Lit.class, new HI32LitJVMCodeGenerator());
        registry.put(HStrLit.class, new HStrLitJVMCodeGenerator());
        registry.put(HVar.class, new HVarJVMCodeGenerator());
        registry.put(HUnaryPlus.class, new HUnaryPlusJVMCodeGenerator());
        registry.put(HExprBlock.class, new HExprBlockJVMCodeGenerator());
        registry.put(HLtExpr.class, new HComparisonExprJVMCodeGenerator());
        registry.put(HLeExpr.class, new HComparisonExprJVMCodeGenerator());
        registry.put(HGtExpr.class, new HComparisonExprJVMCodeGenerator());
        registry.put(HGeExpr.class, new HComparisonExprJVMCodeGenerator());
        registry.put(HAssignExpr.class, new HAssignExprJVMCodeGenerator());
        registry.put(HArrayAlloc.class, new HArrayAllocJVMCodeGenerator());
        registry.put(HUnaryMinus.class, new HUnaryMinusJVMCodeGenerator());
        registry.put(HUnaryNot.class, new HUnaryNotJVMCodeGenerator());
        registry.put(HUnaryTally.class, new HUnaryTallyJVMCodeGenerator());
        registry.put(HMulExpr.class, new HMulExprJVMCodeGenerator());
        registry.put(HDivExpr.class, new HDivExprJVMCodeGenerator());
        registry.put(HTailSelfCall.class, new HTailSelfCallJVMCodeGenerator());
        registry.put(HPreIncExpr.class, new HArithmeticPostfixExprJVMCodeGenerator());
        registry.put(HPostIncExpr.class, new HArithmeticPostfixExprJVMCodeGenerator());
        registry.put(HPreDecExpr.class, new HArithmeticPostfixExprJVMCodeGenerator());
        registry.put(HPostDecExpr.class, new HArithmeticPostfixExprJVMCodeGenerator());
        registry.put(HIfElseExpr.class, new HIfElseExprJVMCodeGenerator());
        registry.put(HEqExpr.class, new HEqExprJVMCodeGenerator());
        registry.put(HNeqExpr.class, new HNeqExprJVMCodeGenerator());
        registry.put(HArrayLit.class, new HArrayLitJVMCodeGenerator());
        registry.put(HArrayIndexing.class, new HArrayIndexingJVMCodeGenerator());
        registry.put(HFnCall.class, new HFnCallJVMCodeGenerator());
        registry.put(HF32Lit.class, new HF32LitJVMCodeGenerator());
        registry.put(HInstanceOfCheck.class, new HInstanceOfCheckJVMCodeGenerator());
        return registry;
    }
}
