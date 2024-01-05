package org.apichat.common.modules.origin;

import com.alibaba.fastjson.JSON;
import fi.iki.elonen.NanoHTTPD;
import org.apichat.common.modules.context.AbstractServerInfos;
import org.apichat.common.modules.context.LicenseVerify;
import org.apichat.common.modules.context.LinuxServerInfos;
import org.apichat.common.modules.context.WindowsServerInfos;
import org.apichat.common.modules.entity.LicenseCheckModel;

import java.io.IOException;

/**
 * 轻量级的HTTP服务器
 * 支持 GET, POST, PUT, HEAD 和 DELETE 请求，支持文件上传，占用内存很小。
 *
 * @author easton
 * @date 2022/4/18 12:16
 * @return null
 */
public class HttpServer extends NanoHTTPD {
    private final static int PORT = 6195;

    public HttpServer() throws IOException {
        super(PORT);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        //System.out.println("\nRunning! Point your browsers to http://localhost:6195/ \n");
    }

    public static void main(String[] args) {
        try {
            new HttpServer();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        String responseMsg = new String("");
        // 获取操作系统信息
        if ("/info".equals(uri) || "/".equals(uri)) {

            String osName = System.getProperty("os.name");
            osName = osName.toLowerCase();
            AbstractServerInfos abstractServerInfos = null;

            //根据不同操作系统类型选择不同的数据获取方法
            if (osName.startsWith("windows")) {
                abstractServerInfos = new WindowsServerInfos();
            } else if (osName.startsWith("linux")) {
                abstractServerInfos = new LinuxServerInfos();
            } else {
                //其他服务器类型
                abstractServerInfos = new LinuxServerInfos();
            }

            LicenseCheckModel license = abstractServerInfos.getServerInfos();
            responseMsg = JSON.toJSONString(license);

        }
        if ("/license".equals(uri)) {
            //System.out.println("验证证书是否可用");
            LicenseVerify licenseVerify = new LicenseVerify();
            //校验证书是否有效
            boolean verifyResult = licenseVerify.verify();
            if (verifyResult) {
                responseMsg = "验证成功，证书可用";
            } else {
                //System.out.println("验证失败，证书无效");
                responseMsg = "您的证书无效，请核查服务器是否取得授权或重新申请证书！";
               // System.exit(0);
            }
        }
        return newFixedLengthResponse(Response.Status.OK, "application/json;charset=UTF-8", responseMsg);

    }
}