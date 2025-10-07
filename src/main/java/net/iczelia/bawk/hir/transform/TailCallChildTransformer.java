package net.iczelia.bawk.hir.transform;

import net.iczelia.bawk.hir.HIRChildTransformer;
import net.iczelia.bawk.hir.HNode;
import net.iczelia.bawk.hir.expr.HExprBlock;
import net.iczelia.bawk.hir.expr.HFnCall;
import net.iczelia.bawk.hir.expr.HIfElseExpr;
import net.iczelia.bawk.hir.stmt.HBlock;
import net.iczelia.bawk.hir.stmt.HFnDecl;

public class TailCallChildTransformer implements HIRChildTransformer {
    private final TailCallBodyTransformer tailCallBodyTransformer = new TailCallBodyTransformer();
    private final TailCallTailExpressionTransformer tailCallTailExpressionTransformer = new TailCallTailExpressionTransformer();

    @Override
    public HNode transform(HNode child, HNode parent) {
        switch (child) {
            case null -> {
            } // Do nothing.
            case HFnDecl fnDecl -> fnDecl.accept(tailCallBodyTransformer);
            default -> child.accept(this);
        }
        ;
        return child;
    }

    private static class TailCallTailExpressionTransformer implements HIRChildTransformer {
        @Override
        public HNode transform(HNode child, HNode parent) {
            return switch (child) {
                case HIfElseExpr ifElseExpr -> {
                    // Children of IfElseExpr are in tail position if the IfElseExpr itself is.
                    ifElseExpr.accept(this);
                    yield ifElseExpr;
                }
                // Call at tail position, automatically transform.
                case HFnCall fnCall ->
                        new HTailSelfCall(fnCall.function, fnCall.args, fnCall.type, fnCall.translationUnitContext, fnCall.environmentContext);
                default -> child; // No transformation; stop traversal.
            };
        }
    }

    private class TailCallBodyTransformer implements HIRChildTransformer {
        @Override
        public HNode transform(HNode child, HNode parent) {
            return switch (child) {
                case HExprBlock exprBlock -> {
                    if (exprBlock.finalExpr != null) {
                        exprBlock.finalExpr.accept(tailCallTailExpressionTransformer);
                    }
                    yield exprBlock;
                }
                case HBlock block -> {
                    // Currently only the last statement of the block can be at tail position.
                    // If we had a return statement, it would be handled here.
                    if (block.stmts.length != 0) {
                        HNode lastStmt = block.stmts[block.stmts.length - 1];
                        lastStmt.accept(tailCallTailExpressionTransformer);
                    }
                    yield block;
                }
                default -> child; // No transformation; stop traversal.
            };
        }
    }
}
