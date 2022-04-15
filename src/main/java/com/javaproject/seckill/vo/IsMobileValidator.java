package com.javaproject.seckill.vo;

import com.javaproject.seckill.utils.ValidatorUtil;
import com.javaproject.seckill.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {

        required = constraintAnnotation.required();
    }

    /**
     *
     * @param s 需要校验的值 实际传入的值
     * @param constraintValidatorContext 上下文信息
     * @return
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if(required){
            return ValidatorUtil.isMobile(s);
        }
        else{
            if(!StringUtils.hasLength(s)){
                return true;
            }
            else{
                return ValidatorUtil.isMobile(s);
            }
        }

    }
}
