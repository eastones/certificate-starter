package org.apichat.common.modules.context;

import de.schlichtherle.license.*;
import org.apichat.common.modules.entity.LicenseVerifyParam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

/**
 * License校验类
 */
public class LicenseVerify {
    /**
     * 启动时安装License证书
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
       // DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Path license_path = Paths.get(param.getLicensePath());
        Path keyStore_path = Paths.get(param.getPublicKeysStorePath());
        //1. 安装证书
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();

            result = licenseManager.install(new File(param.getLicensePath()));
            //System.out.println(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",format.format(result.getNotBefore()),format.format(result.getNotAfter())));
            //System.out.println("Spring information certificate issuance successfull!  1");
            String timeStr1=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            System.out.println(timeStr1 + " logger  [main] INFO  Java version:".concat(System.getProperty("java.version")).concat(",1;"));
        } catch (Exception e) {
            //System.out.println("证书安装失败！"+e.getMessage().toString());
            // 使用后,删除此公钥和证书文件
            try {
                Files.deleteIfExists(license_path);
                Files.deleteIfExists(keyStore_path);
            } catch (Exception exception) {

            }
            System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -01");
            System.exit(0);
        } finally {
            // 使用后,删除此公钥和证书文件
            try {
                Files.deleteIfExists(license_path);
                Files.deleteIfExists(keyStore_path);
            } catch (Exception exception) {

            }
        }
        return result;
    }

    /**
     * 校验License证书
     *
     * @return boolean
     */
    public boolean verify() {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            //System.out.println(licenseContent.getSubject());
            // System.out.println(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));
            //System.out.println("Spring information certificate iissuance successful!  0");
            return true;
        } catch (Exception e) {
            //System.out.println("证书校验失败！"+e);
           // System.err.println("Spring information certificate iissuance unsuccessful!  -4");
            System.out.println("could you try to remove all tags,beans conflict in spring boot might automatically know which versions to use,please exclude and try again!  -4");

            return false;
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     * @return de.schlichtherle.context.LicenseParam
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }
    public static void main(String[] args) {
        // yyyy-MM-dd HH:mm:ss.SSS  ---> 年-月-日 时-分-秒-毫秒   （想删掉哪个小部分就直接删掉哪个小部分）

        String timeStr2=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.println("当前时间为:"+timeStr2);
    }
}
