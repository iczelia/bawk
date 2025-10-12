package net.iczelia.bawk.hir;

import net.iczelia.bawk.ast.*;
import net.iczelia.bawk.hir.expr.*;
import net.iczelia.bawk.hir.expr.binary.*;
import net.iczelia.bawk.hir.expr.unary.*;
import net.iczelia.bawk.hir.stmt.*;
import net.iczelia.bawk.type.Type;

public class HIRLowering {
    public HLetDecl lower(HXlatUnit unit, HEnv env, LetDecl letDecl) {
        if (letDecl.getName() == null || letDecl.getType() == null)
            throw new HLoweringException("letDecl name or type is null");
        HXlatUnit.VarToken token = new HXlatUnit.VarToken(letDecl.getName(), letDecl.getType());
        env.bindVar(letDecl.getName(), token);
        return new HLetDecl(token, lowerGeneric(unit, env, letDecl.getInit()), letDecl.getType(), unit, env);
    }

    public HFnDecl lower(HXlatUnit unit, HEnv env, FnDecl fnDecl) {
        if (fnDecl.getName() == null)
            throw new HLoweringException("fnDecl name is null");
        Type functionalType = fnDecl.getType();
        if (functionalType == null)
            throw new HLoweringException("fnDecl functional type is null");
        HXlatUnit.FnToken token = new HXlatUnit.FnToken(fnDecl.getName(), functionalType);
        env.bindFn(fnDecl.getName(), token);
        HNode newBody;
        HXlatUnit.VarToken[] newParams = new HXlatUnit.VarToken[fnDecl.getParams().size()];
        env.pushScope();
        for (int i = 0; i < fnDecl.getParams().size(); i++) {
            FnDecl.FnParam p = fnDecl.getParams().get(i);
            if (p.name() == null || p.type() == null)
                throw new HLoweringException("fnDecl param name or type is null");
            newParams[i] = new HXlatUnit.VarToken(p.name(), p.type());
            env.bindVar(p.name(), newParams[i]);
        }
        if (fnDecl.getBody() instanceof ExprBlock eb) {
            newBody = lowerGeneric(unit, env, eb);
        } else if (fnDecl.getBody() instanceof Block sb) {
            newBody = lowerGeneric(unit, env, sb);
        } else {
            throw new HLoweringException("fnDecl body is neither ExprBlock nor StmtBlock");
        }
        env.popScope();
        return new HFnDecl(token, fnDecl.getReturnType(), newParams, newBody, unit, env);
    }

    public HArrayAlloc lower(HXlatUnit unit, HEnv env, ArrayAlloc arrayAlloc) {
        if (arrayAlloc.getElementType() == null)
            throw new HLoweringException("arrayAlloc elemType is null");
        HExpr size = lowerGeneric(unit, env, arrayAlloc.getSizeExpr());
        return new HArrayAlloc(size, arrayAlloc.getInferredType(null), unit, env);
    }

    public HArrayIndexing lower(HXlatUnit unit, HEnv env, ArrayIndexing arrayIndexing) {
        HExpr array = lowerGeneric(unit, env, arrayIndexing.getArrayReference());
        HExpr index = lowerGeneric(unit, env, arrayIndexing.getArrayIndex());
        return new HArrayIndexing(array, index, arrayIndexing.getInferredType(null), unit, env);
    }

    public HArrayLit lower(HXlatUnit unit, HEnv env, ArrayLit arrayLit) {
        HExpr[] elements = new HExpr[arrayLit.getInitializers().size()];
        for (int i = 0; i < arrayLit.getInitializers().size(); i++) {
            elements[i] = lowerGeneric(unit, env, arrayLit.getInitializers().get(i));
        }
        return new HArrayLit(elements, arrayLit.getInferredType(null), unit, env);
    }

    public HAssignExpr lower(HXlatUnit unit, HEnv env, AssignExpr assignExpr) {
        HLValue target = (HLValue) lowerGeneric(unit, env, assignExpr.getTarget());
        HExpr value = lowerGeneric(unit, env, assignExpr.getValue());
        return new HAssignExpr(target, value, assignExpr.getInferredType(null), unit, env);
    }

    public HExpr lower(HXlatUnit unit, HEnv env, ArithmeticBinOp op) {
        HExpr left = lowerGeneric(unit, env, op.getLeft());
        HExpr right = lowerGeneric(unit, env, op.getRight());
        return switch (op.getOp()) {
            case "+" -> new HAddExpr(left, right, op.getInferredType(null), unit, env);
            case "-" -> new HSubExpr(left, right, op.getInferredType(null), unit, env);
            case "*" -> new HMulExpr(left, right, op.getInferredType(null), unit, env);
            case "/" -> new HDivExpr(left, right, op.getInferredType(null), unit, env);
            case "==" -> new HEqExpr(left, right, op.getInferredType(null), unit, env);
            case "!=" -> new HNeqExpr(left, right, op.getInferredType(null), unit, env);
            case "<" -> new HLtExpr(left, right, op.getInferredType(null), unit, env);
            case "<=" -> new HLeExpr(left, right, op.getInferredType(null), unit, env);
            case ">" -> new HGtExpr(left, right, op.getInferredType(null), unit, env);
            case ">=" -> new HGeExpr(left, right, op.getInferredType(null), unit, env);
            case "&&" -> new HAndExpr(left, right, op.getInferredType(null), unit, env);
            case "||" -> new HOrExpr(left, right, op.getInferredType(null), unit, env);
            default -> throw new HLoweringException("Unknown binary operator: " + op.getOp());
        };
    }

