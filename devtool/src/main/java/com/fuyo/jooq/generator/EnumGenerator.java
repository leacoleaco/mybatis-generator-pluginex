package com.fuyo.jooq.generator;

import com.fuyo.utils.S;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.Database;
import org.jooq.meta.SchemaDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumGenerator extends JavaGenerator {
    private static final String ENUMS_SCHEMA = "enums";

    private static final Logger log = LoggerFactory.getLogger(EnumGenerator.class);


    @Override
    protected void generateSchema(SchemaDefinition schema) {
        // Apply custom logic only for `enums` schema. Others schema has regular generation
        if (!schema.getName().equals(ENUMS_SCHEMA)) {
            super.generateSchema(schema);
            return;
        }

        log.info("Generating enums");
        log.info("----------------------------------------------------------");

        Database db = schema.getDatabase();

        db.getTables(schema).forEach(
                (table) -> {
                    // Prepare enum name from snake_case to CamelCase
                    String enumName = table.getName();

                    JavaWriter writer = newJavaWriter(new File(getFile(schema).getParentFile(), enumName + ".java"));
                    log.info("Generating enum: {}.java [input={}, output={}]", enumName, table.getName(), enumName);

                    printPackage(writer, schema);

                    writer.println("public enum $enumName {");

                    try {

                        ResultSet rs = db.getConnection().prepareStatement(
                                S.format("SELECT * FROM ${schema}.\"${tableName}\"",
                                        p -> p.set("schema", schema)
                                                .set("tableName", table.getName())
                                )).executeQuery();

                        while (true) {
                            if (!rs.next()) break;

                            // Generate enum entry
                            String name = rs.getString("name");
                            String description = rs.getString("description");
                            String s = rs.isLast() ? ";" : ",";
                            writer.tab(1).println(S.format("${name}(\"${description}\")${s}",
                                    p -> p.set("name", name)
                                            .set("description", description)
                                            .set("s", s)));
                        }

                        writer.println("|    private final String description;\n" +
                                       "|\n" +
                                       "|    private " + enumName + "(String description) {\n" +
                                       "|        this.description = description;\n" +
                                       "|    }\n" +
                                       "|}\n");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        closeJavaWriter(writer);
                    }
                }
        );

        log.info("----------------------------------------------------------");
        super.generateSchema(schema);
    }
}