import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/11/30 20:20
 */
public class test {

    // 上传本地文件
    @Test
    public void uploadFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "vqBGz2goH0pPiQBzNgNmBTNnvYDd5PMwuyHPu09M";
        String secretKey = "2E9ltSJarIlfa1e83PSA2Axw3pI6hpYDggV5G0aC";
        String bucket = "tomato-health";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
        String localFilePath = "C:\\Users\\Hugh\\Desktop\\ee7dcf84-8a3a-4ab9-b981-9c5d272fd58d3.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
    // 删除空间中的文件
    @Test
    public void deleteFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        String accessKey = "liyKTcQC5TP1LrhgZH6Xem8zqMXbEtVgfAINP53w";
        String secretKey = "f5zpuzKAPceEMG77-EK3XbwqgOBRDXDawG4UHRtb";
        String bucket = "itcast_health";
        String key = "Fu3Ic6TV6wIbJt793yaGeBmCkzTX";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 1、使用POI可以从一个已经存在的Excel文件中读取数据
     */
    @Test
    public void exportExcel() throws IOException {
        //1、1、创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\Hugh\\Desktop\\hello.xlsx");
        //1、2、获取工作表对象，既可以根据工作表顺序获取，也可以根据工作表的名称获取workbook.getSheet("名字")
        XSSFSheet sheet = workbook.getSheetAt(0);


       //1、3、遍历工作表对象，获得行对象
        for (Row row : sheet) {
            //1、4、遍历行对象，获得列对象
            for (Cell cell : row) {
                //1、 f5、获得数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }

        }
        //6、关闭
        workbook.close();
    }
}
