package org.example.visitor;

import spoon.reflect.declaration.CtType;

public class ClassVisitor {
    private final MethodVisitor methodVisitor;

    public ClassVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void visit(CtType<?> type) {
        if (type.getSimpleName().contains("Controller")) {
            methodVisitor.visit(type);
        }
    }
}
