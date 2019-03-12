/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindera.openapi.generator.swift4;

import com.mindera.openapi.generator.Swift4Generator;
import io.swagger.models.Swagger;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.openapitools.codegen.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;


public class Swift4GeneratorTest {

    Swift4Generator swiftCodegen = new Swift4Generator();

    @Test(enabled = true)
    public void testCapitalizedReservedWord() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("AS", null), "_as");
    }

    @Test(enabled = true)
    public void testReservedWord() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("Public", null), "_public");
    }

    @Test(enabled = true)
    public void shouldNotBreakNonReservedWord() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("Error", null), "error");
    }

    @Test(enabled = true)
    public void shouldNotBreakCorrectName() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("EntryName", null), "entryName");
    }

    @Test(enabled = true)
    public void testSingleWordAllCaps() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("VALUE", null), "value");
    }

    @Test(enabled = true)
    public void testSingleWordLowercase() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("value", null), "value");
    }

    @Test(enabled = true)
    public void testCapitalsWithUnderscore() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("ENTRY_NAME", null), "entryName");
    }

    @Test(enabled = true)
    public void testCapitalsWithDash() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("ENTRY-NAME", null), "entryName");
    }

    @Test(enabled = true)
    public void testCapitalsWithSpace() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("ENTRY NAME", null), "entryName");
    }

    @Test(enabled = true)
    public void testLowercaseWithUnderscore() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("entry_name", null), "entryName");
    }

    @Test(enabled = true)
    public void testStartingWithNumber() throws Exception {
        Assert.assertEquals(swiftCodegen.toEnumVarName("123EntryName", null), "_123entryName");
        Assert.assertEquals(swiftCodegen.toEnumVarName("123Entry_name", null), "_123entryName");
        Assert.assertEquals(swiftCodegen.toEnumVarName("123EntryName123", null), "_123entryName123");
    }

    @Test(description = "returns Data when response format is binary", enabled = true)
    public void binaryDataTest() {
        // TODO update json file

        final OpenAPI openAPI = new OpenAPIParser().readLocation("src/test/resources/binaryDataTest.json", null, new ParseOptions()).getOpenAPI();
        final DefaultCodegen codegen = new Swift4Generator();
        final String path = "/tests/binaryResponse";
        final Operation p = openAPI.getPaths().get(path).getPost();
        final CodegenOperation op = codegen.fromOperation(path, "post", p, openAPI.getComponents().getSchemas());

        Assert.assertEquals(op.returnType, "URL");
        Assert.assertEquals(op.bodyParam.dataType, "URL");
        Assert.assertTrue(op.bodyParam.isBinary);
        Assert.assertTrue(op.responses.get(0).isBinary);
    }

    @Test(description = "returns Date when response format is date", enabled = true)
    public void dateTest() {
        final OpenAPI openAPI = new OpenAPIParser().readLocation("src/test/resources/datePropertyTest.json", null, new ParseOptions()).getOpenAPI();
        final DefaultCodegen codegen = new Swift4Generator();
        final String path = "/tests/dateResponse";
        final Operation p = openAPI.getPaths().get(path).getPost();
        final CodegenOperation op = codegen.fromOperation(path, "post", p, openAPI.getComponents().getSchemas());

        Assert.assertEquals(op.returnType, "DateWithoutTime");
        Assert.assertEquals(op.bodyParam.dataType, "DateWithoutTime");
    }

    @Test
    public void testPetstoreGeneratedCode() throws Exception {
        final File folder = new File("tmp");

        try {
            testGeneratedCodeCompilation("src/test/resources/petstore.json", folder);
            File order = new File(folder, "/OpenAPIClient/Classes/OpenAPIs/Models/Order.swift");
            Assert.assertTrue(order.exists());
        } finally {
            FileUtils.deleteDirectory(folder);
        }
    }

    @Test(enabled = false)
    public void testSmartboxGeneratedCode() throws Exception {
        final File folder = new File("tmp");

        try {
            testGeneratedCodeCompilation("src/test/resources/smartbox.yml", folder);
            File model = new File(folder, "/OpenAPIClient/Classes/OpenAPIs/Models/VoucherDetails.swift");
            Assert.assertTrue(model.exists());
        } finally {
            //FileUtils.deleteDirectory(folder);
        }
    }

    private void testGeneratedCodeCompilation(String apiLocation, final File folder) throws Exception {
        final OpenAPI openAPI = new OpenAPIParser().readLocation(apiLocation, null, new ParseOptions()).getOpenAPI();
        final DefaultCodegen codegenConfig = new Swift4Generator();

        codegenConfig.additionalProperties().put("responseAs", "RxSwift");
        codegenConfig.setOutputDir(folder.getAbsolutePath());

        ClientOptInput clientOptInput = (new ClientOptInput())
                .opts(new ClientOpts())
                .openAPI(openAPI)
                .config(codegenConfig);
        (new DefaultGenerator()).opts(clientOptInput).generate();
    }
}
