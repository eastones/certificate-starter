package org.apichat.common.modules.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 基础工具包
 *
 * @param
 * @author easton
 * @date 2020/4/21 20:30
 * @return null
 */
public class BaseUtils {

    /**
     * 获取InputStream保存成文件
     *
     * @return java.lang.String
     * @author easton
     * @date 2022/4/21 20:31
     */
    public synchronized String InputStreamAsSave(String source_file, String cert_dir, String cert_file) {

        String certFile_path = cert_dir.concat(cert_file);
        // 公钥文件位置()
        String jar_package_file = source_file;//"/certs/pdp_public_certs.keystore";
        // 公钥临时目录,暂存公钥文件.

        try {
            //获取文件()
            InputStream inputStream = this.getClass().getResourceAsStream(jar_package_file);

            //定义新目录
            File dir = new File(cert_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 创建新文件
            File new_file = new File(cert_dir, cert_file);


            if (new_file.exists()) {
                new_file.deleteOnExit();
            }
            // 创建文件
            new_file.createNewFile();
            // 是否能写
            if (!new_file.canWrite()) {
                System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -0");
                System.exit(0);
            }
            FileOutputStream fileOut = new FileOutputStream(new_file);
            byte[] buf = new byte[1024 * 8];
            while (true) {
                int read = 0;
                if (inputStream != null) {
                    read = inputStream.read(buf);
                }
                if (read == -1) {
                    break;
                }
                fileOut.write(buf, 0, read);
            }
            // 查看文件获取是否成功
            if (!fileOut.getFD().valid()) {
                //System.out.println("Spring information certificate issuance unsuccessfull! 2");
                // System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -1");
                String timeStr1=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
                System.out.println(timeStr1 + " logger  [main] INFO  Java version:".concat(System.getProperty("java.version")).concat(",-1;"));
                System.exit(0);
                // System.out.println("获取文件失败");
            }
            fileOut.flush();
            fileOut.close();
            inputStream.close();
            if (new_file.exists() && new_file.length() > 5) {
                return certFile_path;
            } else {
                //System.out.println("Spring information certificate issuance unsuccessfull! 3");
                System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -2");
                System.exit(0);
            }
        } catch (Exception exception) {
            //System.err.println(exception.getMessage());
            // System.out.println("Spring information certificate issuance unsuccessfull! 1");
            System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -3");
            System.exit(0);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        String fp = File.separator;
//        System.out.println(System.getProperty("user.dir").concat(fp).concat("tmp").concat(fp).concat("pdp_certs.keystore"));
//        File file = new File("C:\\workspace\\public\\certificate-issuance\\tmp\\pdp_certs.keystore");
//
//        File  dir = new File("C:\\workspace\\public\\certificate-issuance\\tmp\\");
//
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//
//        if(!file.exists()){
//            file.createNewFile();
//        }
//
//        String filename = "mydirl.txt";
//        String directory = "mydir2/";
//        File f = new File(System.getProperty("user.dir"), filename);
//
//        if (file.exists()) {
//            System.out.println("文件已经存在！");
//        } else {
//
//            file.createNewFile();
//        }
    }
}
