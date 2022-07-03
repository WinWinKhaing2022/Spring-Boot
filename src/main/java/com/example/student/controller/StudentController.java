package com.example.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.student.dao.CourseDao;
import com.example.student.dao.StudentDao;
import com.example.student.dto.CourseResponseDto;
import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;
import com.example.student.model.StudentBean;


@Controller
public class StudentController {

	 @Autowired
	 private StudentDao sdao;
	 @Autowired
	 private CourseDao cdao;
	 	 
	 
	 	@RequestMapping(value="/setupaddstudent",method=RequestMethod.GET)
	 	public ModelAndView setupaddstudent(ModelMap model) {
	 		List<CourseResponseDto> list=cdao.selectAllCourse();
			model.addAttribute("courseList", list);
			return new ModelAndView("STU001","sbean",new StudentBean());
	 	}
	 	
	 	@RequestMapping(value="/addstudent",method=RequestMethod.POST)
	 	public String addStudent(@ModelAttribute("sbean") @Validated StudentBean sbean,BindingResult br,ModelMap model) {
	 		if(br.hasErrors()) {
	 			List<CourseResponseDto> list=cdao.selectAllCourse();
				model.addAttribute("courseList", list);
	 			return "STU001";
	 		}
	 		String name=sbean.getName();
	 		String dob=sbean.getDob();
	 		String gender=sbean.getGender();
	 		String phone=sbean.getPhone();
	 		String education=sbean.getEducation();
	 		List<String>attendCourses=sbean.getAttendCourses();
	 		if(attendCourses==null) {
	 			model.addAttribute("error","Courses field can not be blank!!");
	 			model.addAttribute("data",sbean);
	 			return "STU001";
	 		}
	 		else 	if (name.isBlank() || dob.isBlank() || gender.isBlank() || phone.isBlank()|| education.isBlank() || attendCourses.isEmpty()) {
	 				model.addAttribute("data",sbean);
	 				model.addAttribute("error","Field can not be blank!!");
	 				return "STU001";
	 				}
	 		else {
	 					List<StudentResponseDto> studentList = sdao.selectAllStudents();
	 					
	 					if (studentList.size() == 0) {
	 						sbean.setId("STU001");

	 					} 
	 					else {
	 						int tempId = Integer.parseInt(studentList.get(studentList.size() - 1).getId().substring(3)) + 1;
	 						String userId = String.format("STU%03d", tempId);
	 						sbean.setId(userId);
	 					}
	 					StudentRequestDto reqDto = new StudentRequestDto(name, dob, gender, phone, education);
	 					reqDto.setId(sbean.getId());
	 					System.out.println(reqDto);
	 					sdao.insertStudentData(reqDto);
	 					
	 					for (String course:attendCourses) {
	 						sdao.addCourseForStrudent( sbean.getId(),course);
	 					}
	 			
	 					
	 					return "redirect:/showstudent";
	 		}
	 	}
	 	
	 	 @RequestMapping (value="/showstudent",method=RequestMethod.GET)
		 	public  String setupSearch(ModelMap model) {
		 	List<StudentResponseDto> studentList = sdao.selectAllStudents();
			for (StudentResponseDto student : studentList) {
				List<String> courseList = cdao.selectCoursesByStudentid(student.getId());
				student.setAttendCourses(courseList);
			}
				model.addAttribute("studentList", studentList);
				return "STU003";
	 	}

