package com.example.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.student.dao.CourseDao;
import com.example.student.dto.CourseRequestDto;
import com.example.student.dto.CourseResponseDto;
import com.example.student.model.CourseBean;


@Controller

public class CourseController {
	
	@Autowired
	CourseDao cdao;

	@RequestMapping(value="/setupaddcourse", method=RequestMethod.GET)
	 public ModelAndView setupcourse() {
		 return new ModelAndView("BUD003","cbean",new CourseBean());
	 }
	 @RequestMapping(value="/addcourse",method=RequestMethod.POST)
	 public String addcourse(@ModelAttribute("cbean") @Validated CourseBean bean,BindingResult bs,ModelMap model) {
		 if(bs.hasErrors()) {
			 return "BUD003";
		 }
		 
		 String name=bean.getName();
		 if(name.trim().equals("") || name.isEmpty() || name == null) {
				model.addAttribute("error", "Filed can not blank!!");
				return"BUD003";
		 }	
		 else if(cdao.checkName(bean.getName())){
			 model.addAttribute("data", bean);
			 model.addAttribute("error", "Name already exists");
			 return "BUD003";
		 }
		 else {
				List<CourseResponseDto> courseList=cdao.selectAllCourse();
				
				if(courseList.size()<=0||courseList==null) {
					bean.setId("COU001");
				}
				else{
					CourseResponseDto lastDTO = courseList.get(courseList.size()-1);
				int lastId = Integer.parseInt(lastDTO.getId().substring(3));
				String userId = String.format("COU"+"%03d", lastId+1);
					bean.setId(userId);
				}
				CourseRequestDto reqdto=new CourseRequestDto();
				reqdto.setId(bean.getId());
				reqdto.setName(bean.getName());
				int result=cdao.insertCourse(reqdto);
				if(result>0) {
					model.addAttribute("msg", "Register Successfull!!!");
				}
				else {
					model.addAttribute("msg", "Insert Fail!!!");
				}
				return "BUD003";	
					
				}
	 }
}


