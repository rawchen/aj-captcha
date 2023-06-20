package com.rawchen.ajcaptcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author RawChen
 * @date 2023-06-16 13:55
 */
@Controller
public class AJCaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
	 * 二次校验
     *
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseModel get(@RequestParam("captchaVerification") String captchaVerification,
                             @RequestParam("userName") String userName,
                             @RequestParam("password") String password) {
        //必传参数：captchaVO.captchaVerification
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        if (response.isSuccess()) {
            System.out.println("CaptchaVerification: " + captchaVO.getCaptchaVerification());
            if ("123".equals(userName) && "456".equals(password)) {
                response.setRepData("后台验证成功");
            } else {
                response.setRepData("后台验证失败");
                System.out.println("userName: " + userName);
                System.out.println("password: " + password);
            }
        } else {
            System.out.println("err code: " + response.getRepCode());
            System.out.println("err msg: " + response.getRepMsg());
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
            //repCode  6113  底图未初始化成功，请检查路径
            //repCode  6201  get接口请求次数超限，请稍后再试!
            //repCode  6206  无效请求，请重新获取验证码
            //repCode  6202  接口验证失败数过多，请稍后再试
            //repCode  6204  check接口请求次数超限，请稍后再试!
        }
        return response;
    }

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

}
