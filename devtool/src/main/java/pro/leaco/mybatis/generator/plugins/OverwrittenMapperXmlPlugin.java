package pro.leaco.mybatis.generator.plugins;

import java.io.File;

import pro.leaco.mybatis.generator.plugins.utils.BasePlugin;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverwrittenMapperXmlPlugin extends BasePlugin {
    private static final Logger log = LoggerFactory.getLogger(OverwrittenMapperXmlPlugin.class);

    public OverwrittenMapperXmlPlugin() {
    }

    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        String dir = sqlMap.getTargetPackage();
        if (dir.contains(".")) {
            dir = dir.replace(".", File.separator);
        }

        String fileName = sqlMap.getTargetProject() + File.separator + dir + File.separator + sqlMap.getFileName();
        File file = new File(fileName);
        if (file.exists()) {
            log.warn("Existing file {}  was overwritten ", file);
            if (!file.delete()) {
                log.warn("覆盖原有xml文件: {} 失败!", fileName);
            }
        }

        super.sqlMapGenerated(sqlMap, introspectedTable);
        return true;
    }
}
