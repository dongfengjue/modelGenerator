import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenbing
 * @date 2017-12-14 9:37
 */
public class GeneratorMain {
    public static DataSource getDataSource() {
        PropKit.use("a_little_config.txt");
        DruidPlugin druidPlugin = createDruidPlugin();
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

    public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPackageName = "com.bwoil.newc2b.api.daos";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/bwoil/newc2b/api/daos";

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "com.bwoil.newc2b.api.types";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/bwoil/newc2b/api/types";

        // 这里可以自定义 模版
        Map<String,String> map = new HashMap<String, String>();

        map.put("aaa","test");
        map.put("modelPackageName",modelPackageName);

        // 创建生成器
        Generator generator = new MyGenerator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir,map);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(false);
        // 添加不需要生成的表名
        generator.addExcludedTable("adv");
        // 设置是否在 Model 中生成 dao 对象
        generator.setGenerateDaoInModel(false);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(false);
        // 设置是否生成字典文件
        generator.setGenerateDataDictionary(false);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        generator.setRemovedTableNamePrefixes("t_");

        generator.setBaseModelTemplate("jf/dao_template.jf");
        generator.setModelTemplate("./jf/model_template.jf");
        // 生成
        generator.generate();
    }
}
