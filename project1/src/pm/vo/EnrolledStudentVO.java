package pm.vo;

public class EnrolledStudentVO {

    //강의 인덱스, 수강생 인덱스, 학생 인덱스
    private String lec_no, lec_stdno, stdno;
    //강의이름, 학생 이름, 학생 핸드폰 번호
    private String lec_name, std_name, std_phone;

    public String getLec_stdno() {
        return lec_stdno;
    }

    public void setLec_stdno(String lec_stdno) {
        this.lec_stdno = lec_stdno;
    }

    public String getStdno() {
        return stdno;
    }

    public void setStdno(String stdno) {
        this.stdno = stdno;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }

    public String getLec_name() {
        return lec_name;
    }

    public void setLec_name(String lec_name) {
        this.lec_name = lec_name;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public String getStd_phone() {
        return std_phone;
    }

    public void setStd_phone(String std_phone) {
        this.std_phone = std_phone;
    }
}