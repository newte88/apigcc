package com.github.ayz6uem.restdoc.springmvc;

import com.github.ayz6uem.restdoc.Enviroment;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import com.github.ayz6uem.restdoc.RestDoc;
import com.github.ayz6uem.restdoc.util.JsonHelper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class SpringTreeVisitorTest {

    @Test
    public void test1() throws IOException {

        String sourceFolder = System.getProperty("user.dir")+"/src/test/java";

        RestDoc restDoc = new RestDoc(Enviroment.builder().build());

        JavaParserTypeSolver sourceTypeSolver = new JavaParserTypeSolver(sourceFolder);
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(sourceTypeSolver);

        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(combinedTypeSolver));

        SourceRoot root = new SourceRoot(Paths.get(sourceFolder),parserConfiguration);
        root.tryToParse().forEach(result-> result.ifSuccessful(compilationUnit -> compilationUnit.accept(new SpringNodeVisitor(),restDoc.getTree())));

        System.out.println(JsonHelper.toPretty(restDoc.getTree()));
    }

}
