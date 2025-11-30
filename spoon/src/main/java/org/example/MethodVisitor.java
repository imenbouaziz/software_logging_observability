package org.example;

import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;

import java.util.ArrayList;
import java.util.List;

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
                String log;
                if ("UNKNOWN".equals(action)){
                    log = "logger.info(\"ACTION UNKNOWN\")";

                } else if (methodName.startsWith("fetch")) {
                    log = "logger.info(\"ACTION | userId={} | action={} | method={} | expensiveCount={} | totalExpensiveProducts={}\", "
                            + "userId, \"" + action + "\", \"" + methodName + "\", expensiveCount, 10)";
                }
                else{
                    log = "logger.info(\"ACTION | userId={} | action={} | method={} \", "
                            + "userId, \"" + action + "\", \"" + methodName + "\")";

                }
                CtCodeSnippetStatement snippet = factory.Code().createCodeSnippetStatement(log);

                boolean inserted = false;
                List<CtStatement> statements = new ArrayList<>(method.getBody().getStatements());
                for (CtStatement stmt : statements) {
                    if (stmt instanceof CtReturn<?>) {
                        stmt.insertBefore(snippet);
                        inserted = true;
                    }
                }

                if (!inserted) {
                    method.getBody().insertEnd(snippet);
                }



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
        if(methodName.startsWith("fetch") || methodName.startsWith("login"))return "READ";
        if(methodName.startsWith("add") || methodName.startsWith("update") || methodName.startsWith("delete") || methodName.startsWith("register")) return "WRITE";
        return "UNKNOWN";
    }
}
