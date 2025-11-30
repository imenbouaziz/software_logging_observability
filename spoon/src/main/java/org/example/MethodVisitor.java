package org.example;

import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class MethodVisitor {
    private final Factory factory;
    public MethodVisitor(Factory factory) {
        this.factory = factory;
    }

    public void visit(CtType<?> type) {
        ensureLoggerField(type);
        for (CtMethod<?> method : type.getMethods()) {
            if (method.getBody() != null) {
                String action = determineAction(method.getSimpleName());
                String methodName = method.getSimpleName();
                String log = "logger.info(\"ACTION | userId={} | action={} | method={}\", id, \""
                        + action + "\", \"" + methodName + "\")";
                CtCodeSnippetStatement snippet = factory.Code().createCodeSnippetStatement(log);
                method.getBody().insertBegin(snippet);
            }
        }
    }

    private void ensureLoggerField(CtType<?> type) {
        if (type.getField("logger") != null) return;

        var loggerType = factory.Type().createReference("org.slf4j.Logger");
        String newQualifiedName = type.getQualifiedName()
                .replace("org.example", "org.example.logs_tp_backend_spooned");
        var loggerField = factory.createField(
                type,
                java.util.EnumSet.of(spoon.reflect.declaration.ModifierKind.PRIVATE,
                        spoon.reflect.declaration.ModifierKind.STATIC,
                        spoon.reflect.declaration.ModifierKind.FINAL),
                loggerType,
                "logger",
                factory.Code().createCodeSnippetExpression(
                        "org.slf4j.LoggerFactory.getLogger(" + newQualifiedName + ".class)"
                )
        );
    }
    private String determineAction(String methodName) {
        if(methodName.startsWith("get")|| methodName.startsWith("fetch")) return "READ";
        if(methodName.startsWith("set") || methodName.startsWith("add") || methodName.startsWith("update") || methodName.startsWith("delete")) return "WRITE";
        return "UNKNOWN";
    }
}
