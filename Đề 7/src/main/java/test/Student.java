package test;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
@Table(name ="Student")
public class Student {
	@Id
	@NotBlank(message = "Mã sinh viên không được để trống")
    private long id;

    @NotBlank(message = "Tên sinh viên không được để trống")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống")
    private Date dob;

    @NotBlank(message = "Phòng ban không được để trống")
    private String department;
	private int approved;
}
