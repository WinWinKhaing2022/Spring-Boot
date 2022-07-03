package com.example.student.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.student.dao.UserDao;
import com.example.student.dto.UserRequestDto;
import com.example.student.dto.UserResponseDto;
import com.example.student.model.UserBean;


@Controller
public class UserController {
	@Autowired
	private UserDao userDao;
	
	@ModelAttribute("ubean")
	public UserBean getUserBean() {
		return new UserBean();
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
		public ModelAndView login() {
			return new ModelAndView("LGN001","ubean",new UserBean());					
		}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String setlogin(@RequestParam("email")String email,@RequestParam("password")String password,HttpSession session,@ModelAttribute("ubean") UserBean bean, ModelMap model) {
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date(System.currentTimeMillis());
	String currentdate = formatter.format(date);
	bean.setEmail(email);
	bean.setPassword(password);
		if (userDao.checkLogin(email, password)) {
			UserResponseDto dto = userDao.selectLoginOneEmail(email);
			session.setAttribute("userdata", dto);
			session.setAttribute("date", currentdate);
			return "MNU001";
		} 
		else {
			model.addAttribute("ubean", bean);
			model.addAttribute("error", "Email and Password do not match!!!");
			return "LGN001";
		}

	}
	
	@RequestMapping(value="/menu",method=RequestMethod.GET)
	public String Menu() {
		return "MNU001";
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String Logout(ModelMap model,HttpSession session) {
		session.removeAttribute("userdata");
		session.invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping(value="/showuser",method=RequestMethod.GET)
	public String showUser(ModelMap model) {
		List<UserResponseDto> list=userDao.selectAllUsers();
			model.addAttribute("list", list);
		
		return "USR003";
	}
	 
	@RequestMapping(value="/searchUser",method=RequestMethod.POST)
	public String searchUser(@RequestParam("id")String id,@RequestParam("name")String name,ModelMap model) {
		List<UserResponseDto> searchlist=null;
		if(name.isEmpty() && id.isEmpty()) {
			searchlist=userDao.selectAllUsers();
			System.out.println("Search All"+searchlist);
		}
			else if(id.isEmpty()) {
			searchlist=userDao.selectNameSearch(name);
		}		
		else if(name.isEmpty()) {
			searchlist=userDao.selectIdSearch(id);
		}		
		else {
			searchlist =userDao.selectNameAndIdSearch(id,name);
			
		}
		model.addAttribute("list", searchlist);
		return "USR003";
	}
	@RequestMapping(value="/setupAddUser",method=RequestMethod.GET)
	public ModelAndView setupaddUser() {
		return new ModelAndView("USR001","ubean",new UserBean());
	}
	
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	public String addUser(@ModelAttribute("ubean") @Validated UserBean bean,BindingResult bs, ModelMap model) {
		
		if(bs.hasErrors()) {
			return "USR001";
		}
		String name=bean.getName();
		String email=bean.getEmail();
		String password=bean.getPassword();
		String cpassword=bean.getCpassword();
		String role=bean.getRole();
		if(name.isBlank()||email.isBlank()||password.isBlank()||cpassword.isBlank()||role.isBlank()) {
			model.addAttribute("error","Filed cannot be blank!!!");
			model.addAttribute("ubean",bean);
			return "USR001";
		}
		else  if(!password.equals(cpassword)){
			model.addAttribute("error","Password do not match");
			model.addAttribute("ubean", bean);
			return "USR001";
		}
		else {
			List<UserResponseDto> list=userDao.selectAllUsers();
			if(userDao.checkEmailExist(email)) {
				model.addAttribute("error", "Email already exist!!");
				model.addAttribute("ubean",bean);
				return "USR001";
			}
			else {
				if (list == null) {
					list = new ArrayList<>();
				}
				else if(list.size()==0) {
					bean.setId("USR001");
				}
				else {
					int id=Integer.parseInt(list.get(list.size()-1).getId().substring(3))+1;
					String userid=String.format("USR%03d", id);
					bean.setId(userid);
				}
				UserRequestDto reqdto=new UserRequestDto();
				reqdto.setId(bean.getId());
				reqdto.setName(bean.getName());
				reqdto.setEmail(bean.getEmail());
				reqdto.setPassword(bean.getPassword());
				reqdto.setRole(bean.getRole());
				int result=0;
				result=userDao.insertUserData(reqdto);
				if(result==0) {
					
					model.addAttribute("error","Insert fail!!!");
					return "USR001";
				}
				else
					return "redirect:/showuser";
				}
			}
		}
	
	@RequestMapping (value="/setupUpdate", method=RequestMethod.GET)
	public	 ModelAndView Update(ModelMap model,@RequestParam("id") String id) {
	UserResponseDto resdto = userDao.selectOneId(id);
	UserBean user=new UserBean();
	user.setId(resdto.getId());
	user.setName(resdto.getName());
	user.setEmail(resdto.getEmail());
	user.setPassword(resdto.getPassword());
	user.setCpassword(resdto.getPassword());
	user.setRole(resdto.getRole());
	return new ModelAndView("USR002","ubean",user);
}

@RequestMapping(value = "/UpdateUser", method = RequestMethod.POST)
public String updateUser(@ModelAttribute("ubean") @Validated UserBean ubean, BindingResult bs,ModelMap model,HttpSession session,HttpServletRequest req)  {
		if(bs.hasErrors()) {
			return "USR002";
		}
		String id=ubean.getId();
		String name=ubean.getName();
		String email=ubean.getEmail();
		String password=ubean.getPassword();
		String cpassword=ubean.getCpassword();
		String role=ubean.getRole();
		UserResponseDto sessiondata = (UserResponseDto) session.getAttribute("userdata");
		
		 if(name.isBlank()||email.isBlank()||password.isBlank()||cpassword.isBlank()||role.isBlank()) {
			model.addAttribute("error","Filed cannot be blank!!!");
			return"USR002";
		} 
		else if (!password.equals(cpassword)) {
			model.addAttribute("error", "Do not match password and cpassword!!");
			return "USR002";
		}
		else {
			
			
			UserResponseDto res=userDao.selectOneId(id);
			UserRequestDto reqdto=new UserRequestDto(id,name,email,password,role);
			if(!res.getEmail().equals(email)) {
				if(userDao.checkEmailExist(email)) {
					model.addAttribute("error","Email already Exist");
					return "USR002";
				} 
				else {
						int rs=userDao.UpdateUserData( reqdto);
						if(rs==0) {
							model.addAttribute("error","Update Failed");
							return "USR002";
						}
						if(reqdto.getEmail().equals(sessiondata.getEmail())) {
							session.setAttribute("userdata", reqdto);
						}
						return "redirect:/showuser";
				}
			}
			else {
				int rs=userDao.UpdateUserData(reqdto);
				if(rs==0) {
					model.addAttribute("error","Update Failed");
					return "USR002";
				}
				if(reqdto.getEmail().equals(sessiondata.getEmail())) {
					UserResponseDto resdto=new UserResponseDto(id,name,email,password,role);
					session.setAttribute("userdata",resdto);
				}
				return "redirect:/showuser";
				
			}
		}
}

	@RequestMapping (value="/deleteUser", method=RequestMethod.GET)
	public	String Delete(ModelMap model,@RequestParam("id") String id) {
	
		UserResponseDto dto=userDao.selectOneId(id);
		UserRequestDto reqdto=new UserRequestDto();
		reqdto.setId(dto.getId());
		reqdto.setName(dto.getName());
		reqdto.setEmail(dto.getEmail());
		reqdto.setPassword(dto.getPassword());
		reqdto.setRole(dto.getRole());
		userDao.deleteUser(reqdto);
		return "redirect:/showuser";
		
	}
}

