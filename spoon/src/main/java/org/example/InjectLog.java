package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;

public class InjectLog {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("/home/imene/IdeaProjects/logs_tp/src/main/java/org/example");
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setAutoImports(true);
        launcher.buildModel();

        CtModel model = launcher.getModel();

        MethodVisitor visitor = new MethodVisitor(launcher.getFactory());
        model.getAllTypes().forEach(visitor::visit);

        launcher.prettyprint();
    }
}
