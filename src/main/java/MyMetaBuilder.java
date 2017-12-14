import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenbing
 * @date 2017-12-14 10:42
 */
public class MyMetaBuilder extends MetaBuilder {
    public MyMetaBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String buildBaseModelName(String modelName) {
        return modelName + "Dao";
    }

    @Override
    public List<TableMeta> build() {
        System.out.println("Build TableMeta ...");

        try {
            this.conn = this.dataSource.getConnection();
            this.dbMeta = this.conn.getMetaData();
            List<TableMeta> ret = new ArrayList();
            this.buildTableNames(ret);
            Iterator var2 = ret.iterator();

            while(var2.hasNext()) {
                TableMeta tableMeta = (TableMeta)var2.next();
                this.buildPrimaryKey(tableMeta);
                this.buildColumnMetas(tableMeta);
            }

            ArrayList var13 = (ArrayList) ret;
            return var13;
        } catch (SQLException var11) {
            throw new RuntimeException(var11);
        } finally {
            if (this.conn != null) {
                try {
                    this.conn.close();
                } catch (SQLException var10) {
                    throw new RuntimeException(var10);
                }
            }

        }
    }

    @Override
    protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
        ResultSet rs = this.dbMeta.getPrimaryKeys(this.conn.getCatalog(), (String)null, tableMeta.name);
        String primaryKey = "";

        for(int var4 = 0; rs.next(); primaryKey = primaryKey + rs.getString("COLUMN_NAME")) {
            if (var4++ > 0) {
                primaryKey = primaryKey + ",";
            }
        }

        if (StrKit.isBlank(primaryKey)) {
//            throw new RuntimeException("primaryKey of table \"" + tableMeta.name + "\" required by active record pattern");
            tableMeta.primaryKey = "";
        } else {
            tableMeta.primaryKey = primaryKey;
        }

        rs.close();
    }
}
