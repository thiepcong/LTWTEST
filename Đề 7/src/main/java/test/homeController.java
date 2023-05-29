package test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class homeController {
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Student student1 = (Student) session.getAttribute("student1");
        if (student1 == null) {
            student1 = new Student();
            session.setAttribute("student1", student1);
        }
        model.addAttribute("student", new Student());
        return "home";
    }

    @PostMapping("/")
    public String confirm(Model model, HttpSession session, @Valid Student student,BindingResult bindingResult, Errors errors) {
      
        if(errors.hasErrors()) { 
			return "home";
		}

        Student student1 = (Student) session.getAttribute("student1");
        if (student1 == null) {
            student1 = new Student();
            session.setAttribute("student1", student1);
        }
        
        // Kiểm tra trùng lặp id
        if (studentRepo.existsById(student.getId())) {
        	errors.rejectValue("id", "error.student", "Mã sinh viên đã tồn tại");
            return "home";
        }
            
        // Kiểm tra nếu trường name chưa được nhập
        if (student.getName().isEmpty()) {
            errors.rejectValue("name", "error.student", "Tên sinh viên chưa được nhập");
            return "home";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(student.getDob().toString(), formatter);
            student1.setDob(Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (DateTimeParseException e) {
            bindingResult.rejectValue("dob", "error.student", "Ngày sinh không đúng định dạng dd/MM/yyyy");
            return "home";
        }

        if (student.getDob() == null || student.getDob().toString().isEmpty()) {
            errors.rejectValue("dob", "error.student", "Ngày sinh chưa được nhập");
            return "home";
        }

       

        // Kiểm tra nếu trường department chưa được chọn
        if (student.getDepartment().isEmpty()) {
            errors.rejectValue("department", "error.student", "Phòng ban chưa được chọn");
            return "home";
        }
           
        
        student1.setId(student.getId());
        student1.setDepartment(student.getDepartment());
        student1.setDob(student.getDob());
        student1.setName(student.getName());
        student1.setApproved(0);
        
        return "confirm";
    }

    @GetMapping("/confirm")
    public String saveStudent(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student1");
        studentRepo.save(student);
        session.removeAttribute("student1");
        model.addAttribute("student", new Student());
        return "home";
    }
}
