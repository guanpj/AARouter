package com.me.guanpj.aarouter

import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", project.rootProject.projectDir.absolutePath)
            }
        }

        project.clean.doFirst {
            File mappingFileDir = new File(project.rootProject.projectDir.absolutePath, "router_mapping")
            if (mappingFileDir.exists()) {
                mappingFileDir.deleteDir()
            }
        }

        project.getExtensions().create("router", RouterExtension)

        project.afterEvaluate {
            RouterExtension extension = project["router"]
            println("rootDir:$extension.wikiDir")
        }
    }
}