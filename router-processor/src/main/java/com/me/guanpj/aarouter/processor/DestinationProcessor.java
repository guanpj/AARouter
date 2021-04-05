package com.me.guanpj.aarouter.processor;

import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.me.guanpj.aarouter.annotations.Destination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {
    private static final String TAG = "DestinationProcessor";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Destination.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        System.out.println(TAG + " >>> process start ...");

        Set<Element> elementsAnnotatedWith = (Set<Element>) roundEnv.getElementsAnnotatedWith(Destination.class);

        String rootDir = processingEnv.getOptions().get("root_project_dir");

        String className = "RouterMapping_" + System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("package com.me.guanpj.aarouter.mapping;\n\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n\n");
        sb.append("public class ").append(className).append(" {\n");
        sb.append("    public static Map<String, String> get() {\n");
        sb.append("        Map<String, String> mapping = new HashMap<>();\n");

        if (elementsAnnotatedWith.size() < 1) {
            return false;
        }

        JsonArray destJsonArr = new JsonArray();

        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            Destination destination = typeElement.getAnnotation(Destination.class);

            String url = destination.url();
            String description = destination.description();
            String realPath = typeElement.getQualifiedName().toString();

            sb.append(String.format("        mapping.put(\"%s\", \"%s\");\n", url, realPath));

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("url", url);
            jsonObject.addProperty("description", description);
            jsonObject.addProperty("realPath", realPath);

            destJsonArr.add(jsonObject);

            System.out.println(TAG + " >>> url = " + url);
            System.out.println(TAG + " >>> description = " + description);
            System.out.println(TAG + " >>> realPath = " + realPath);
        }

        sb.append("        return mapping;\n");
        sb.append("    }\n");
        sb.append("}\n");

        String fullName = "com.me.guanpj.aarouter.mapping." + className;
        try {
            JavaFileObject object = processingEnv.getFiler().createSourceFile(fullName);
            Writer writer = object.openWriter();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File rootFile = new File(rootDir);
        if (!rootFile.exists()) {
            throw new RuntimeException("root project dir no exists!");
        }

        File mappingFileDir = new File(rootFile, "router_mapping");
        if (!mappingFileDir.exists()) {
            mappingFileDir.mkdir();
        }

        File mappingFile = new File(mappingFileDir, "mapping_" + System.currentTimeMillis() + ".json");

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mappingFile));
            bufferedWriter.write(destJsonArr.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(TAG + " >>> process finish ...");

        return false;
    }
}
