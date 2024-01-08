package org.apichart.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.apichart.common.modules.context.LicenseVerify;
import org.apichart.common.modules.entity.LicenseVerifyParam;
import org.apichart.common.modules.origin.HttpServer;
import org.apichart.common.modules.util.BaseUtils;

import java.io.File;
import java.io.IOException;

import static org.apichart.common.modules.util.BaseUtils.pub_key;
import static org.apichart.common.modules.util.BaseUtils.sub_key;


/**
 * 基于SpringBoot, 提供SpringBoot的各个生命周期的方法。
 *
 * @param
 * @author easton
 * @date 2022/4/21 15:14
 * @return null
 */
public class AbstractApplicationRunListener implements SpringApplicationRunListener {
    private final SpringApplication application;
    private final String[] args;
    private final String  fp = File.separator;
    /**
     * 项目运行所在的目录
     */
    private String user_root = System.getProperty("user.dir").concat(fp);

    /**
     * 公钥源文件存放目录
     */
    private String source_kyscert_dir = "/certs/pdp_public_certs.keystore";
    /**
    * 公钥新文件名
    */
    private String cert_file = "pdp_certs.keystore";

    /**
     * 证书源文件存放目录
     */
    private String source_license_dir = "/certs/Apache-2.0.lic";
    /**
     * 证书新文件名
     */
    private  String license_file = "Apache-2.0.lic";

    /**
     * 证书及公钥文件临时存放目录
     */
    private String tmp_dir = user_root.concat("tmp").concat(fp);

    private String print_str = "";

    public AbstractApplicationRunListener(SpringApplication sa, String[] args) {
        this.application = sa;
        this.args = args;
    }

    @Override
    public void starting() {
        // 在run()方法开始执行时，该方法就立即被调用，可用于在初始化最早期时做一些工作
        //System.out.println("服务启动RunnerTest SpringApplicationRunListener的starting方法...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        // 当environment构建完成，ApplicationContext创建之前，该方法被调用
        // System.out.println("服务启动 SpringApplicationRunListener的environmentPrepared方法...");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        // 当ApplicationContext构建完成时，该方法被调用
        // System.out.println("服务启动 SpringApplicationRunListener的contextPrepared方法...");
        LicenseVerifyParam param = new LicenseVerifyParam();
        BaseUtils utils = new BaseUtils();
        // 判定项目启动目录有没有写入权限

        // 在项目启动根目录tmp并在目录中创建license证书文件
        String private_license_path = utils.InputStreamAsSave(source_license_dir,tmp_dir,license_file);
        // 在项目启动根目录tmp并在目录中创建certKey公钥文件
        String public_keysStore_path = utils.InputStreamAsSave(source_kyscert_dir,tmp_dir,cert_file);

        try {
            param.setSubject("pdp_license_local");
            param.setPublicAlias("pdp_public_cert");
            param.setStorePass("public_pswd8353");
            //param.setLicensePath("/data/license.lic");
            param.setLicensePath(private_license_path);
            param.setPublicKeysStorePath(public_keysStore_path);

        } catch (Exception e) {

            if(pub_key.length() > 0){
                print_str = pub_key.replace("&"," ");
                print_str = print_str + sub_key.replace("&"," ");
                System.out.println(print_str+" 0");
            }

            System.exit(0);
        }
        LicenseVerify licenseVerify = new LicenseVerify();
        //安装证书
        licenseVerify.install(param);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        // 在ApplicationContext完成加载，但没有被刷新前，该方法被调用
        // System.out.println("服务启动 SpringApplicationRunListener的contextLoaded方法...");

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        // 当应用运行出错时该方法被调用
        // System.out.println("服务启动 SpringApplicationRunListener的failed方法...");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        // 在ApplicationContext刷新并启动后，CommandLineRunners和ApplicationRunner未被调用前，该方法被调用
        // System.out.println("服务启动 SpringApplicationRunListener的started方法...");
        try {
            // 启动HTTP服务
            new HttpServer();
            System.out.println(" - published root WebApplicationContext as Servlet http service exec with classes [HTTPD]");
            // 公钥使用后,删除此公钥文件
            //Path path = Paths.get(cert_dir.concat(cert_file));
            //Files.deleteIfExists(path);
        } catch (IOException ioe) {
            System.out.println(" - couldn't start server:" + ioe.getMessage());
        }

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        // 在run()方法执行完成前该方法被调用
        //System.out.println("服务启动 SpringApplicationRunListener的running方法...");

    }
}