	 	@RequestMapping(value="/searchstudent",method=RequestMethod.POST)
	 	public	String searchStudent(ModelMap model,@RequestParam("id") String id,@RequestParam("name") String name,@RequestParam("attendCourses") String attendCourses) {
	 			String sid= id.isBlank()?"@#$%": id;
	 			String sname=name.isBlank()?"@#$%" :name;
	 			String scourse=attendCourses.isBlank()?"@#$%" : attendCourses;
	 			
	 			
	 			List<StudentResponseDto> studentList = sdao.selectStudentListByIdOrNameOrCourse(sid, sname, scourse);
	 			for (StudentResponseDto student : studentList) {
	 			List<String> courseList = (List<String>)cdao.selectCoursesByStudentid(student.getId());
	 				student.setAttendCourses(courseList);
	 			}
	 		
	 			if (studentList.size() == 0) {
	 				studentList = sdao.selectAllStudents();
	 				for (StudentResponseDto student : studentList) {
	 					List<String> courseList = cdao.selectCoursesByStudentid(student.getId());
	 					student.setAttendCourses(courseList);
	 				}
	 				model.addAttribute("studentList", studentList);
	 				
	 				return "STU003";
	 			} 
	 			else {
	 				model.addAttribute("studentList", studentList);
	 				
	 				return"STU003";
	 			}
	 		}
	 		
	 	@RequestMapping (value="/seemore", method=RequestMethod.GET)
	 		public	ModelAndView seeMore(ModelMap model,@RequestParam("id") String id) {
	 			
	 				StudentResponseDto student = sdao.selectStudentById(id);
	 				
	 				List<CourseResponseDto> courses = cdao.selectAllCourse();
	 				
	 				List<CourseResponseDto> attendCourses = cdao.selectCoursesByStudentId(id);
	 				
	 				ArrayList<String> stuCourseList=new ArrayList<String>();
	 				for(CourseResponseDto course:attendCourses) {
	 					stuCourseList.add(course.getId());
	 				}
	 				StudentBean sbean=new StudentBean();
	 				sbean.setId(student.getId());
	 				sbean.setName(student.getName());
	 				sbean.setDob(student.getDob());
	 				sbean.setGender(student.getGender());
	 				sbean.setPhone(student.getPhone());
	 				sbean.setEducation(student.getEducation());
	 				sbean.setAttendCourses(stuCourseList);
	 				model.addAttribute("courses",courses);
	 				return new ModelAndView("STU002","sbean",sbean);
	 			}
	 	
	 
	 	
	 				
	 		@RequestMapping(value="/updatestudent",method=RequestMethod.POST)
	 			public String updateStudent	(ModelMap model,@ModelAttribute ("sbean") @Validated StudentBean sbean,BindingResult br) {
	 			if(br.hasErrors()) {
	 				List<CourseResponseDto> list=cdao.selectAllCourse();
	 				model.addAttribute("courses", list);
	 				return "STU002";
	 			}
	 			String id=sbean.getId();
	 			String name=sbean.getName();
	 	 		String dob=sbean.getDob();
	 	 		String gender=sbean.getGender();
	 	 		String phone=sbean.getPhone();
	 	 		String education=sbean.getEducation();
	 	 		List<String>attendCourses=sbean.getAttendCourses();
	 		
	 			if (attendCourses == null) {
	 				sbean = new StudentBean(id, name, dob, gender, phone, education);
	 				model.addAttribute("error", "Fill the blank !!");
	 				
	 				return "STU002";
	 			}
	 			else {
	 				 if (name.isBlank() || dob.isBlank() || gender.isBlank() || phone.isBlank() || education.isBlank()) {
	 					
	 					sbean = new StudentBean(id, name, dob, gender, phone, education, attendCourses);
	 					model.addAttribute("error", "Fill the blank !!");
	 					
	 					return "STU002";
	 					
	 				 }
	 				 else {
	 					StudentRequestDto reqDto = new StudentRequestDto(id, name, dob, gender, phone, education);
	 					sdao.updateStudent(reqDto);
	 					sdao.deleteAttendCoursesByStudentId(id);
	 					for (String course:attendCourses) {
	 						sdao.addCourseForStrudent( reqDto.getId(),course);
	 						
	 					}
	 					return "redirect:/showstudent";
	 				 }
	 			
	 		}
	 			
	 	}
	 		@RequestMapping (value="/Deletestudent", method=RequestMethod.GET)
	 		public	String deleteStudent(ModelMap model,@RequestParam("id") String stuid) {
	 			sdao.deleteStudentById(stuid);
	 			sdao.deleteAttendCoursesByStudentId(stuid);
	 			return "redirect:/showstudent";
	 			
	 		}
	 		
}
