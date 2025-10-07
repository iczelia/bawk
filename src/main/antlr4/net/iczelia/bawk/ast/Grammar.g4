grammar Grammar;

program
  : (stmt | decl)* EOF
  ;

fn_arg
  : ID (':' type)?
  ;

decl
  : 'let' ID (':' type)? ('=' expr)? ';' # LetDecl
  | 'fn' ID '(' (fn_arg (',' fn_arg)*)? ')' ':' type exprBlock # FnDecl
  | 'fn' ID '(' (fn_arg (',' fn_arg)*)? ')' block # ProcDecl
  ;

base_type
  : 'i32'
  | 'str'
  ;

type
  : type '[]' # ArrayType
  | base_type # BaseType
  ;

stmt
  : printStmt
  | whileStmt
  | simpleForStmt
  | ifStmt
  | block
  | exprStmt
  ;

printStmt
  : 'print' '(' expr ')' ';'
  ;

whileStmt
  : 'while' '(' expr ')' stmt
  ;

ifStmt
  : 'if' '(' expr ')' stmt ('else' stmt)?
  ;

forInit
  : stmt | decl | ';'
  ;

forBody
  : stmt
  ;

forCondition
  : expr
  ;

forUpdate
  : expr
  ;

simpleForStmt
  : 'for' '(' forInit forCondition ';' forUpdate? ')' forBody
  ;

block
  : '{' (stmt | decl)* '}'
  ;

exprBlock
  : '{' (stmt | decl)* expr '}'
  ;

expr
  : assignExpr
  ;

exprStmt
  : expr ';'
  ;

assignExpr
  : lvalue '=' expr                    # Assign
  | lvalue '+=' expr                   # AddAssign
  | lvalue '-=' expr                   # SubAssign
  | lvalue '*=' expr                   # MulAssign
  | lvalue '/=' expr                   # DivAssign
  | condExpr                           # Cond
  ;

condExpr
  : 'if' '(' expr ')' expr 'else' expr  # IfElse
  | logicalOrExpr                      # CondPass
  ;

logicalOrExpr
  : logicalAndExpr ('||' logicalAndExpr)* # Or
  ;

logicalAndExpr
  : equalityExpr ('&&' equalityExpr)*  # And
  ;

equalityExpr
  : relationalExpr (('=='|'!=') relationalExpr)* # Eq
  ;

relationalExpr
  : additiveExpr (('<'|'>'|'<='|'>=') additiveExpr)* # Cmp
  ;

// additive
additiveExpr
  : multiplicativeExpr (('+'|'-') multiplicativeExpr)* # AddSub
  ;

// multiplicative
multiplicativeExpr
  : unaryExpr (('*'|'/') unaryExpr)* # MulDiv
  ;

// unary operators
unaryExpr
  : ('++' | '--') lvalue              # PreIncDec
  | ('+'|'-'|'!'|'#') unaryExpr           # UnaryOp
  | postfixExpr                       # ToPostfix
  | primary                           # ToPrimary
  ;

// postfix operators
postfixExpr
  : lvalue ( '++' | '--' )*          # Postfix
  ;

fnCallRule
  : ID '(' (expr (',' expr)*)? ')'
  ;

// primaries: constants, vars, sub-expr
primary
  : '(' expr ')'                      # Paren
  | fnCallRule                        # FnCall
  | INT                               # ConstInt
  | STRING                            # ConstStr
  | ID                                # Var
  | type '[' expr ']'                 # ArrayAlloc
  | ('(' type ')')? '{' (expr (',' expr)*)? '}'       # ArrayInitializer
  ;

lvalue
  : ID                                # LValVar
  | '(' lvalue ')'                    # LValParen
  | lvalue ('[' expr ']')             # LValIndexing
  ;

// LEXER

ID      : [a-zA-Z_][a-zA-Z_0-9]* ;
INT     : [0-9]+ ;
STRING  : '"' ( '\\' . | ~["\\] )* '"' ;

WS      : [ \t\r\n]+ -> skip ;
LINE_CMT: '//' ~[\r\n]* -> skip ;
BLOCK_CMT: '/*' .*? '*/' -> skip ;
