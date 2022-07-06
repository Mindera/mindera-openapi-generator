/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindera.openapi.generator;

public class Swift5ClientCodegen extends org.openapitools.codegen.languages.Swift5ClientCodegen {

    public Swift5ClientCodegen() {
        super();

        embeddedTemplateDir = templateDir = "mindera-swift5";

        this.typeMapping.put("date", "DateWithoutTime");
        this.typeMapping.put("Date", "DateWithoutTime");
        this.typeMapping.put("DateTime", "DateWithTime");
        this.typeMapping.put("DateWithTime", "Date");
    }

    @Override
    public String getName() {
        return "mindera-swift5";
    }
}