    public HBlock lower(HXlatUnit unit, HEnv env, Block block) {
        HStmt[] statements = new HStmt[block.getStatements().size()];
        env.pushScope();
        for (int i = 0; i < block.getStatements().size(); i++) {
            statements[i] = lowerGeneric(unit, env, block.getStatements().get(i));
        }
        env.popScope();
        return new HBlock(statements, unit, env);
    }

    public HExprBlock lower(HXlatUnit unit, HEnv env, ExprBlock exprBlock) {
        HStmt[] statements = new HStmt[exprBlock.getStatements().size()];
        env.pushScope();
        for (int i = 0; i < exprBlock.getStatements().size(); i++) {
            statements[i] = lowerGeneric(unit, env, exprBlock.getStatements().get(i));
        }
        HExpr finalExpr = lowerGeneric(unit, env, exprBlock.getFinalExpr());
        env.popScope();
        return new HExprBlock(statements, finalExpr, exprBlock.getInferredType(null), unit, env);
    }

    public HExprStmt lower(HXlatUnit unit, HEnv env, ExprStmt exprStmt) {
        HExpr expr = lowerGeneric(unit, env, exprStmt.getExpr());
        return new HExprStmt(expr, unit, env);
    }

    public HI32Lit lower(HXlatUnit unit, HEnv env, I32Lit i32Lit) {
        return new HI32Lit(i32Lit.getValue(), i32Lit.getInferredType(null), unit, env);
    }

    public HF32Lit lower(HXlatUnit unit, HEnv env, F32Lit f32Lit) {
        return new HF32Lit(f32Lit.getValue(), f32Lit.getInferredType(null), unit, env);
    }

    public HIfElseExpr lower(HXlatUnit unit, HEnv env, IfElseExpr ifElseExpr) {
        HExpr condition = lowerGeneric(unit, env, ifElseExpr.cond);
        HExpr yes = lowerGeneric(unit, env, ifElseExpr.yes);
        HExpr no = lowerGeneric(unit, env, ifElseExpr.no);
        return new HIfElseExpr(condition, yes, no, ifElseExpr.getInferredType(null), unit, env);
    }

    public HIfElseStmt lower(HXlatUnit unit, HEnv env, IfStmt ifElseStmt) {
        HExpr condition = lowerGeneric(unit, env, ifElseStmt.cond);
        HStmt yes = lowerGeneric(unit, env, ifElseStmt.yes);
        HStmt no = lowerGeneric(unit, env, ifElseStmt.no);
        return new HIfElseStmt(condition, yes, no, unit, env);
    }

    public HExpr lower(HXlatUnit unit, HEnv env, PreIncDec preIncDec) {
        HLValue target = (HLValue) lowerGeneric(unit, env, preIncDec.getTarget());
        return switch (preIncDec.getOp()) {
            case "++" -> new HPreIncExpr(target, preIncDec.getInferredType(null), unit, env);
            case "--" -> new HPreDecExpr(target, preIncDec.getInferredType(null), unit, env);
            default -> throw new HLoweringException("Unknown pre-inc-dec operator: " + preIncDec.getOp());
        };
    }

    public HExpr lower(HXlatUnit unit, HEnv env, PostIncDec postIncDec) {
        HLValue target = (HLValue) lowerGeneric(unit, env, postIncDec.getTarget());
        return switch (postIncDec.getOp()) {
            case "++" -> new HPostIncExpr(target, postIncDec.getInferredType(null), unit, env);
            case "--" -> new HPostDecExpr(target, postIncDec.getInferredType(null), unit, env);
            default -> throw new HLoweringException("Unknown post-inc-dec operator: " + postIncDec.getOp());
        };
    }

    public HPrint lower(HXlatUnit unit, HEnv env, Print printStmt) {
        HExpr expr = lowerGeneric(unit, env, printStmt.getValue());
        return new HPrint(expr, unit, env);
    }

    public HProgram lower(HXlatUnit unit, HEnv env, Program program) {
        HStmt[] statements = new HStmt[program.statements.size()];
        env.pushScope();
        for (int i = 0; i < program.statements.size(); i++) {
            statements[i] = lowerGeneric(unit, env, program.statements.get(i));
        }
        env.popScope();
        return new HProgram(statements, unit, env);
    }

