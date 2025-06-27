package pm.vo;

public class StudentVO {
    private String stdno, std_name, std_address, std_phone, std_email;
    private String lec_no, lec_stdno, lec_name;//선영수정_추가

    private MemberVO mvo;

    public MemberVO getMvo() {
        return mvo;
    }

    public void setMvo(MemberVO mvo) {
        this.mvo = mvo;
    }

    public String getStdno() {
        return stdno;
    }

    public void setStdno(String stdno) {
        this.stdno = stdno;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public String getStd_address() {
        return std_address;
    }

    public void setStd_address(String std_address) {
        this.std_address = std_address;
    }

    public String getStd_phone() {
        return std_phone;
    }

    public void setStd_phone(String std_phone) {
        this.std_phone = std_phone;
    }

    public String getStd_email() {
        return std_email;
    }

    public void setStd_email(String std_email) {
        this.std_email = std_email;
    }

    public String getLec_no() {
        return lec_no;
    }

    public void setLec_no(String lec_no) {
        this.lec_no = lec_no;
    }

    public String getLec_stdno() {
        return lec_stdno;
    }

    public void setLec_stdno(String lec_stdno) {
        this.lec_stdno = lec_stdno;
    }

    public String getLec_name() {
        return lec_name;
    }

    public void setLec_name(String lec_name) {
        this.lec_name = lec_name;
    }
}
