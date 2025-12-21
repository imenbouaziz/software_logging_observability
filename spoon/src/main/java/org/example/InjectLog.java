package org.example;

import org.example.visitor.ClassVisitor;
import org.example.visitor.MethodVisitor;
import spoon.Launcher;
import spoon.reflect.CtModel;

public class InjectLog {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("/home/imene/IdeaProjects/software_logging_observability/backend/src/main/java/org/example");
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setAutoImports(true);
        launcher.buildModel();

        CtModel model = launcher.getModel();

        MethodVisitor methodVisitor = new MethodVisitor(launcher.getFactory());
        ClassVisitor classVisitor = new ClassVisitor(methodVisitor);
        model.getAllTypes().forEach(classVisitor::visit);

        launcher.prettyprint();
    }
}
