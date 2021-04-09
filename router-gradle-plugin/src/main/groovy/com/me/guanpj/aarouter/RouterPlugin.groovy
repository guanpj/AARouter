package com.me.guanpj.aarouter

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        if (project.plugins.hasPlugin(AppPlugin)) {
            AppExtension appExtension = project.extensions.getByType(AppExtension)
            Transform transform = new RouterMappingTransform()
            appExtension.registerTransform(transform)
        }

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

        if (!project.plugins.hasPlugin(AppPlugin)) {
            return
        }

        project.getExtensions().create("router", RouterExtension)

        project.afterEvaluate {
            RouterExtension extension = project["router"]
            println("rootDir:$extension.wikiDir")

            project.tasks.findAll {
                it.name.startsWith("compile") && it.name.endsWith("JavaWithJavac")
            }.each {
                it.doLast {
                    File mappingFileDir = new File(project.rootProject.projectDir, "router_mapping")
                    if (!mappingFileDir.exists()) {
                        return
                    }

                    File[] jsonFileArr = mappingFileDir.listFiles()
                    if (jsonFileArr.length < 1) {
                        return
                    }

                    StringBuilder markdownBuilder = new StringBuilder()

                    markdownBuilder.append("# 页面文档\n\n")


                    jsonFileArr.each { child ->
                        if (child.name.endsWith(".json")) {
                            JsonSlurper jsonSlurper = new JsonSlurper()
                            def content = jsonSlurper.parse(child)

                            content.each { innerContent ->
                                def url = innerContent['url']
                                def description = innerContent['description']
                                def realPath = innerContent['realPath']

                                markdownBuilder.append("## $description \n")
                                markdownBuilder.append("- url: $url \n")
                                markdownBuilder.append("- realPath: $realPath \n\n")
                            }
                        }
                    }

                    File wikiFileDir = new File(extension.wikiDir)
                    if (!wikiFileDir.exists()) {
                        wikiFileDir.mkdir()
                    }

                    File wikiFile = new File(wikiFileDir, "页面文档.md")
                    if (wikiFile.exists()) {
                        wikiFile.delete()
                    }

                    wikiFile.write(markdownBuilder.toString())
                }
            }
        }
    }
}