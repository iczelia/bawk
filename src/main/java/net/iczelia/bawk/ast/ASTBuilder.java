package net.iczelia.bawk.ast;

import net.iczelia.bawk.type.ArrayType;
import net.iczelia.bawk.type.PrimitiveType;
import net.iczelia.bawk.type.Type;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends GrammarBaseVisitor<ParseResult> {
    @Override
    public ParseResult visitProgram(GrammarParser.ProgramContext ctx) {
        Program p = new Program();
        for (var child : ctx.children) {
            if (child instanceof GrammarParser.StmtContext || child instanceof GrammarParser.DeclContext) {
                p.statements.add((Statement) visit(child));
            }
        }
        return p;
    }

    @Override
    public ParseResult visitFnDecl(GrammarParser.FnDeclContext ctx) {
        var params = new ArrayList<FnDecl.FnParam>();
        for (var paramCtx : ctx.fn_arg()) {
            params.add(new FnDecl.FnParam(paramCtx.ID().getText(), (Type) visit(paramCtx.type())));
        }
        return new FnDecl(
                (ExprBlock) visit(ctx.exprBlock()),
                params,
                ctx.type() != null ? (Type) visit(ctx.type()) : null,
                ctx.ID().getText()
        );
    }

    @Override
    public ParseResult visitProcDecl(GrammarParser.ProcDeclContext ctx) {
        var params = new ArrayList<FnDecl.FnParam>();
        for (var paramCtx : ctx.fn_arg()) {
            params.add(new FnDecl.FnParam(paramCtx.ID().getText(), (Type) visit(paramCtx.type())));
        }
        return new FnDecl(
                (Block) visit(ctx.block()),
                params,
                null,
                ctx.ID().getText()
        );
    }

    @Override
    public ParseResult visitLetDecl(GrammarParser.LetDeclContext ctx) {
        return new LetDecl(
                ctx.ID().getText(),
                ctx.type() != null ? (Type) visit(ctx.type()) : null,
                ctx.expr() != null ? (Expr) visit(ctx.expr()) : null
        );
    }

    @Override
    public ParseResult visitBase_type(GrammarParser.Base_typeContext ctx) {
        String typeName = ctx.getText();
        return switch (typeName) {
            case "i32" -> PrimitiveType.I32;
            case "f32" -> PrimitiveType.F32;
            case "str" -> PrimitiveType.STR;
            default -> throw new RuntimeException("Unknown type: " + typeName);
        };
    }

    @Override
    public ParseResult visitArrayType(GrammarParser.ArrayTypeContext ctx) {
        return new ArrayType((Type) visit(ctx.type()));
    }

    @Override
    public ParseResult visitBaseType(GrammarParser.BaseTypeContext ctx) {
        return visit(ctx.base_type());
    }

    @Override
    public ParseResult visitPrintStmt(GrammarParser.PrintStmtContext ctx) {
        return new Print((Expr) visit(ctx.expr()));
    }

    @Override
    public ParseResult visitWhileStmt(GrammarParser.WhileStmtContext ctx) {
        return new While((Expr) visit(ctx.expr()), (Statement) visit(ctx.stmt()));
    }

    @Override
    public ParseResult visitIfStmt(GrammarParser.IfStmtContext ctx) {
        IfStmt i = new IfStmt();
        i.cond = (Expr) visit(ctx.expr());
        i.yes = (Statement) visit(ctx.stmt(0));
        if (ctx.stmt().size() > 1) {
            i.no = (Statement) visit(ctx.stmt(1));
        }
        return i;
    }

    @Override
    public ParseResult visitAssign(GrammarParser.AssignContext ctx) {
        return new AssignExpr((LValue) visit(ctx.lvalue()), (Expr) visit(ctx.expr()));
    }

    private AssignExpr createCompoundAssign(GrammarParser.LvalueContext leftCtx, GrammarParser.ExprContext rightCtx, String op) {
        var target = (LValue) visit(leftCtx);
        ArithmeticBinOp b = new ArithmeticBinOp(op, target, (Expr) visit(rightCtx));
        return new AssignExpr(target, b);
    }

    @Override
    public ParseResult visitAddAssign(GrammarParser.AddAssignContext ctx) {
        return createCompoundAssign(ctx.lvalue(), ctx.expr(), "+");
    }

    @Override
    public ParseResult visitSubAssign(GrammarParser.SubAssignContext ctx) {
        return createCompoundAssign(ctx.lvalue(), ctx.expr(), "-");
    }

    @Override
    public ParseResult visitMulAssign(GrammarParser.MulAssignContext ctx) {
        return createCompoundAssign(ctx.lvalue(), ctx.expr(), "*");
    }

    @Override
    public ParseResult visitDivAssign(GrammarParser.DivAssignContext ctx) {
        return createCompoundAssign(ctx.lvalue(), ctx.expr(), "/");
    }

    @Override
    public ParseResult visitIfElse(GrammarParser.IfElseContext ctx) {
        return new IfElseExpr(
                (Expr) visit(ctx.expr(0)),
                (Expr) visit(ctx.expr(1)),
                (Expr) visit(ctx.expr(2))
        );
    }

    @Override
    public ParseResult visitEq(GrammarParser.EqContext ctx) {
        Expr e = (Expr) visit(ctx.relationalExpr(0));
        for (int i = 1; i < ctx.relationalExpr().size(); i++) {
            e = new ArithmeticBinOp(ctx.getChild(2 * i - 1).getText(), e, (Expr) visit(ctx.relationalExpr(i)));
        }
        return e;
    }

    @Override
    public ParseResult visitCmp(GrammarParser.CmpContext ctx) {
        Expr e = (Expr) visit(ctx.additiveExpr(0));
        for (int i = 1; i < ctx.additiveExpr().size(); i++) {
            e = new ArithmeticBinOp(ctx.getChild(2 * i - 1).getText(), e, (Expr) visit(ctx.additiveExpr(i)));
        }
        return e;
    }

    @Override
    public ParseResult visitAddSub(GrammarParser.AddSubContext ctx) {
        Expr e = (Expr) visit(ctx.multiplicativeExpr(0));
        for (int i = 1; i < ctx.multiplicativeExpr().size(); i++) {
            e = new ArithmeticBinOp(ctx.getChild(2 * i - 1).getText(), e, (Expr) visit(ctx.multiplicativeExpr(i)));
        }
        return e;
    }

    @Override
    public ParseResult visitMulDiv(GrammarParser.MulDivContext ctx) {
        Expr e = (Expr) visit(ctx.unaryExpr(0));
        for (int i = 1; i < ctx.unaryExpr().size(); i++) {
            e = new ArithmeticBinOp(ctx.getChild(2 * i - 1).getText(), e, (Expr) visit(ctx.unaryExpr(i)));
        }
        return e;
    }

    @Override
    public ParseResult visitPreIncDec(GrammarParser.PreIncDecContext ctx) {
        return new PreIncDec(
                ctx.getChild(0).getText(),
                (LValue) visit(ctx.lvalue())
        );
    }

    @Override
    public ParseResult visitUnaryOp(GrammarParser.UnaryOpContext ctx) {
        return new Unary(ctx.getChild(0).getText(), (Expr) visit(ctx.unaryExpr()));
    }

    @Override
    public ParseResult visitPostfix(GrammarParser.PostfixContext ctx) {
        Expr base = (Expr) visit(ctx.lvalue());
        for (int i = 1; i < ctx.getChildCount(); i++) {
            if (!(base instanceof LValue))
                throw new RuntimeException("Postfix op needs lvalue");
            String op = ctx.getChild(i).getText();
            base = new PostIncDec(op, (LValue) base);
        }
        return base;
    }

    @Override
    public ParseResult visitFnCallRule(GrammarParser.FnCallRuleContext ctx) {
        var args = new ArrayList<Expr>();
        for (var argCtx : ctx.expr()) {
            args.add((Expr) visit(argCtx));
        }
        return new UnresolvedFnCall(ctx.ID().getText(), args);
    }

    @Override
    public ParseResult visitConstInt(GrammarParser.ConstIntContext ctx) {
        return new I32Lit(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public ParseResult visitConstHex(GrammarParser.ConstHexContext ctx) {
        return new I32Lit(Integer.parseInt(ctx.HEX().getText().substring(2), 16));
    }

    @Override
    public ParseResult visitConstOct(GrammarParser.ConstOctContext ctx) {
        return new I32Lit(Integer.parseInt(ctx.OCT().getText().substring(2), 8));
    }

    @Override
    public ParseResult visitConstBin(GrammarParser.ConstBinContext ctx) {
        return new I32Lit(Integer.parseInt(ctx.BIN().getText().substring(2), 2));
    }

    @Override
    public ParseResult visitConstFloat(GrammarParser.ConstFloatContext ctx) {
        return new F32Lit(Float.parseFloat(ctx.FLOAT().getText()));
    }

    @Override
    public ParseResult visitConstStr(GrammarParser.ConstStrContext ctx) {
        String myValue = StringEscapeUtils.unescapeJava(ctx.STRING().getText());
        myValue = myValue.substring(1, myValue.length() - 1); // Unquote
        return new StrLit(myValue);
    }

    @Override
    public ParseResult visitParen(GrammarParser.ParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public ParseResult visitVar(GrammarParser.VarContext ctx) {
        return new UnresolvedVar(ctx.ID().getText());
    }

    @Override
    public ParseResult visitArrayAlloc(GrammarParser.ArrayAllocContext ctx) {
        return new ArrayAlloc((Expr) visit(ctx.expr()), (Type) visit(ctx.type()));
    }

    @Override
    public ParseResult visitArrayInitializer(GrammarParser.ArrayInitializerContext ctx) {
        List<Expr> initializers = new ArrayList<>();
        for (var exprCtx : ctx.expr()) {
            initializers.add((Expr) visit(exprCtx));
        }
        return new ArrayLit(initializers, ctx.type() != null ? (Type) visit(ctx.type()) : null);
    }

    @Override
    public ParseResult visitLValVar(GrammarParser.LValVarContext ctx) {
        return new UnresolvedVar(ctx.ID().getText());
    }

    @Override
    public ParseResult visitLValIndexing(GrammarParser.LValIndexingContext ctx) {
        return new ArrayIndexing((Expr) visit(ctx.lvalue()), (Expr) visit(ctx.expr()));
    }

    @Override
    public ParseResult visitExprStmt(GrammarParser.ExprStmtContext ctx) {
        return new ExprStmt((Expr) visit(ctx.expr()));
    }

    @Override
    public ParseResult visitBlock(GrammarParser.BlockContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (var child : ctx.children) {
            if (child instanceof GrammarParser.StmtContext || child instanceof GrammarParser.DeclContext) {
                statements.add((Statement) visit(child));
            }
        }
        return new Block(statements);
    }

    @Override
    public ParseResult visitExprBlock(GrammarParser.ExprBlockContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (var child : ctx.children) {
            if (child instanceof GrammarParser.StmtContext || child instanceof GrammarParser.DeclContext) {
                statements.add((Statement) visit(child));
            }
        }
        return new ExprBlock(statements, ctx.expr() != null ? (Expr) visit(ctx.expr()) : null);
    }

    @Override
    public ParseResult visitSimpleForStmt(GrammarParser.SimpleForStmtContext ctx) {
        return new SimpleFor(
                (Statement) visit(ctx.forInit()),
                (Expr) visit(ctx.forCondition().expr()),
                ctx.forUpdate() != null ? (Expr) visit(ctx.forUpdate().expr()) : null,
                (Statement) visit(ctx.forBody().stmt())
        );
    }

    @Override
    public ParseResult visitForInit(GrammarParser.ForInitContext ctx) {
        if (ctx.decl() != null) {
            return visit(ctx.decl());
        } else if (ctx.stmt() != null) {
            return visit(ctx.stmt());
        } else {
            return null; // empty init
        }
    }
}
