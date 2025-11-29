package org.example;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.factory.Factory;

public class MethodVisitor {
    private final Factory factory;
    public MethodVisitor(Factory factory) {
        this.factory = factory;
    }

    public void visit(CtType<?> type) {
        for(CtMethod<?> method : type.getMethods()) {
            if(method.getBody() != null) {
                String action = determineAction(method.getSimpleName());
                String methodName = method.getSimpleName();
                String log = "logger.info(\"ACTION | userId={} | action=" + action + " | method=" + methodName + "\")";
                CtCodeSnippetStatement snippet = factory.Code().createCodeSnippetStatement(log);
                method.getBody().insertBegin(snippet);

            }
        }
    }

    private String determineAction(String methodName) {
        if(methodName.startsWith("get")|| methodName.startsWith("fetch")) return "READ";
        if(methodName.startsWith("set") || methodName.startsWith("add") || methodName.startsWith("update") || methodName.startsWith("delete")) return "WRITE";
        return "UNKNOWN";
    }
}
