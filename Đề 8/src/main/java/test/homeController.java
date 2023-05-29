package test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class homeController {
	@Autowired
	private StudentRepository studentRepo;
	
	@GetMapping
	public String home(Model model) {
		return "home";
	}
	

	@GetMapping ("/view")
	public String viewList(Model model) {
		List<Student> students = (List<Student>) studentRepo.findAll();
		model.addAttribute("students", students);
		return "view"; 
	}
	
	@GetMapping("view/approved/{id}") 
	public String approvedStudent(@PathVariable("id") Long id) {
		Student student = studentRepo.findById(id).orElse(null); 
		if (student != null) {
			student.setApproved(1);
			studentRepo.save(student);
		}
		return "redirect:/view";
	}
	
	@GetMapping("view/delete/{id}") 
	public String deleteStudent(@PathVariable("id") Long id,Model model) {
		Student student = studentRepo.findById(id).orElse(null); 
		if (student != null) {
			model.addAttribute("student", student);
		}else model.addAttribute("student", new Student());
		return "delete";
	}
	@GetMapping("view/delete/confirm/{id}") 
	public String confirmDeleteStudent(@PathVariable("id") Long id) {
		studentRepo.deleteById(id);
		return "redirect:/view";
	}
	
}
