package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class FileController {
    /**
     * markedown上传图片功能，当因为没有服务器等其他因数，并不会保存用户的图片，只会返回本地的一张图片
     *
     * @return
     */
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/editor.md-master/images/logos/vi.png");
        return fileDTO;
    }
}
