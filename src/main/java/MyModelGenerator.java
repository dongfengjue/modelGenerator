import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chenbing
 * @date 2017-12-14 12:08
 */
public class MyModelGenerator extends BaseModelGenerator {

    public Map<String,String> myMap;

    public void setMyMap(Map<String,String> map){
        this.myMap = map;
    }

    public MyModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
        super(baseModelPackageName, baseModelOutputDir);
    }

    public void setDaoModelTemplate(String daoModelTemplate) {
        super.setTemplate(daoModelTemplate);
    }

    @Override
    public void generate(List<TableMeta> tableMetas) {
        System.out.println("Generate base model ...");
        System.out.println("Base Model Output Dir: " + this.baseModelOutputDir);
        Engine engine = Engine.create("forBaseModel");
        engine.setSourceFactory(new ClassPathSourceFactory());
        engine.addSharedMethod(new StrKit());
        engine.addSharedObject("getterTypeMap", this.getterTypeMap);
        engine.addSharedObject("javaKeyword", this.javaKeyword);
        Iterator var3 = tableMetas.iterator();

        while(var3.hasNext()) {
            TableMeta tableMeta = (TableMeta)var3.next();
            this.genBaseModelContent(tableMeta);
        }

        this.writeToFile(tableMetas);
    }

    @Override
    protected void genBaseModelContent(TableMeta tableMeta) {
        Kv data = Kv.by("baseModelPackageName", this.baseModelPackageName);
        data.set("generateChainSetter", this.generateChainSetter);
        data.set("tableMeta", tableMeta);
        data.set("myMap", this.myMap);
        Engine engine = Engine.use("forBaseModel");
        tableMeta.baseModelContent = engine.getTemplate(this.template).renderToString(data);
    }
}
