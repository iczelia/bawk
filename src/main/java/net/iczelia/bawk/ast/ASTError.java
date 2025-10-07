package net.iczelia.bawk.ast;

public class ASTError extends RuntimeException {
    public ASTError(String message) {
        super(message);
    }
}
