package org.nju.demo.controller;

import org.nju.demo.entity.ACode;
import org.nju.demo.entity.AUser;
import org.nju.demo.entity.Project;
import org.nju.demo.service.CodeService;
import org.nju.demo.utils.OssUtil;
import org.nju.demo.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class CodeController {
    @Autowired
    private CodeService codeService;

    @RequestMapping("/view/codes")
    public String viewProjects(){
        return "code_list";
    }

    @ResponseBody
    @RequestMapping("/codes")
    public List<ACode> getCodes(){
        return codeService.getAllCodes();
    }

    @RequestMapping(value = "/addCode",method = RequestMethod.POST)
    public String addCode(@RequestParam("codeName") String codeName,
                          @RequestParam("description") String description,
                          @RequestParam("fileInput") MultipartFile[] files) throws IOException {
        ACode aCode = new ACode();
        aCode.setCodeName(codeName);
        aCode.setDescription(description);
        codeService.addCode(aCode);
        OssUtil.upload(codeName,files);
        return "redirect:/view/projects";
    }

    @ResponseBody
    @RequestMapping("/code")
    public int getCode(@RequestParam("codeName") String codeName){
        List<ACode> aCodeList = codeService.getCodesByCodeName(codeName);
        if (aCodeList.size()!=0) return 1;
        else return  0;
    }
}
