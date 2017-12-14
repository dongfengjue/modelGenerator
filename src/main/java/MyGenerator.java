import com.jfinal.plugin.activerecord.generator.*;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author chenbing
 * @date 2017-12-14 10:25
 */
public class MyGenerator extends Generator {

    public MyMetaBuilder myMetaBuilder;
    protected MyModelGenerator myModelGenerator;

    public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir, Map<String,String> map) {
        super(dataSource, new BaseModelGenerator(baseModelPackageName,baseModelOutputDir), new ModelGenerator(modelPackageName, baseModelPackageName,modelOutputDir));
        this.myModelGenerator = new MyModelGenerator(baseModelPackageName,baseModelOutputDir);
        this.myModelGenerator.setMyMap(map);
        this.myMetaBuilder = new MyMetaBuilder(dataSource);
    }

    @Override
    public void setBaseModelTemplate(String baseModelTemplate) {
        this.myModelGenerator.setTemplate(baseModelTemplate);
    }

    @Override
    public void generate() {
        if (this.dialect != null) {
            myMetaBuilder.setDialect(this.dialect);
        }

        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = myMetaBuilder.build();
        if (tableMetas.size() == 0) {
            System.out.println("TableMeta 数量为 0，不生成任何文件");
        } else {
            this.myModelGenerator.generate(tableMetas);
            if (this.modelGenerator != null) {
                this.modelGenerator.generate(tableMetas);
            }

            long usedTime = (System.currentTimeMillis() - start) / 1000L;
            System.out.println("Generate complete in " + usedTime + " seconds.");
        }
    }


}