    public HSimpleFor lower(HXlatUnit unit, HEnv env, SimpleFor simpleFor) {
        env.pushScope();
        HStmt init = lowerGeneric(unit, env, simpleFor.getInit());
        HExpr cond = lowerGeneric(unit, env, simpleFor.getCond());
        HExpr update = lowerGeneric(unit, env, simpleFor.getUpdate());
        HStmt body = lowerGeneric(unit, env, simpleFor.getBody());
        env.popScope();
        return new HSimpleFor(init, cond, update, body, unit, env);
    }

    public HStrLit lower(HXlatUnit unit, HEnv env, StrLit strLit) {
        return new HStrLit(strLit.getValue(), strLit.getInferredType(null), unit, env);
    }

    public HExpr lower(HXlatUnit unit, HEnv env, Unary unary) {
        HExpr expr = lowerGeneric(unit, env, unary.expr);
        return switch (unary.op) {
            case "-" -> new HUnaryMinus(expr, unary.getInferredType(null), unit, env);
            case "+" -> new HUnaryPlus(expr, unary.getInferredType(null), unit, env);
            case "!" -> new HUnaryNot(expr, unary.getInferredType(null), unit, env);
            case "#" -> new HUnaryTally(expr, unary.getInferredType(null), unit, env);
            default -> throw new HLoweringException("Unknown unary operator: " + unary.op);
        };
    }

    public HVar lower(HXlatUnit unit, HEnv env, UnresolvedVar var) {
        HXlatUnit.VarToken token = env.resolveVar(var.getName());
        if (token == null)
            throw new HLoweringException("Variable not found in environment: " + var.getName());
        return new HVar(token, var.getInferredType(null), unit, env);
    }

    public HFnCall lower(HXlatUnit unit, HEnv env, UnresolvedFnCall fnCall) {
        HXlatUnit.FnToken token = env.resolveFn(fnCall.getName());
        if (token == null)
            throw new HLoweringException("Function not found in environment: " + fnCall.getName());
        HExpr[] args = new HExpr[fnCall.getArgs().size()];
        for (int i = 0; i < fnCall.getArgs().size(); i++) {
            args[i] = lowerGeneric(unit, env, fnCall.getArgs().get(i));
        }
        return new HFnCall(token, args, fnCall.getInferredType(null), unit, env);
    }

    public HWhile lower(HXlatUnit unit, HEnv env, While whileStmt) {
        env.pushScope();
        HExpr condition = lowerGeneric(unit, env, whileStmt.getCond());
        HStmt body = lowerGeneric(unit, env, whileStmt.getBody());
        env.popScope();
        return new HWhile(condition, body, unit, env);
    }

    public HInstanceOfCheck lower(HXlatUnit unit, HEnv env, InstanceOfCheck ioc) {
        HExpr reference = lowerGeneric(unit, env, ioc.getReference());
        Type desiredType = ioc.getDesiredType();
        return new HInstanceOfCheck(reference, desiredType, ioc.getInferredType(null), unit, env);
    }

    private HExpr lowerGeneric(HXlatUnit unit, HEnv env, Expr init) {
        return switch (init) {
            case ArrayAlloc aa -> lower(unit, env, aa);
            case ArrayIndexing ai -> lower(unit, env, ai);
            case ArrayLit al -> lower(unit, env, al);
            case AssignExpr ae -> lower(unit, env, ae);
            case ArithmeticBinOp bo -> lower(unit, env, bo);
            case I32Lit i32 -> lower(unit, env, i32);
            case F32Lit f32 -> lower(unit, env, f32);
            case InstanceOfCheck ioc -> lower(unit, env, ioc);
            case IfElseExpr iee -> lower(unit, env, iee);
            case PreIncDec pid -> lower(unit, env, pid);
            case ExprBlock eb -> lower(unit, env, eb);
            case PostIncDec pod -> lower(unit, env, pod);
            case StrLit sl -> lower(unit, env, sl);
            case Unary u -> lower(unit, env, u);
            case UnresolvedVar uv -> lower(unit, env, uv);
            case UnresolvedFnCall ufc -> lower(unit, env, ufc);
            case null -> null;
            default -> throw new HLoweringException("Unhandled Expr type in lowering: " + init.getClass().getName());
        };
    }

    private HStmt lowerGeneric(HXlatUnit unit, HEnv env, Statement init) {
        return switch (init) {
            case LetDecl ld -> lower(unit, env, ld);
            case FnDecl fd -> lower(unit, env, fd);
            case Block b -> lower(unit, env, b);
            case ExprStmt es -> lower(unit, env, es);
            case IfStmt ies -> lower(unit, env, ies);
            case Print ps -> lower(unit, env, ps);
            case SimpleFor sf -> lower(unit, env, sf);
            case While w -> lower(unit, env, w);
            case null -> null;
            default ->
                    throw new HLoweringException("Unhandled Statement type in lowering: " + init.getClass().getName());
        };
    }
}
