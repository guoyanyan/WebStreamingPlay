package com.jianghujiuji.streaming.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/file")
public class FileController {

    private static final Log logger = LogFactory.getLog(FileController.class);
    @Autowired
    private ServletContext   servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping("/download")
    public void fileDownload(HttpServletResponse response) {
        // 获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
        String path = servletContext.getRealPath("/");
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
        response.setHeader("Content-Disposition", "attachment;fileName=" + "see.mp3");
        ServletOutputStream out;
        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        String mp3Path =path+"/mp3/see.mp3";
        logger.debug("mp3Path is "+mp3Path);
        File file = new File(mp3Path);
        
        try {
            FileInputStream inputStream = new FileInputStream(file);
            // 3.通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();

            int b = 0;
            byte[] buffer = new byte[512];
            while (b != -1) {
                b = inputStream.read(buffer);
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
